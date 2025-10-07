package org.jeecg.dataingest.projectworker.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.openapi.service.OpenApiService;
import org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties;
import org.jeecg.dataingest.projectworker.config.ProjectWorkerProjectRegistry;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.jeecg.dataingest.projectworker.util.ProjectWorkerUtils.*;

/**
 * 工地项目人员考勤同步服务
 * 从宜昌实名制平台系统获取“打卡信息”，推送到 D6C ProjectWorkerAttendance.Upload
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectWorkerAttendanceSyncService {

    private final OpenApiService openApiService;
    private final ProjectWorkerIntegrationProperties props;
    private final ProjectWorkerProjectRegistry projectRegistry;
    private final D6CApiService d6cApiService;
    private final AttendanceIdentityResolver identityResolver;
    private final RedisPersonIndexCache personIndexCache;

    private final AtomicLong lastAApiMs = new AtomicLong(0);
    private final AtomicLong lastD6cMs = new AtomicLong(0);

    @Data
    public static class SyncResult {
        private int fetched;
        private int pushed;
    }

    /**
     * 载荷及元信息，用于重复冲突时优先“劳务身份”（team 非空）。
     */
    private static class PayloadWithMeta {
        final Map<String, Object> payload;
        final boolean laborIdentity;

        private PayloadWithMeta(Map<String, Object> payload, boolean laborIdentity) {
            this.payload = payload;
            this.laborIdentity = laborIdentity;
        }
    }

    /**
     * 将一条考勤（已映射为 D6C 载荷）按 (idCardNumber, workerName, attendTime) 在 engId 维度去重；
     * 当发生冲突时，优先保留 laborIdentity=true（team 非空）的记录。
     */
    private void addAttendanceWithDedup(Map<String, LinkedHashMap<String, PayloadWithMeta>> byEngId,
                                        String engId,
                                        String idCardNumber,
                                        String workerName,
                                        String attendTime,
                                        boolean laborIdentity,
                                        Map<String, Object> payload) {
        if (isBlank(engId) || isBlank(idCardNumber) || isBlank(attendTime)) { return; }
        String normName = nvl(workerName).trim();
        String key = idCardNumber + "|" + normName + "|" + attendTime;
        LinkedHashMap<String, PayloadWithMeta> dedup = byEngId.computeIfAbsent(engId, k -> new LinkedHashMap<>());
        PayloadWithMeta exists = dedup.get(key);
        if (exists == null) {
            dedup.put(key, new PayloadWithMeta(payload, laborIdentity));
            return;
        }
        // 若已存在为管理身份（非劳务），当前为劳务，则进行替换；否则保持已存在
        if (!exists.laborIdentity && laborIdentity) {
            dedup.put(key, new PayloadWithMeta(payload, true));
        }
    }

    /**
     * 同步指定人员某天的考勤
     */
    public SyncResult sync(String engId, String code, String idCardNumber, String verifyDate) {
        if (isBlank(engId)) { throw new IllegalArgumentException("engId 不能为空"); }
        if (isBlank(idCardNumber)) { throw new IllegalArgumentException("idCardNumber 不能为空"); }
        // 入参必须为明文身份证，不能是脱敏展示串（如 1234**********5678）
        if (looksMasked(idCardNumber)) {
            throw new IllegalArgumentException("idCardNumber 不可为脱敏串，请传真实身份证号");
        }
        // 简单18/15位身份证格式校验（与前端保持一致）
        if (!isValidChineseIdCardFormat(idCardNumber)) {
            throw new IllegalArgumentException("idCardNumber 格式不正确");
        }

        // 计算有效 code：优先入参 code -> 注册表
        String effectiveCode = null;
        if (!isBlank(code)) {
            effectiveCode = code;
        } else {
            try {
                Optional<ProjectWorkerIntegrationProperties.Project> p = projectRegistry.findByEngId(engId);
                if (p.isPresent()) {
                    effectiveCode = nvl(p.get().getCode());
                }
            } catch (Exception ignore) {}
        }

        String date = isBlank(verifyDate) ? LocalDate.now().format(DateTimeFormatter.ISO_DATE) : verifyDate;

        JSONArray records = fetchAttendanceRecords(engId, effectiveCode, idCardNumber, date);
        if (records == null) { throw new IllegalStateException("宜昌考勤接口无数据或请求失败"); }

        // 回写候选/强映射
        safeWriteBackFromPrecise(engId, idCardNumber, records);

        List<Map<String, Object>> payload = mapToD6CAttendance(records, idCardNumber);

        Map<String, String> envelope = generateCommonAttributes(engId, null);
        String method = props.getD6cApi().getMethod().getAttendance();
        throttleD6c();
        d6cApiService.post(method, payload, envelope);

        SyncResult r = new SyncResult();
        r.setFetched(records.size());
        r.setPushed(payload.size());
        return r;
    }

    /**
     * 同步指定人员今日考勤（code 可不传）
     */
    public SyncResult sync(String engId, String idCardNumber) {
        return sync(engId, null, idCardNumber, LocalDate.now().format(DateTimeFormatter.ISO_DATE));
    }

    /**
     * 按日期索引分页同步（不带身份证号），结合 Redis 解析器还原真实身份证
     */
    public SyncResult syncByDateIndex(String engId, String code, String verifyDate) {
        if (isBlank(engId)) { throw new IllegalArgumentException("engId 不能为空"); }

        // 计算有效 code：优先入参 code -> 注册表
        String effectiveCode = null;
        if (!isBlank(code)) {
            effectiveCode = code;
        } else {
            try {
                Optional<ProjectWorkerIntegrationProperties.Project> p = projectRegistry.findByEngId(engId);
                if (p.isPresent()) {
                    effectiveCode = nvl(p.get().getCode());
                }
            } catch (Exception ignore) {}
        }

        String date = isBlank(verifyDate) ? LocalDate.now().format(DateTimeFormatter.ISO_DATE) : verifyDate;

        int pageSize = Math.max(1, props.getAttendance().getPageSize());
        int maxPages = Math.max(1, props.getAttendance().getMaxPagesPerDay());

        // 分 engId 聚合（去重层）：key = idCardNumber|workerName|attendTime
        Map<String, LinkedHashMap<String, PayloadWithMeta>> byEngIdDedup = new LinkedHashMap<>();
        // 单次调用期间，按 engId 缓存人员清单，避免对人员接口的重复请求
        Map<String, List<JSONObject>> personCacheByEngId = new HashMap<>();
        // 单次调用期间，缓存按身份证(明文)+engId+date 的精确查询结果，避免重复请求
        Map<String, JSONArray> preciseCache = new HashMap<>();
        // 防重复累加：避免同一 engId+realId+date 的精确结果被多次加入 payload
        Set<String> preciseAdded = new HashSet<>();
        // 本次任务内，按 engId 做一次性人员索引预热，避免内层循环重复触发
        Set<String> indexPreloaded = new HashSet<>();
        int totalFetched = 0;

        for (int page = 1; page <= maxPages; page++) {
            JSONArray records = fetchAttendanceIndex(engId, effectiveCode, date, page, pageSize);
            if (records == null || records.isEmpty()) { break; }
            totalFetched += records.size();

            for (int i = 0; i < records.size(); i++) {
                JSONObject rec = records.getJSONObject(i);
                String masked = toStr(rec.get("idCardShow"));
                String name = toStr(rec.get("name"));
                String actualEngId = toStr(rec.get("engId"));
                // 规则：仅使用记录中的实际 engId，缺失则跳过
                if (isBlank(actualEngId)) { continue; }
                // 当开启 dropUnknownEngId 时，若记录中的 engId 未在注册表中配置，则直接跳过
                if (props.getAttendance().isDropUnknownEngId()) {
                    Optional<ProjectWorkerIntegrationProperties.Project> known = projectRegistry.snapshotByEngId(actualEngId);
                    if (!known.isPresent()) { continue; }
                }

                String resolved = identityResolver.resolveStrong(masked, name, actualEngId);
                if (isBlank(resolved)) {
                    // 若候选集合唯一，尝试使用
                    Set<String> candidates = identityResolver.getCandidates(masked, name, actualEngId);
                    if (candidates != null && candidates.size() == 1) {
                        resolved = candidates.iterator().next();
                    }
                }
                if (isBlank(resolved)) {
                    // 先尝试从本地人员索引缓存获取候选（避免在内层循环中拉人像接口）
                    Set<String> localCands = personIndexCache.getCandidates(actualEngId, name, masked);
                    if (localCands != null && localCands.size() == 1) {
                        resolved = localCands.iterator().next();
                    } else if (localCands != null && !localCands.isEmpty()) {
                        for (String real : localCands) {
                            identityResolver.addCandidate(masked, name, actualEngId, real, Math.max(1, props.getAttendance().getCache().getCandidateTtlHours()));
                        }
                    }
                }
                if (isBlank(resolved)) {
                    // 本地索引未命中时，对该 engId 进行一次性人员索引预热，然后重试本地候选
                    if (!indexPreloaded.contains(actualEngId)) {
                        try {
                            JSONArray arr = fetchPersons(actualEngId, effectiveCode);
                            List<JSONObject> persons = new ArrayList<>();
                            if (arr != null) {
                                for (int t = 0; t < arr.size(); t++) { persons.add(arr.getJSONObject(t)); }
                            }
                            personCacheByEngId.put(actualEngId, persons);
                            int ttlDays = Math.max(1, props.getAttendance().getCache().getResolveTtlDays());
                            personIndexCache.indexPersons(actualEngId, persons, ttlDays);
                        } catch (Exception e) {
                            log.warn("人员索引预热失败 engId={} err={}", actualEngId, e.getMessage());
                        } finally {
                            indexPreloaded.add(actualEngId);
                        }
                        // 预热后再尝试一次本地候选
                        Set<String> retried = personIndexCache.getCandidates(actualEngId, name, masked);
                        if (retried != null && retried.size() == 1) {
                            resolved = retried.iterator().next();
                        } else if (retried != null && !retried.isEmpty()) {
                            for (String real : retried) {
                                identityResolver.addCandidate(masked, name, actualEngId, real, Math.max(1, props.getAttendance().getCache().getCandidateTtlHours()));
                            }
                        }
                    }
                }
                if (isBlank(resolved)) {
                    // 回退：通过人员信息接口按姓名+脱敏规则匹配真实身份证（按记录实际 engId 拉取）
                    resolved = tryResolveByPersonApi(masked, name, actualEngId, effectiveCode, personCacheByEngId);
                }
                if (isBlank(resolved)) {
                    // 歧义：对候选明文身份证逐个进行“精确查询”，以明文结果替代脱敏分页结果
                    Set<String> cands = identityResolver.getCandidates(masked, name, actualEngId);
                    if (cands != null && !cands.isEmpty()) {
                        for (String real : cands) {
                            try {
                                String key = actualEngId + "|" + real + "|" + date;
                                JSONArray precise = preciseCache.get(key);
                                if (precise == null) {
                                    precise = fetchAttendanceRecords(actualEngId, effectiveCode, real, date);
                                    if (precise != null) { preciseCache.put(key, precise); }
                                }
                                if (precise != null && !precise.isEmpty()) {
                                    // 回写解析缓存
                                    safeWriteBackFromPrecise(actualEngId, real, precise);
                                    // 转为 D6C 载荷并累加（同一去重器，劳务优先）
                                    if (preciseAdded.add(key)) {
                                        for (int pi = 0; pi < precise.size(); pi++) {
                                            JSONObject prec = precise.getJSONObject(pi);
                                            Map<String, Object> mapped = mapSingleAttendanceToD6C(prec, real);
                                            if (!mapped.isEmpty()) {
                                                String workerName2 = toStr(prec.get("workerName"));
                                                if (isBlank(workerName2)) { workerName2 = toStr(prec.get("name")); }
                                                String attendTime2 = resolveAttendTime(prec.get("verifyTime"), prec.get("checkDate"), prec.get("attTime"));
                                                boolean labor2 = !isBlank(toStr(prec.get("team")));
                                                addAttendanceWithDedup(byEngIdDedup, actualEngId, real, workerName2, attendTime2, labor2, mapped);
                                            }
                                        }
                                    }
                                }
                            } catch (Exception ignore) {}
                        }
                    }
                    // 本条脱敏记录不再直接输出
                    continue;
                }
                Map<String, Object> m = mapSingleAttendanceToD6C(rec, resolved);
                if (!m.isEmpty()) {
                    String workerName = toStr(rec.get("workerName"));
                    if (isBlank(workerName)) { workerName = toStr(rec.get("name")); }
                    String attendTime = resolveAttendTime(rec.get("verifyTime"), rec.get("checkDate"), rec.get("attTime"));
                    boolean labor = !isBlank(toStr(rec.get("team")));
                    addAttendanceWithDedup(byEngIdDedup, actualEngId, resolved, workerName, attendTime, labor, m);
                }
            }
        }

        // 将去重结构转为最终载荷
        Map<String, List<Map<String, Object>>> engIdToPayload = new LinkedHashMap<>();
        for (Map.Entry<String, LinkedHashMap<String, PayloadWithMeta>> e : byEngIdDedup.entrySet()) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (PayloadWithMeta pwm : e.getValue().values()) { list.add(pwm.payload); }
            engIdToPayload.put(e.getKey(), list);
        }

        // 分批、分 engId 推送
        String method = props.getD6cApi().getMethod().getAttendance();
        int batch = Math.max(0, props.getAttendance().getMaxPostBatchSize());

        int pushed = 0;
		for (Map.Entry<String, List<Map<String, Object>>> e : engIdToPayload.entrySet()) {
            String targetEngId = e.getKey();
            List<Map<String, Object>> payload = e.getValue();
			// 二次防护：开启 dropUnknownEngId 时，未注册的 engId 直接跳过，不使用全局 D6C 凭证回退
			if (props.getAttendance().isDropUnknownEngId()) {
				Optional<ProjectWorkerIntegrationProperties.Project> known = projectRegistry.snapshotByEngId(targetEngId);
				if (!known.isPresent()) {
					log.info("跳过未注册 engId 的考勤推送（dropUnknownEngId=true）：engId={}, size={}", targetEngId, payload == null ? 0 : payload.size());
					continue;
				}
			}
            Map<String, String> envelope = generateCommonAttributes(targetEngId, null);
            if (batch > 0 && payload.size() > batch) {
                for (int i = 0; i < payload.size(); i += batch) {
                    int j = Math.min(i + batch, payload.size());
                    throttleD6c();
                    d6cApiService.post(method, payload.subList(i, j), envelope);
                }
            } else {
                throttleD6c();
                d6cApiService.post(method, payload, envelope);
            }
            pushed += payload.size();
        }

        SyncResult r = new SyncResult();
        r.setFetched(totalFetched);
        r.setPushed(pushed);
        return r;
    }

    /**
     * 通过人员信息接口，结合姓名与脱敏匹配规则，解析真实身份证。
     * 命中唯一人时：写入强映射与候选集合；多候选时：仅写入候选集合；无命中返回 null。
     */
    private String tryResolveByPersonApi(String maskedIdCard, String name, String lookupEngId, String effectiveCode,
                                         Map<String, List<JSONObject>> personCacheByEngId) {
        try {
            if (isBlank(maskedIdCard) || isBlank(name) || isBlank(lookupEngId)) { return null; }
            List<JSONObject> persons = personCacheByEngId.get(lookupEngId);
            if (persons == null) {
                // 按记录实际 engId 拉取人员清单
                JSONArray arr = fetchPersons(lookupEngId, effectiveCode);
                if (arr == null) { arr = new JSONArray(); }
                persons = new ArrayList<>();
                for (int i = 0; i < arr.size(); i++) { persons.add(arr.getJSONObject(i)); }
                personCacheByEngId.put(lookupEngId, persons);
            }

            List<String> candidateIds = new ArrayList<>();
            String normName = nvl(name).trim();
            for (JSONObject p : persons) {
                String realId = toStr(p.get("idCardNumber"));
                String personName = toStr(p.get("name"));
                if (isBlank(personName)) { personName = toStr(p.get("workerName")); }
                if (isBlank(realId) || isBlank(personName)) { continue; }
                if (!normName.equals(personName.trim())) { continue; }
                if (maskedMatchesReal(maskedIdCard, realId)) {
                    candidateIds.add(realId);
                }
            }

            int ttlDays = Math.max(1, props.getAttendance().getCache().getResolveTtlDays());
            int candHours = Math.max(1, props.getAttendance().getCache().getCandidateTtlHours());

            if (candidateIds.size() == 1) {
                String real = candidateIds.get(0);
                identityResolver.putResolved(maskedIdCard, name, lookupEngId, real, ttlDays);
                identityResolver.addCandidate(maskedIdCard, name, lookupEngId, real, candHours);
                return real;
            }
            if (!candidateIds.isEmpty()) {
                for (String real : candidateIds) {
                    identityResolver.addCandidate(maskedIdCard, name, lookupEngId, real, candHours);
                }
            }
        } catch (Exception e) {
            log.warn("tryResolveByPersonApi error: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 人员清单拉取（姓名+身份证明文）。仅用于脱敏身份证解析的回退场景。
     */
    private JSONArray fetchPersons(String engId, String code) {
        String base = props.getYichangApi().getBase();
        String path = props.getYichangApi().getPersonPath();
        String url = base + path + "?engId=" + urlEncode(engId) + "&code=" + urlEncode(nvl(code));

        OpenApiService.ApiRequest req = new OpenApiService.ApiRequest();
        req.setUrl(url);
        req.setMethod("GET");
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        req.setHeaders(headers);

        JSONObject resp = executeAWithRetry(req);
        if (resp == null) { return null; }
        Object arr = resp.get("records");
        if (arr instanceof JSONArray) { return (JSONArray) arr; }
        return new JSONArray();
    }

    /**
     * 判断明文身份证是否与脱敏串匹配（支持常见的“前若干位/后4位保留，中间*”样式）。
     */
    private static boolean maskedMatchesReal(String masked, String real) {
        if (isBlank(masked) || isBlank(real)) { return false; }
        String m = masked.trim();
        String r = real.trim();
        if (m.equals(r)) { return true; }
        if (r.length() < 8 || m.length() < 8) { return false; }
        String rPrefix4 = r.substring(0, 4);
        String rSuffix4 = r.substring(r.length() - 4);
        String mPrefix4 = m.substring(0, 4);
        String mSuffix4 = m.substring(m.length() - 4);
        if (!rPrefix4.equals(mPrefix4)) { return false; }
        if (!rSuffix4.equals(mSuffix4)) { return false; }
        // 中间段允许为 '*'/'＊' 或与真实一致
        int mStart = 4;
        int rStart = 4;
        int mEnd = m.length() - 4;
        int rEnd = r.length() - 4;
        int midLen = Math.min(mEnd - mStart, rEnd - rStart);
        for (int i = 0; i < midLen; i++) {
            char cm = m.charAt(mStart + i);
            char cr = r.charAt(rStart + i);
            if (cm == '*' || cm == '＊') { continue; }
            if (cm != cr) { return false; }
        }
        return true;
    }

    private static boolean looksMasked(String v) {
        if (v == null) { return false; }
        String s = v.trim();
        if (s.isEmpty()) { return false; }
        // 常见脱敏包含 '*'
        if (s.indexOf('*') >= 0 || s.indexOf('＊') >= 0) { return true; }
        return false;
    }

    private static boolean isValidChineseIdCardFormat(String id) {
        if (isBlank(id)) { return false; }
        String s = id.trim();
        // 兼容15位或18位，18位末位可为X/x；参考前端校验规则
        return s.matches("^\\d{6}(18|19|20)?\\d{2}(0[1-9]|1[012])(0[1-9]|[12]\\d|3[01])\\d{3}(\\d|[xX])$")
                || s.matches("^\\d{15}$");
    }

    /**
     * 调用宜昌市平台考勤接口
     * 示例：/laboratt/attendance/page/?page=1&limit=100&idCardNumber=...&verifyTime=yyyy-MM-dd&orderByField=verifyTime&isAsc=false&code=...&engId=...
     */
    private JSONArray fetchAttendanceRecords(String engId, String code, String idCardNumber, String verifyDate) {
        String base = props.getYichangApi().getBase();
        String path = props.getYichangApi().getAttendancePath();
        String url = base + path + "?page=1&limit=100"
                + "&idCardNumber=" + urlEncode(idCardNumber)
                + "&verifyTime=" + urlEncode(verifyDate)
                + "&orderByField=verifyTime&isAsc=false"
                + (isBlank(code) ? "" : ("&code=" + urlEncode(code)))
                + "&engId=" + urlEncode(engId);

        OpenApiService.ApiRequest req = new OpenApiService.ApiRequest();
        req.setUrl(url);
        req.setMethod("GET");
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        req.setHeaders(headers);

        JSONObject resp = executeAWithRetry(req);
        if (resp == null) { return null; }
        Object arr = resp.get("records");
        if (arr instanceof JSONArray) {
            return (JSONArray) arr;
        }
        return new JSONArray();
    }

    /**
     * 索引分页拉取：不带身份证号
     */
    private JSONArray fetchAttendanceIndex(String engId, String code, String verifyDate, int page, int limit) {
        String base = props.getYichangApi().getBase();
        String path = props.getYichangApi().getAttendancePath();
        String url = base + path + "?page=" + page + "&limit=" + limit
                + "&verifyTime=" + urlEncode(verifyDate)
                + "&orderByField=verifyTime&isAsc=false"
                + (isBlank(code) ? "" : ("&code=" + urlEncode(code)))
                + "&engId=" + urlEncode(engId);

        OpenApiService.ApiRequest req = new OpenApiService.ApiRequest();
        req.setUrl(url);
        req.setMethod("GET");
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        req.setHeaders(headers);

        JSONObject resp = executeAWithRetry(req);
        if (resp == null) { return null; }
        Object arr = resp.get("records");
        if (arr instanceof JSONArray) {
            return (JSONArray) arr;
        }
        return new JSONArray();
    }

    /**
     * 字段映射：宜昌 -> D6C ProjectWorkerAttendance.Upload
     * 对应关系：
     * - externalId <- id
     * - idCardNumber <- record.idCardNumber | 入参 idCardNumber
     * - workerName <- workerName | name
     * - attendDirection <- inOrOut (in->1, out->0, 其它->"to_do")
     * - attendTime <- verifyTime|checkDate (epoch ms) 格式化 yyyy-MM-dd HH:mm:ss
     * - attendType <- 空（无对应字段）
     * - attendImageUrl <- 完整HTTP(imgUrl)
     * - attendDeviceCode <- deviceSn
     */
    private List<Map<String, Object>> mapToD6CAttendance(JSONArray records, String fallbackIdCardNumber) {
        List<Map<String, Object>> list = new ArrayList<>();
        String base = nvl(props.getYichangApi().getBase());
        for (int i = 0; i < records.size(); i++) {
            JSONObject p = records.getJSONObject(i);
            Map<String, Object> m = new LinkedHashMap<>();

            putIfNotBlank(m, "externalId", toStr(p.get("id")));

            String idCard = toStr(p.get("idCardNumber"));
            if (isBlank(idCard)) { idCard = fallbackIdCardNumber; }
            putIfNotBlank(m, "idCardNumber", idCard);

            String workerName = toStr(p.get("workerName"));
            if (isBlank(workerName)) { workerName = toStr(p.get("name")); }
            putIfNotBlank(m, "workerName", workerName);

            String inOrOut = toStr(p.get("inOrOut"));
            String attendDirection;
            if ("in".equalsIgnoreCase(inOrOut)) {
                attendDirection = "1";
            } else if ("out".equalsIgnoreCase(inOrOut)) {
                attendDirection = "0";
            } else {
                attendDirection = ""; // 其它或空均留空
            }
            putIfNotBlank(m, "attendDirection", attendDirection);

            String attendTime = resolveAttendTime(p.get("verifyTime"), p.get("checkDate"), p.get("attTime"));
            putIfNotBlank(m, "attendTime", attendTime);

            // attendType 暂无对应，留空

            String imgUrl = toStr(p.get("imgUrl"));
            if (!isBlank(imgUrl)) {
                if (!(imgUrl.startsWith("http://") || imgUrl.startsWith("https://"))) {
                    imgUrl = base + imgUrl;
                }
                putIfNotBlank(m, "attendImageUrl", imgUrl);
            }

            String deviceSn = toStr(p.get("deviceSn"));
            putIfNotBlank(m, "attendDeviceCode", deviceSn);

            list.add(m);
        }
        return list;
    }

    private Map<String, Object> mapSingleAttendanceToD6C(JSONObject p, String idCardNumber) {
        Map<String, Object> m = new LinkedHashMap<>();
        String base = nvl(props.getYichangApi().getBase());
        putIfNotBlank(m, "externalId", toStr(p.get("id")));
        putIfNotBlank(m, "idCardNumber", idCardNumber);
        String workerName = toStr(p.get("workerName"));
        if (isBlank(workerName)) { workerName = toStr(p.get("name")); }
        putIfNotBlank(m, "workerName", workerName);
        String inOrOut = toStr(p.get("inOrOut"));
        String attendDirection;
        if ("in".equalsIgnoreCase(inOrOut)) {
            attendDirection = "1";
        } else if ("out".equalsIgnoreCase(inOrOut)) {
            attendDirection = "0";
        } else {
            attendDirection = "";
        }
        putIfNotBlank(m, "attendDirection", attendDirection);
        String attendTime = resolveAttendTime(p.get("verifyTime"), p.get("checkDate"), p.get("attTime"));
        putIfNotBlank(m, "attendTime", attendTime);
        String imgUrl = toStr(p.get("imgUrl"));
        if (!isBlank(imgUrl)) {
            if (!(imgUrl.startsWith("http://") || imgUrl.startsWith("https://"))) {
                imgUrl = base + imgUrl;
            }
            putIfNotBlank(m, "attendImageUrl", imgUrl);
        }

        String deviceSn = toStr(p.get("deviceSn"));
        putIfNotBlank(m, "attendDeviceCode", deviceSn);
        //001  ⼈脸识别
        putIfNotBlank(m, "attendType", "001");
        return m;
    }

    private void safeWriteBackFromPrecise(String engId, String realIdCardNumber, JSONArray records) {
        if (records == null || records.isEmpty()) { return; }
        int ttlDays = Math.max(1, props.getAttendance().getCache().getResolveTtlDays());
        int candHours = Math.max(1, props.getAttendance().getCache().getCandidateTtlHours());
        for (int i = 0; i < records.size(); i++) {
            try {
                JSONObject p = records.getJSONObject(i);
                // 优先使用脱敏字段 idCardShow；缺失时，若 idCardNumber 看起来是脱敏串且与真实证件匹配，再作为回退
                String masked = toStr(p.get("idCardShow"));
                if (isBlank(masked)) {
                    String maybeMasked = toStr(p.get("idCardNumber"));
                    if (!isBlank(maybeMasked)
                            && !maybeMasked.equals(realIdCardNumber)
                            && maskedMatchesReal(maybeMasked, realIdCardNumber)) {
                        masked = maybeMasked;
                    }
                }
                String name = toStr(p.get("workerName"));
                if (isBlank(name)) { name = toStr(p.get("name")); }
                // 仅当 masked 明确为脱敏串且可与真实证件匹配时才回写缓存
                if (!isBlank(masked) && !masked.equals(realIdCardNumber) && maskedMatchesReal(masked, realIdCardNumber)) {
                    identityResolver.putResolved(masked, name, engId, realIdCardNumber, ttlDays);
                    identityResolver.addCandidate(masked, name, engId, realIdCardNumber, candHours);
                }
            } catch (Exception ignore) {}
        }
    }

    private JSONObject executeAWithRetry(OpenApiService.ApiRequest req) {
        int attempts = Math.max(1, props.getAttendance().getRetryMaxAttempts());
        int backoff = Math.max(0, props.getAttendance().getRetryBackoffMs());
        for (int i = 0; i < attempts; i++) {
            throttleA();
            JSONObject resp = openApiService.executeApiRequest(req);
            if (resp != null) { return resp; }
            if (i + 1 < attempts && backoff > 0) {
                try { Thread.sleep(backoff); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
            }
        }
        return null;
    }

    private void throttleA() {
        int qps = props.getAttendance().getQpsLimitYichang();
        if (qps <= 0) { return; }
        long minInterval = Math.max(1, 1000L / qps);
        throttleCommon(lastAApiMs, minInterval);
    }

    private void throttleD6c() {
        int qps = props.getAttendance().getQpsLimitD6c();
        if (qps <= 0) { return; }
        long minInterval = Math.max(1, 1000L / qps);
        throttleCommon(lastD6cMs, minInterval);
    }

    private void throttleCommon(AtomicLong lastRef, long minIntervalMs) {
        while (true) {
            long now = System.currentTimeMillis();
            long last = lastRef.get();
            long elapsed = now - last;
            if (elapsed >= minIntervalMs) {
                if (lastRef.compareAndSet(last, now)) { return; }
                else { continue; }
            }
            long sleep = minIntervalMs - elapsed;
            try { Thread.sleep(Math.min(sleep, 50)); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); return; }
        }
    }

    private String resolveAttendTime(Object verifyTimeObj, Object checkDateObj, Object attTimeObj) {
        String fromMs = epochMsToString(verifyTimeObj);
        if (!isBlank(fromMs)) { return fromMs; }
        String fromMs2 = epochMsToString(checkDateObj);
        if (!isBlank(fromMs2)) { return fromMs2; }
        String s = toStr(attTimeObj);
        if (!isBlank(s)) { return s; }
        return null;
    }

    private String epochMsToString(Object v) {
        if (v == null) { return null; }
        try {
            long ms;
            if (v instanceof Number) {
                ms = ((Number) v).longValue();
            } else {
                String s = String.valueOf(v).trim();
                if (s.isEmpty() || "null".equalsIgnoreCase(s)) { return null; }
                ms = Long.parseLong(s);
            }
            LocalDateTime dt = LocalDateTime.ofInstant(Instant.ofEpochMilli(ms), ZoneId.systemDefault());
            return dt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception ignore) {
            return null;
        }
    }

    /**
     * 生成公共属性（签名/时间戳/appKey）
     * 逻辑与 ProjectWorkerSyncService 保持一致
     */
    private Map<String, String> generateCommonAttributes(String engId, ProjectWorkerIntegrationProperties.Project override) {
        String appKey = null;
        String appSecret = null;
        String supplier = props.getD6cApi().getSupplier();

        if (override != null) {
            if (!isBlank(override.getAppkey())) { appKey = nvl(override.getAppkey()); }
            if (!isBlank(override.getAppSecret())) { appSecret = nvl(override.getAppSecret()); }
        }

        if (isBlank(appKey) || isBlank(appSecret)) {
            Optional<ProjectWorkerIntegrationProperties.Project> opt = projectRegistry.snapshotByEngId(engId);
            if (opt.isPresent()) {
                ProjectWorkerIntegrationProperties.Project p = opt.get();
                if (isBlank(appKey)) { appKey = nvl(p.getAppkey()); }
                if (isBlank(appSecret)) { appSecret = nvl(p.getAppSecret()); }
            }
        }

        if (isBlank(appKey)) appKey = nvl(props.getD6cApi().getAppKey());
        if (isBlank(appSecret)) appSecret = nvl(props.getD6cApi().getAppSecret());
        if (isBlank(appKey) || isBlank(appSecret)) {
            throw new IllegalStateException("D6C系统 appKey/appSecret 未配置（项目级或全局级）");
        }

        String ts = String.valueOf(System.currentTimeMillis());
        String toSign = (appSecret + appKey + ts + nvl(supplier) + appSecret).toLowerCase();
        String sign = sha256Hex(toSign);

        Map<String, String> m = new HashMap<>();
        m.put("appKey", appKey);
        m.put("timestamp", ts);
        m.put("supplier", nvl(supplier));
        m.put("sign", sign);
        return m;
    }
}


