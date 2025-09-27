package org.jeecg.dataingest.projectworker.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.projectworker.config.ProjectWorkerIntegrationProperties;
import org.jeecg.dataingest.projectworker.config.ProjectWorkerProjectRegistry;
import org.jeecg.dataingest.openapi.service.OpenApiService;
import org.jeecg.dataingest.openapi.service.OpenApiService.ApiRequest;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static org.jeecg.dataingest.projectworker.util.ProjectWorkerUtils.*;

/**
 * 工地项目人员信息同步服务
 * 从宜昌实名制平台系统获取数据，处理后推送到D6C系统
 * 基于原NiFi Processor逻辑实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectWorkerSyncService {

    private final OpenApiService openApiService;
    private final ProjectWorkerIntegrationProperties props;
    private final ProjectWorkerProjectRegistry projectRegistry;
    private final D6CApiService d6cApiService;

    @Data
    public static class SyncResult {
        private int fetched;
        private int team;
        private int person;
        private int personBasic;
        private int company;
    }

    /**
     * 同步工地项目人员信息
     * @param engId 工程ID
     * @param code 项目代码
     * @return 同步结果
     */
    public SyncResult sync(String engId, String code) {
        return sync(engId, code, null);
    }

    /**
     * 同步工地项目人员信息，支持传入参数级覆盖（appkey/appSecret/code 等）
     */
    public SyncResult sync(String engId, String code, ProjectWorkerIntegrationProperties.Project override) {
        // 计算有效 code：优先 override.code -> 入参 code -> 注册表
        String effectiveCode = null;
        if (override != null && !isBlank(override.getCode())) {
            effectiveCode = override.getCode();
        } else if (!isBlank(code)) {
            effectiveCode = code;
        } else {
            try {
                Optional<ProjectWorkerIntegrationProperties.Project> p = projectRegistry.findByEngId(engId);
                if (p.isPresent()) {
                    effectiveCode = nvl(p.get().getCode());
                }
            } catch (Exception ignore) {}
        }

        // 从宜昌实名制平台系统获取人员数据
        JSONArray persons = fetchPersons(engId, effectiveCode);
        if (persons == null) {
            throw new IllegalStateException("宜昌实名制平台系统接口无数据或请求失败");
        }

        // 处理得到团队和人员记录
        List<Map<String, Object>> teamList = processTeamData(persons);
        List<Map<String, Object>> personTeamList = processPersonTeamData(persons);
        List<Map<String, Object>> personBasicList = processPersonBasicData(persons);
        List<Map<String, Object>> companyList = processCompanyData(persons);

        // 生成公共属性（签名等）
        Map<String, String> envelopeCommon = generateCommonAttributes(engId, override);

        // 推送到D6C系统
        postToD6C(props.getD6cApi().getMethod().getCompanyBasic(), companyList, envelopeCommon);
        postToD6C(props.getD6cApi().getMethod().getCompany(), companyList, envelopeCommon);
        postToD6C(props.getD6cApi().getMethod().getTeam(), teamList, envelopeCommon);
        postToD6C(props.getD6cApi().getMethod().getPersonBasic(), personBasicList, envelopeCommon);
        postToD6C(props.getD6cApi().getMethod().getPerson(), personTeamList, envelopeCommon);
        
        

        SyncResult r = new SyncResult();
        r.setFetched(persons.size());
        r.setTeam(teamList.size());
        r.setPerson(personTeamList.size());
        r.setPersonBasic(personBasicList.size());
        r.setCompany(companyList.size());
        
        log.info("ProjectWorkerSyncService 生成 team数组={} 条, person数组={} 条, personInfo数组={} 条, company数组={} 条", 
                new Object[]{teamList.size(), personTeamList.size(), personBasicList.size(), companyList.size()});
        
        return r;
    }

    /**
     * 从宜昌实名制平台系统获取人员数据
     */
    private JSONArray fetchPersons(String engId, String code) {
        String url = props.getYichangApi().getBase() + props.getYichangApi().getPersonPath() + "?engId="
                + engId + "&code=" + code;
        ApiRequest req = new ApiRequest();
        req.setUrl(url);
        req.setMethod("GET");
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        //headers.put("User-Agent", "JeecgBoot-DataIngest/1.0");
        req.setHeaders(headers);
        return openApiService.executeApiRequestForArray(req);
    }

    /**
     * 推送到D6C系统
     */
    private void postToD6C(String method, List<Map<String, Object>> data, Map<String, String> common) {
        if (data.isEmpty()) { return; }
        d6cApiService.post(method, data, common);
    }

    /**
     * 生成公共属性（签名等）
     */
    private Map<String, String> generateCommonAttributes(String engId, ProjectWorkerIntegrationProperties.Project override) {
        String appKey = null;
        String appSecret = null;
        String supplier = props.getD6cApi().getSupplier();

        // 优先使用 override 的 appkey/appSecret
        if (override != null) {
            if (!isBlank(override.getAppkey())) { appKey = nvl(override.getAppkey()); }
            if (!isBlank(override.getAppSecret())) { appSecret = nvl(override.getAppSecret()); }
        }

        // 其次取注册表中项目级的 appkey/appSecret（若 override 未提供）
        if (isBlank(appKey) || isBlank(appSecret)) {
            Optional<ProjectWorkerIntegrationProperties.Project> opt = projectRegistry.snapshotByEngId(engId);
            if (opt.isPresent()) {
                ProjectWorkerIntegrationProperties.Project p = opt.get();
                if (isBlank(appKey)) { appKey = nvl(p.getAppkey()); }
                if (isBlank(appSecret)) { appSecret = nvl(p.getAppSecret()); }
            }
        }
        // 兜底全局配置
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

    /**
     * 处理团队数据
     */
    private List<Map<String, Object>> processTeamData(JSONArray persons) {
        Map<String, List<JSONObject>> teamMap = new LinkedHashMap<>();
        for (int i = 0; i < persons.size(); i++) {
            JSONObject p = persons.getJSONObject(i);
            String teamName = toStr(p.get("team"));
            if( isBlank(teamName) ){
                teamName = props.getDefaults().getDefaultTeamName();
            }

            teamMap.computeIfAbsent(teamName, k -> new ArrayList<>()).add(p);
        }
        
        List<Map<String, Object>> list = new ArrayList<>();
        for (Map.Entry<String, List<JSONObject>> e : teamMap.entrySet()) {
            String teamName = e.getKey();
            List<JSONObject> mem = e.getValue();
            if (mem.isEmpty()) continue;
            JSONObject first = mem.get(0);

            // 查找班组长
            String leaderName = null;
            String cardNo = null;
            for (JSONObject m : mem) {
                if ("是".equals(toStr(m.get("isHeadMan")))) {
                    leaderName = toStr(m.get("name"));
                    cardNo = toStr(m.get("idCardNumber"));
                    break;
                }
            }

            String engId = toStr(first.get("engId"));
            String organName = toStr(first.get("organName"));
            String workType = toStr(first.get("worktype"));
            String teamCode = String.valueOf(teamName.hashCode()) + "_" + nvl(engId);

            Map<String, Object> m = new LinkedHashMap<>();
            //putIfNotBlank(m, "externalId", toStr(first.get("id")));
            m.put("teamCode", teamCode);
            m.put("teamName", teamName);
            
            // 使用完整的公司信息查找逻辑
            ProjectWorkerProjectRegistry.CompanyInfo info = ProjectWorkerProjectRegistry.lookupCompanyInfo(organName);
            String corpCode = info == null ? "" : info.creditCode;
            String corpType = info == null ? "" : info.corpType;
            putIfNotBlank(m, "corpCode", corpCode);
            putIfNotBlank(m, "corpName", organName);
            putIfNotBlank(m, "corpType", corpType);
            m.put("teamType", isBlank(teamName) || props.getDefaults().getDefaultTeamName().equals(teamName) ? "2" : "1");
            putIfNotBlank(m, "workType", ProjectWorkerProjectRegistry.getWorkTypeForToD6C(workType));
            putIfNotBlank(m, "leaderName", leaderName);
            putIfNotBlank(m, "cardNo", cardNo);
            m.put("amount", mem.size());
            m.put("isPass", 1);
            m.put("isBlack", 0);
            list.add(m);
        }
        return list;
    }

    /**
     * 处理人员数据
     */
    private List<Map<String, Object>> processPersonTeamData(JSONArray persons) {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < persons.size(); i++) {
            JSONObject p = persons.getJSONObject(i);
            String teamName = toStr(p.get("team"));
            if( isBlank(teamName) ){
                teamName = props.getDefaults().getDefaultTeamName();
            }
            String engId = toStr(p.get("engId"));
            String organName = toStr(p.get("organName"));
            String isHeadMan = toStr(p.get("isHeadMan"));
            String teamCode = (teamName != null ? String.valueOf(teamName.hashCode()) : "") + "_" + nvl(engId);
            
            Map<String, Object> m = new LinkedHashMap<>();
            //put(m, "externalId", toStr(p.get("id")));
            m.put("status", 1);
            put(m, "idCardNumber", toStr(p.get("idCardNumber")));
            put(m, "workerName", toStr(p.get("name")));
            m.put("teamCode", teamCode);
            put(m, "teamName", teamName);
            
            // 使用完整的公司信息查找逻辑
            ProjectWorkerProjectRegistry.CompanyInfo info = ProjectWorkerProjectRegistry.lookupCompanyInfo(organName);
            String corpCode = info == null ? "" : info.creditCode;
            String corpType = info == null ? "" : info.corpType;
            put(m, "corpCode", corpCode);
            put(m, "corpName", organName);
            put(m, "corpType", corpType);
            put(m, "workType", ProjectWorkerProjectRegistry.getWorkTypeForToD6C(toStr(p.get("worktype"))));
            m.put("measureWay", "1");
            m.put("measureUnit", "02");
            m.put("unitPrice", 300.0);
            m.put("isTeamLeader", "是".equals(isHeadMan) ? 1 : 0);
            m.put("entryExitTime", nowFormatted());
            list.add(m);
        }
        return list;
    }

    /**
     * 处理人员基本信息数据（唯一键：身份证号）
     */
    private List<Map<String, Object>> processPersonBasicData(JSONArray persons) {
        LinkedHashMap<String, Map<String, Object>> idToBasic = new LinkedHashMap<>();
        for (int i = 0; i < persons.size(); i++) {
            JSONObject p = persons.getJSONObject(i);
            String idCardNumber = toStr(p.get("idCardNumber"));
            if (isBlank(idCardNumber)) {
                // 唯一键缺失，跳过
                continue;
            }

            // 新增字段及别名回退
            String externalId = toStr(p.get("externalId"));
            if (isBlank(externalId)) {
                externalId = toStr(p.get("id"));
            }
            String workerName = toStr(p.get("workerName"));
            if (isBlank(workerName)) {
                workerName = toStr(p.get("name"));
            }
            String workerCode = toStr(p.get("workerCode"));
            String cellPhone = toStr(p.get("cellPhone"));
            if (isBlank(cellPhone)) {
                String altPhone = toStr(p.get("phone"));
                cellPhone = isBlank(altPhone) ? toStr(p.get("mobile")) : altPhone;
                if (isBlank(cellPhone)) {
                    cellPhone = toStr(p.get("mobilePhone"));
                }
                if (isBlank(cellPhone)) {
                    cellPhone = props.getDefaults().getDefaultCellPhone();
                }
            }

            // 校验必填字段（含你列出的新增必填）
            String address = toStringOrDefault(p, "address", props.getDefaults().getDefaultAddress());
            String birthPlace = toStringOrDefault(p, "birthPlace", props.getDefaults().getDefaultbirthPlace());
            // 带默认值示例：nation 默认 "02"
            String nation = toStringOrDefault(p, "nation", "02");
            String grantOrg = toStringOrDefault(p, "grantOrg", props.getDefaults().getDefaultGrantOrg());
            String cardStartDate = toStringOrDefault(p, "cardStartDate", "");
            String cardExpiryDate = toStringOrDefault(p, "cardExpiryDate", "");
            // 头像策略：默认使用配置的Base64；当开启A系统照片补充时尝试抓取
            String faceImage = props.getDefaults().getDefaultBase64Image();
            String headImage = props.getDefaults().getDefaultBase64Image();
            if (props.getYichangApi().isEnablePhotoEnrich()) {
                try {
                    Map<String, String> photo = tryFetchPersonPhoto(toStr(p.get("engId")), toStr(p.get("id")));
                    if (photo != null) {
                        String face = photo.get("faceImage");
                        String head = photo.get("headImage");
                        if (!isBlank(face)) faceImage = face;
                        if (!isBlank(head)) headImage = head;
                    }
                } catch (Exception ex) {
                    log.warn("补充照片失败 idCard={} err={}", idCardNumber, ex.getMessage());
                }
            }
            String politicsType = toStringOrDefault(p, "politicsType", "");
            String cultureLevelType = toStringOrDefault(p, "cultureLevelType", "");

            // 使用有序Map，严格保证字段输出顺序（仅传非空字段）
            Map<String, Object> basic = new LinkedHashMap<>();
            // 顺序：externalId, idCardNumber, workerName, workerCode, cellPhone
            //put(basic, "externalId", externalId);
            basic.put("idCardNumber", idCardNumber);
            basic.put("workerName", workerName);
            put(basic, "workerCode", workerCode);
            basic.put("cellPhone", cellPhone);
            // 其余字段按文档列出顺序，仅在非空时包含
            put(basic, "address", address);
            put(basic, "birthPlace", birthPlace);
            put(basic, "nation", nation);
            put(basic, "grantOrg", grantOrg);
            put(basic, "cardStartDate", cardStartDate);
            put(basic, "cardExpiryDate", cardExpiryDate);
            // 人像、头像强制输出固定Base64
            basic.put("faceImage", faceImage);
            basic.put("headImage", headImage);
            put(basic, "positiveIdCardImage", toStr(p.get("positiveIdCardImage")));
            put(basic, "negativeIdCardImage", toStr(p.get("negativeIdCardImage")));
            put(basic, "politicsType", politicsType);
            put(basic, "cultureLevelType", cultureLevelType);
            put(basic, "bloodType", toStr(p.get("bloodType")));
            put(basic, "urgentLinkMan", toStr(p.get("urgentLinkMan")));
            put(basic, "urgentLinkManPhone", toStr(p.get("urgentLinkManPhone")));

            Integer isEntryUnionVal = toIntegerOrNull(p.get("isEntryUnion"));
            if (isEntryUnionVal != null) { basic.put("isEntryUnion", isEntryUnionVal); }
            put(basic, "entryUnionTime", toStr(p.get("entryUnionTime")));
            Integer isHaveLaborCertificateVal = toIntegerOrNull(p.get("isHaveLaborCertificate"));
            if (isHaveLaborCertificateVal != null) { basic.put("isHaveLaborCertificate", isHaveLaborCertificateVal); }
            Integer isHaveMajorMedicalHistoryVal = toIntegerOrNull(p.get("isHaveMajorMedicalHistory"));
            if (isHaveMajorMedicalHistoryVal != null) { basic.put("isHaveMajorMedicalHistory", isHaveMajorMedicalHistoryVal); }
            put(basic, "cardNo", toStr(p.get("cardNo")));
            put(basic, "maritalStatus", toStr(p.get("maritalStatus")));
            put(basic, "healthCode", toStr(p.get("healthCode")));
            Integer isBlackVal = toIntegerOrNull(p.get("isBlack"));
            if (isBlackVal != null) { basic.put("isBlack", isBlackVal); }
            put(basic, "bankCode", toStr(p.get("bankCode")));
            put(basic, "bankName", toStr(p.get("bankName")));
            put(basic, "bankCardNo", toStr(p.get("bankCardNo")));

            Object extend = p.get("extendObject");
            if (extend instanceof Map && !((Map<?, ?>) extend).isEmpty()) {
                basic.put("extendObject", extend);
            }

            // 按身份证去重，后出现的覆盖先前记录
            idToBasic.put(idCardNumber, basic);
        }
        return new ArrayList<>(idToBasic.values());
    }

    /**
     * 可选：从A系统补充人像与证件头像
     * 需要在配置中提供 photoPath，例如：/labor/externalCall/person/photo
     * 假设返回JSON包含 faceImage/headImage Base64 字段
     */
    private Map<String, String> tryFetchPersonPhoto(String engId, String personId) {
        String photoPath = props.getYichangApi().getPhotoPath();
        if (isBlank(photoPath)) { return null; }
        String base = props.getYichangApi().getBase();
        String url = base + photoPath + "?engId=" + urlEncode(engId) + "&id=" + urlEncode(personId);
        OpenApiService.ApiRequest req = new OpenApiService.ApiRequest();
        req.setUrl(url);
        req.setMethod("GET");
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("User-Agent", "JeecgBoot-DataIngest/1.0");
        req.setHeaders(headers);
        com.alibaba.fastjson.JSONObject resp = openApiService.executeApiRequest(req);
        if (resp == null) { return null; }
        Map<String, String> m = new HashMap<>();
        String face = toStr(resp.get("faceImage"));
        String head = toStr(resp.get("headImage"));
        if (!isBlank(face)) m.put("faceImage", face);
        if (!isBlank(head)) m.put("headImage", head);
        return m.isEmpty() ? null : m;
    }

    /**
     * 处理企业数据（去重按 organId；organId 缺失时回退 organName 规范化）
     */
    private List<Map<String, Object>> processCompanyData(JSONArray persons) {
        LinkedHashMap<String, Map<String, Object>> keyToCompany = new LinkedHashMap<>();
        for (int i = 0; i < persons.size(); i++) {
            JSONObject p = persons.getJSONObject(i);
            String organName = toStr(p.get("organName"));
            String organId = toStr(p.get("organId"));
            if (isBlank(organName) && isBlank(organId)) {
                continue;
            }
            
            // 使用完整的公司信息查找逻辑
            ProjectWorkerProjectRegistry.CompanyInfo info = ProjectWorkerProjectRegistry.lookupCompanyInfo(organName);
            String corpCode = info == null ? "" : info.creditCode;
            String corpType = info == null ? "" : info.corpType;

            // 组装记录
            Map<String, Object> company = new LinkedHashMap<>();
            // externalId: 使用上游原始 id/companyId，如无则回退 organId
            String externalId = toStr(p.get("companyExternalId"));
            if (isBlank(externalId)) {
                externalId = toStr(p.get("corpExternalId"));
            }
            //put(company, "externalId", externalId);
            put(company, "corpCode", corpCode);
            put(company, "corpName", organName);
            put(company, "corpType", corpType);
            // 上下级、发包方等可从人员侧透传，如无则为空
            put(company, "supCorpCode", toStr(p.get("supCorpCode")));
            put(company, "supCorpName", toStr(p.get("supCorpName")));
            put(company, "contractCorpCode", toStr(p.get("contractCorpCode")));
            put(company, "contractCorpName", toStr(p.get("contractCorpName")));
            put(company, "contractCode", toStr(p.get("contractCode")));
            Integer statusVal = toIntegerOrNull(p.get("corpStatus"));
            if (statusVal != null) { company.put("status", statusVal); }
            put(company, "entryTime", toStr(p.get("corpEntryTime")));
            put(company, "exitTime", toStr(p.get("corpExitTime")));

            // 去重：优先 organId；其缺失时回退 organName 规范化
            String key = !isBlank(organId) ? ("ORG:" + organId) : ("NAME:" + normalizeCompanyNameForKey(organName));
            keyToCompany.put(key, company);
        }
        return new ArrayList<>(keyToCompany.values());
    }

    private static String normalizeCompanyNameForKey(String name) {
        if (isBlank(name)) { return ""; }
        return name.toLowerCase()
                .replace("（", "(")
                .replace("）", ")")
                .replaceAll("\\s+", "");
    }


    // ==== 工具方法 ====
    private static String nowFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    private static String urlEncode(String s) { 
        return s == null ? "" : URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
    
    private static boolean isBlank(String s) { 
        return s == null || s.trim().isEmpty(); 
    }
    
    private static String nvl(String s) { 
        return s == null ? "" : s; 
    }
    
    private static String toStr(Object v) { 
        return v == null ? null : String.valueOf(v); 
    }
    
    private static void put(Map<String, Object> m, String k, String v) { 
        if (!isBlank(v)) m.put(k, v); 
    }
    
    private static Integer toIntegerOrNull(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        String s = String.valueOf(value).trim();
        if (s.isEmpty() || "null".equalsIgnoreCase(s)) {
            return null;
        }
        try {
            return Integer.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }
    
    private static String toStringOrDefault(JSONObject obj, String key, String defaultValue) {
        if (obj == null) { return defaultValue; }
        Object val = obj.get(key);
        String str = toStr(val);
        return isBlank(str) ? defaultValue : str;
    }
    
    private static String sha256Hex(String s) { 
        try { 
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(s.getBytes(StandardCharsets.UTF_8)); 
            StringBuilder sb = new StringBuilder(); 
            for (byte x : b) { 
                sb.append(String.format("%02x", x)); 
            } 
            return sb.toString(); 
        } catch (Exception e) { 
            throw new RuntimeException(e);
        } 
    }

    private static String JSONString(Object obj) {
        return com.alibaba.fastjson.JSON.toJSONString(obj);
    }
}
