package org.jeecg.dataingest.projectworker.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 工地项目人员信息项目注册表
 * 包含完整的公司信息映射数据与工种映射规则
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ProjectWorkerProjectRegistry {

    private final ProjectWorkerIntegrationProperties props;
    private final ResourceLoader resourceLoader;

    private final Map<String, ProjectWorkerIntegrationProperties.Project> byEngId = new ConcurrentHashMap<>();

    private final ReadWriteLock registryLock = new ReentrantReadWriteLock();

    private ConfigService nacosConfigService;

    @Value("${spring.cloud.nacos.config.server-addr:}")
    private String nacosServerAddr;
    @Value("${spring.cloud.nacos.config.namespace:}")
    private String nacosNamespace;
    @Value("${spring.cloud.nacos.config.username:}")
    private String nacosUsername;
    @Value("${spring.cloud.nacos.config.password:}")
    private String nacosPassword;
    
    /**
     * 公司信息：统一社会信用代码 + 类型
     */
    public static final class CompanyInfo {
        public final String creditCode;
        public final String corpType;
        CompanyInfo(String creditCode, String corpType) {
            this.creditCode = creditCode;
            this.corpType = corpType;
        }
    }

    /**
     * 公司信息映射表
     * key 使用规范化公司名（去空格、统一括号、去结尾"公司/有限公司/股份有限公司/集团有限公司"等）
     */
    public static final Map<String, CompanyInfo> COMPANY_MAP = new HashMap<>();
    
    /**
     * 别名映射：将脏数据/别名统一到规范公司名（键、值都使用 normalizeCompanyName 处理后）
     */
    public static final Map<String, String> COMPANY_ALIASES = new HashMap<>();

    static {
        // 初始化公司信息映射表
        // key 使用规范化公司名（去空格、统一括号、去结尾"公司/有限公司/股份有限公司/集团有限公司"等）
        COMPANY_MAP.put(normalizeCompanyName("湖北盛荣建设集团有限公司"), new CompanyInfo("91420500777586514R", "009"));
        COMPANY_MAP.put(normalizeCompanyName("湖北中业宏工程咨询有限公司"), new CompanyInfo("914205001791258143", "007"));
        COMPANY_MAP.put(normalizeCompanyName("中国建筑第七工程局有限公司"), new CompanyInfo("91410000169954619U", "009"));
        COMPANY_MAP.put(normalizeCompanyName("湖北广成建设工程有限公司"), new CompanyInfo("91420500309750336R", "006"));
        COMPANY_MAP.put(normalizeCompanyName("宜昌市点军区住房和城乡建设局"), new CompanyInfo("unknown", "006"));
        COMPANY_MAP.put(normalizeCompanyName("华中科技大学同济医学院附属协和医院宜昌医院"), new CompanyInfo("12420500MB1P92376B", "008"));
        COMPANY_MAP.put(normalizeCompanyName("宜昌鹏顺置业有限公司"), new CompanyInfo("91420500MA490LT044", "006"));
        COMPANY_MAP.put(normalizeCompanyName("湖北华雷工程项目管理有限公司"), new CompanyInfo("91420500714689698D", "007"));
        COMPANY_MAP.put(normalizeCompanyName("宜昌金猇置业有限公司"), new CompanyInfo("91420505MAD19KHK7P", "009"));

        // 别名：将"湖北盛荣建设有限公司"映射到"湖北盛荣建设集团有限公司"
        COMPANY_ALIASES.put(
                normalizeCompanyName("湖北盛荣建设有限公司"),
                normalizeCompanyName("湖北盛荣建设集团有限公司")
        );
    }

    /**
     * 公司名归一化：小写、去空白、统一括号、去常见后缀、去常见中性词片段
     */
    private static String normalizeCompanyName(String name) {
        if (isBlank(name)) { return null; }
        String s = name.toLowerCase()
                .replace("（", "(")
                .replace("）", ")")
                .replaceAll("\\s+", "");
        
        return s;
    }

    /**
     * 查找公司信息
     */
    public static CompanyInfo lookupCompanyInfo(String organName) {
        String norm = normalizeCompanyName(organName);
        if (norm == null) { return null; }
        // 别名归并：先查别名得到规范名
        String canonical = COMPANY_ALIASES.getOrDefault(norm, norm);
        return COMPANY_MAP.get(canonical);
    }

    // ==== 工种映射逻辑：集中在 Registry ====

    /** 非空但未命中规则时默认归入 "030 其他" */
    private static final String WORKTYPE_DEFAULT = "030";
    /** 当上游工种字段为空或视为空时，默认为 "908 管理人员" */
    private static final String WORKTYPE_WHEN_BLANK = "908";

    private static final class WorkTypeRule {
        final String code;
        final String[] keywords;
        WorkTypeRule(String code, String... keywords) {
            this.code = code;
            this.keywords = keywords;
        }
    }

    /** 规则顺序敏感：更具体的放前面 */
    private static final List<WorkTypeRule> WORKTYPE_RULES = new ArrayList<>();

    static {
        // —— 管理岗 ——
        WORKTYPE_RULES.add(new WorkTypeRule("909", "业主管理人员", "业主管理"));
        WORKTYPE_RULES.add(new WorkTypeRule("908", "管理人员"));
        WORKTYPE_RULES.add(new WorkTypeRule("910", "资料员"));
        WORKTYPE_RULES.add(new WorkTypeRule("911", "专职安全员"));
        WORKTYPE_RULES.add(new WorkTypeRule("913", "安全员"));
        WORKTYPE_RULES.add(new WorkTypeRule("912", "材料员"));
        WORKTYPE_RULES.add(new WorkTypeRule("914", "测量员"));
        WORKTYPE_RULES.add(new WorkTypeRule("915", "实验员"));
        WORKTYPE_RULES.add(new WorkTypeRule("916", "质检员", "质量员"));
        WORKTYPE_RULES.add(new WorkTypeRule("917", "预算员", "造价员"));
        WORKTYPE_RULES.add(new WorkTypeRule("918", "施工员"));
        WORKTYPE_RULES.add(new WorkTypeRule("919", "项目经理", "项目经理部"));
        WORKTYPE_RULES.add(new WorkTypeRule("920", "技术员"));
        WORKTYPE_RULES.add(new WorkTypeRule("921", "机械员"));
        WORKTYPE_RULES.add(new WorkTypeRule("922", "标准员"));

        // —— 作业工种 ——
        WORKTYPE_RULES.add(new WorkTypeRule("001", "电工", "弱电", "弱电工"));
        WORKTYPE_RULES.add(new WorkTypeRule("002", "钢筋工", "钢筋"));
        WORKTYPE_RULES.add(new WorkTypeRule("003", "混凝土工", "混凝土", "砼"));
        WORKTYPE_RULES.add(new WorkTypeRule("004", "模板工", "模板"));
        WORKTYPE_RULES.add(new WorkTypeRule("005", "通风工", "通风"));
        WORKTYPE_RULES.add(new WorkTypeRule("006", "安装钳工", "钳工"));
        WORKTYPE_RULES.add(new WorkTypeRule("007", "电气设备安装调试工", "电气设备安装调试", "电气安装调试"));
        WORKTYPE_RULES.add(new WorkTypeRule("008", "管道工", "管道", "管工"));
        WORKTYPE_RULES.add(new WorkTypeRule("009", "变电安装工", "变电安装", "变电站安装"));
        WORKTYPE_RULES.add(new WorkTypeRule("010", "司泵工", "司泵", "泵工"));
        WORKTYPE_RULES.add(new WorkTypeRule("011", "挖掘铲运和桩工机械司机", "挖掘机", "装载机", "推土机", "铲运", "桩工机械司机"));
        WORKTYPE_RULES.add(new WorkTypeRule("012", "装饰装修工", "装饰装修", "装修工", "装饰工"));
        WORKTYPE_RULES.add(new WorkTypeRule("013", "室内成套设施安装工", "室内成套设施安装"));
        WORKTYPE_RULES.add(new WorkTypeRule("014", "建筑门窗幕墙安装工", "门窗幕墙安装", "幕墙安装", "门窗安装"));
        WORKTYPE_RULES.add(new WorkTypeRule("015", "幕墙制作工", "幕墙制作"));
        WORKTYPE_RULES.add(new WorkTypeRule("016", "防水工", "防水"));
        WORKTYPE_RULES.add(new WorkTypeRule("017", "木工"));
        WORKTYPE_RULES.add(new WorkTypeRule("018", "石工"));
        WORKTYPE_RULES.add(new WorkTypeRule("019", "除尘工", "除尘"));
        WORKTYPE_RULES.add(new WorkTypeRule("020", "测量放线工", "测量放线"));
        WORKTYPE_RULES.add(new WorkTypeRule("021", "线路架设工", "线路架设"));
        WORKTYPE_RULES.add(new WorkTypeRule("022", "古建筑传统石工", "古建石工"));
        WORKTYPE_RULES.add(new WorkTypeRule("023", "古建筑传统瓦工", "古建瓦工"));
        WORKTYPE_RULES.add(new WorkTypeRule("024", "古建筑传统彩画工", "古建彩画"));
        WORKTYPE_RULES.add(new WorkTypeRule("025", "古建筑传统木工", "古建木工"));
        WORKTYPE_RULES.add(new WorkTypeRule("026", "古建筑传统油工", "古建油工"));
        WORKTYPE_RULES.add(new WorkTypeRule("027", "金属工", "金属"));
        WORKTYPE_RULES.add(new WorkTypeRule("028", "杂工", "杂工", "普工", "辅工"));
        WORKTYPE_RULES.add(new WorkTypeRule("029", "砌筑工", "砌筑", "瓦工", "建筑瓦工"));
        WORKTYPE_RULES.add(new WorkTypeRule("031", "绿化工", "绿化"));
        WORKTYPE_RULES.add(new WorkTypeRule("032", "桩工", "桩工"));
        WORKTYPE_RULES.add(new WorkTypeRule("033", "抹灰工", "抹灰"));
        WORKTYPE_RULES.add(new WorkTypeRule("034", "镶贴工", "镶贴"));
        WORKTYPE_RULES.add(new WorkTypeRule("035", "建筑外墙保温安装工", "外墙保温", "保温安装"));
        WORKTYPE_RULES.add(new WorkTypeRule("036", "灌浆工", "灌浆"));
        WORKTYPE_RULES.add(new WorkTypeRule("037", "构件装配工", "构件装配", "装配式"));
        WORKTYPE_RULES.add(new WorkTypeRule("038", "打胶工", "打胶"));
        WORKTYPE_RULES.add(new WorkTypeRule("039", "防腐保温工", "防腐", "保温工"));
        WORKTYPE_RULES.add(new WorkTypeRule("040", "园林植保工", "园林植保"));
        WORKTYPE_RULES.add(new WorkTypeRule("041", "电梯驾驶工", "电梯司机", "施工电梯司机", "电梯驾驶员"));
        WORKTYPE_RULES.add(new WorkTypeRule("042", "油漆工", "油漆"));
        WORKTYPE_RULES.add(new WorkTypeRule("043", "电梯驾驶"));
        WORKTYPE_RULES.add(new WorkTypeRule("044", "架子工", "架子"));
        WORKTYPE_RULES.add(new WorkTypeRule("045", "机械设备安装工", "机械设备安装"));
        WORKTYPE_RULES.add(new WorkTypeRule("046", "建筑起重工(升降梯)", "建筑起重工（升降梯）", "升降梯", "升降机", "施工升降机", "货梯"));
        WORKTYPE_RULES.add(new WorkTypeRule("047", "起重机司机", "塔吊司机", "塔吊", "起重机", "吊车司机"));
        WORKTYPE_RULES.add(new WorkTypeRule("048", "爆破工", "爆破"));
        WORKTYPE_RULES.add(new WorkTypeRule("049", "安装起重工", "起重安装工", "安装起重"));
        // 其他/消防归入 030
        WORKTYPE_RULES.add(new WorkTypeRule("030", "其他", "其它", "消防"));
        WORKTYPE_RULES.add(new WorkTypeRule("051", "桩机操作工", "桩机操作", "打桩机"));
        WORKTYPE_RULES.add(new WorkTypeRule("052", "起重信号工", "起重信号", "信号工", "司索", "起重指挥"));
        WORKTYPE_RULES.add(new WorkTypeRule("053", "建筑起重机械安装拆卸工", "安装拆卸工", "安拆工", "起重机械安装拆卸", "附着升降脚手架", "附着式升降脚手架", "脚手架安装拆卸", "爬架", "附墙式升降脚手架"));
        WORKTYPE_RULES.add(new WorkTypeRule("054", "电焊工", "焊工", "焊接", "氩弧焊"));

        // —— 后勤类 ——
        WORKTYPE_RULES.add(new WorkTypeRule("1001", "保安", "门卫", "门房", "门岗"));
        WORKTYPE_RULES.add(new WorkTypeRule("1002", "保洁人员", "保洁"));
        WORKTYPE_RULES.add(new WorkTypeRule("1003", "食堂人员", "食堂", "餐厅", "炊事"));
        WORKTYPE_RULES.add(new WorkTypeRule("1004", "宿舍其他人员", "宿舍", "宿管"));
    }

    /**
     * 归一化工种描述，便于关键词匹配
     */
    private static String normalizeWorkType(String s) {
        if (isBlank(s)) { return null; }
        String t = s.trim();
        String tLower = t.toLowerCase();
        if ("无".equals(t) || "空".equals(t)
                || tLower.contains("未填写") || tLower.contains("未填") || tLower.contains("为空")
                || tLower.contains("null") || tLower.contains("none") || tLower.contains("空值")) {
            return null;
        }
        return tLower
                .replace("（", "(")
                .replace("）", ")")
                .replaceAll("\\s+", "");
    }

    private static boolean containsAny(String haystack, String... needles) {
        if (isBlank(haystack) || needles == null) { return false; }
        for (String n : needles) {
            if (isBlank(n)) { continue; }
            if (haystack.contains(n.toLowerCase().replaceAll("\\s+", ""))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将上游工种文本映射为 D6C 工种代码
     */
    public static String getWorkTypeForToD6C(String workType) {
        String norm = normalizeWorkType(workType);
        if (norm == null) { return WORKTYPE_WHEN_BLANK; }
        for (WorkTypeRule rule : WORKTYPE_RULES) {
            if (containsAny(norm, rule.keywords)) {
                return rule.code;
            }
        }
        return WORKTYPE_DEFAULT;
    }

    @PostConstruct
    public void init() {
        // 1) Load registry: prefer Nacos when enabled, else load from local JSON; if both empty -> fail fast
        ProjectWorkerIntegrationProperties.Nacos nacos = props.getProjectConfig().getNacos();
        Map<String, ProjectWorkerIntegrationProperties.Project> loaded = Collections.emptyMap();
        boolean nacosEnabled = nacos != null && nacos.isEnabled();

        if (nacosEnabled) {
            loaded = loadFromNacosOnce(nacos);
            if (loaded.isEmpty()) {
                // fallback to local file only when Nacos has no data
                loaded = loadFromFileOnce();
            }
        } else {
            loaded = loadFromFileOnce();
        }

        if (loaded == null || loaded.isEmpty()) {
            throw new IllegalStateException("ProjectWorkerProjectRegistry init failed: no projects found from Nacos or local json");
        }
        applyNewProjects(loaded, nacosEnabled ? "init-preferred:nacos-or-file" : "init:file");

        // 2) Register Nacos hot update listener only if enabled
        if (nacosEnabled) {
            registerNacosHotUpdateListener(nacos);
        }

        log.info("ProjectWorkerProjectRegistry initialized, total items: {}", byEngId.size());

        // 3) merge company mapping from properties (defaults)
        try {
            Map<String, String> codeMap = props.getDefaults().getCorpCodeMapping();
            Map<String, String> typeMap = props.getDefaults().getCorpTypeMapping();
            if (codeMap != null || typeMap != null) {
                java.util.Set<String> keys = new java.util.HashSet<>();
                if (codeMap != null) keys.addAll(codeMap.keySet());
                if (typeMap != null) keys.addAll(typeMap.keySet());
                for (String name : keys) {
                    String norm = normalizeCompanyName(name);
                    if (norm == null) { continue; }
                    String code = codeMap == null ? null : codeMap.get(name);
                    String type = typeMap == null ? null : typeMap.get(name);
                    if (code == null || code.isBlank()) { code = "unknown"; }
                    if (type == null) { type = ""; }
                    COMPANY_MAP.put(norm, new CompanyInfo(code, type));
                }
            }
        } catch (Exception ignore) {
            // ignore
        }
        log.info("Company mapping initialized, total companies: {}, total aliases: {}",
                COMPANY_MAP.size(), COMPANY_ALIASES.size());

        // 4) merge work type rules from properties (user-configured rules take precedence at the front)
        try {
            java.util.List<ProjectWorkerIntegrationProperties.WorkTypeRule> extra = props.getDefaults().getWorkTypeRules();
            if (extra != null && !extra.isEmpty()) {
                for (int i = extra.size() - 1; i >= 0; i--) {
                    ProjectWorkerIntegrationProperties.WorkTypeRule r = extra.get(i);
                    if (r == null || r.getCode() == null || r.getCode().trim().isEmpty()
                            || r.getKeywords() == null || r.getKeywords().isEmpty()) {
                        continue;
                    }
                    WORKTYPE_RULES.add(0, new WorkTypeRule(r.getCode(), r.getKeywords().toArray(new String[0])));
                }
                log.info("WORKTYPE_RULES merged with {} configured rules, total now {}", extra.size(), WORKTYPE_RULES.size());
            }
        } catch (Exception e) {
            log.warn("Merge configured work type rules failed: {}", e.getMessage());
        }
    }

    public Optional<ProjectWorkerIntegrationProperties.Project> findByEngId(String engId) {
        registryLock.readLock().lock();
        try {
            return Optional.ofNullable(byEngId.get(engId));
        } finally {
            registryLock.readLock().unlock();
        }
    }

    /**
     * 返回指定 engId 的项目快照（独立副本），用于一次同步过程中的一致性读取
     */
    public Optional<ProjectWorkerIntegrationProperties.Project> snapshotByEngId(String engId) {
        registryLock.readLock().lock();
        try {
            ProjectWorkerIntegrationProperties.Project p = byEngId.get(engId);
            if (p == null) { return Optional.empty(); }
            ProjectWorkerIntegrationProperties.Project copy = new ProjectWorkerIntegrationProperties.Project();
            copy.setEngId(p.getEngId());
            copy.setCode(p.getCode());
            copy.setAppkey(p.getAppkey());
            copy.setAppSecret(p.getAppSecret());
            copy.setProjectName(p.getProjectName());
            return Optional.of(copy);
        } finally {
            registryLock.readLock().unlock();
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private Map<String, ProjectWorkerIntegrationProperties.Project> loadFromFileOnce() {
        String path = props.getProjectConfig().getJsonPath();
        if (isBlank(path)) { return Collections.emptyMap(); }
        try {
            Resource resource = resourceLoader.getResource(path);
            if (!resource.exists()) {
                log.warn("ProjectWorkerProjectRegistry jsonPath not found: {}", path);
                return Collections.emptyMap();
            }
            try (InputStream in = resource.getInputStream()) {
                String text = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                Map<String, ProjectWorkerIntegrationProperties.Project> loaded = parseProjects(text);
                return loaded == null ? Collections.emptyMap() : loaded;
            }
        } catch (Exception e) {
            log.error("Load project worker projects failed from {}", path, e);
            return Collections.emptyMap();
        }
    }

    private Map<String, ProjectWorkerIntegrationProperties.Project> parseProjects(String text) {
        try {
            JSONArray arr = JSON.parseArray(text);
            if (arr == null) { return Collections.emptyMap(); }
            Map<String, ProjectWorkerIntegrationProperties.Project> map = new HashMap<>();
            for (int i = 0; i < arr.size(); i++) {
                JSONObject o = arr.getJSONObject(i);
                ProjectWorkerIntegrationProperties.Project p = new ProjectWorkerIntegrationProperties.Project();
                p.setEngId(o.getString("engId"));
                p.setCode(o.getString("code"));
                p.setAppkey(o.getString("appkey"));
                p.setAppSecret(o.getString("appSecret"));
                p.setProjectName(o.getString("projectName"));
                if (p.getEngId() != null) {
                    map.put(p.getEngId(), p);
                }
            }
            return map;
        } catch (Exception e) {
            log.error("parseProjects error", e);
            return null;
        }
    }

    private void applyNewProjects(Map<String, ProjectWorkerIntegrationProperties.Project> newMap, String source) {
        if (newMap == null) { return; }
        registryLock.writeLock().lock();
        try {
            byEngId.clear();
            byEngId.putAll(newMap);
            log.info("ProjectWorkerProjectRegistry applied {} items from {}", byEngId.size(), source);
        } finally {
            registryLock.writeLock().unlock();
        }
    }

    private Map<String, ProjectWorkerIntegrationProperties.Project> loadFromNacosOnce(ProjectWorkerIntegrationProperties.Nacos nacos) {
        try {
            if (nacos == null || !nacos.isEnabled()) { return Collections.emptyMap(); }
            if (isBlank(nacosServerAddr)) {
                log.warn("spring.cloud.nacos.config.server-addr is empty, skip initial Nacos load");
                return Collections.emptyMap();
            }
            if (isBlank(nacos.getDataId())) {
                log.warn("ProjectWorkerProjectRegistry Nacos enabled but dataId empty, skip initial Nacos load");
                return Collections.emptyMap();
            }

            if (nacosConfigService == null) {
                Properties properties = new Properties();
                properties.setProperty("serverAddr", nacosServerAddr);
                if (!isBlank(nacosNamespace)) properties.setProperty("namespace", nacosNamespace);
                if (!isBlank(nacosUsername)) properties.setProperty("username", nacosUsername);
                if (!isBlank(nacosPassword)) properties.setProperty("password", nacosPassword);
                nacosConfigService = NacosFactory.createConfigService(properties);
            }

            String group = isBlank(nacos.getGroup()) ? "DEFAULT_GROUP" : nacos.getGroup();
            String content = nacosConfigService.getConfig(nacos.getDataId(), group, nacos.getTimeoutMs());
            if (!isBlank(content)) {
                Map<String, ProjectWorkerIntegrationProperties.Project> loaded = parseProjects(content);
                return loaded == null ? Collections.emptyMap() : loaded;
            } else {
                log.warn("Nacos config empty for dataId={}, group={}", nacos.getDataId(), group);
                return Collections.emptyMap();
            }
        } catch (NacosException e) {
            log.error("Initial Nacos load failed", e);
            return Collections.emptyMap();
        }
    }

    private void registerNacosHotUpdateListener(ProjectWorkerIntegrationProperties.Nacos nacos) {
        try {
            if (nacos == null || !nacos.isEnabled()) { return; }
            if (nacosConfigService == null) { return; }
            String dataId = nacos.getDataId();
            String group = isBlank(nacos.getGroup()) ? "DEFAULT_GROUP" : nacos.getGroup();
            if (isBlank(dataId)) { return; }

            nacosConfigService.addListener(dataId, group, new Listener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    try {
                        Map<String, ProjectWorkerIntegrationProperties.Project> loaded = parseProjects(configInfo);
                        if (loaded != null) {
                            applyNewProjects(loaded, "nacos:" + dataId);
                        }
                    } catch (Exception e) {
                        log.error("Nacos listener update failed", e);
                    }
                }
                @Override
                public Executor getExecutor() {
                    return null;
                }
            });
            log.info("ProjectWorkerProjectRegistry Nacos listener registered, dataId={}, group={}", dataId, group);
        } catch (Exception e) {
            log.error("Register Nacos listener failed", e);
        }
    }
}
