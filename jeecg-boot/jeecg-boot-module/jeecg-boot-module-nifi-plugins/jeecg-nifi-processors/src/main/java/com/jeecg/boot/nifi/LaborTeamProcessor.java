package com.jeecg.boot.nifi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.nifi.annotation.behavior.EventDriven;
import org.apache.nifi.annotation.behavior.InputRequirement;
import org.apache.nifi.annotation.behavior.InputRequirement.Requirement;
import org.apache.nifi.annotation.behavior.SideEffectFree;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.expression.ExpressionLanguageScope;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.Relationship;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.StreamCallback;
import org.apache.nifi.processor.util.StandardValidators;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@EventDriven
@SideEffectFree
@InputRequirement(Requirement.INPUT_REQUIRED)
@Tags({"jeecg", "labor", "team", "processor"})
@CapabilityDescription("Example processor template for labor team data transformation.")
public class LaborTeamProcessor extends AbstractProcessor {


    // 公司信息：统一社会信用代码 + 类型
    private static final class CompanyInfo {
        final String creditCode;
        final String corpType;
        CompanyInfo(String creditCode, String corpType) {
            this.creditCode = creditCode;
            this.corpType = corpType;
        }
    }

    public static final Map<String, CompanyInfo> COMPANY_MAP = new HashMap<>();
    // 别名映射：将脏数据/别名统一到规范公司名（键、值都使用 normalizeCompanyName 处理后）
    public static final Map<String, String> COMPANY_ALIASES = new HashMap<>();

    static {
        // key 使用规范化公司名（去空格、统一括号、去结尾“公司/有限公司/股份有限公司/集团有限公司”等）
        COMPANY_MAP.put(normalizeCompanyName("湖北盛荣建设集团有限公司"), new CompanyInfo("91420500777586514R", "009"));
        // 示例
        COMPANY_MAP.put(normalizeCompanyName("湖北中业宏工程咨询有限公司"), new CompanyInfo("914205001791258143", "007"));
        COMPANY_MAP.put(normalizeCompanyName("中国建筑第七工程局有限公司"), new CompanyInfo("91410000169954619U", "009"));
        COMPANY_MAP.put(normalizeCompanyName("湖北广成建设工程有限公司"), new CompanyInfo("91420500309750336R", "006"));

        COMPANY_MAP.put(normalizeCompanyName("宜昌市点军区住房和城乡建设局"), new CompanyInfo("unknown", "006"));
        COMPANY_MAP.put(normalizeCompanyName("华中科技大学同济医学院附属协和医院宜昌医院"), new CompanyInfo("12420500MB1P92376B", "008"));
       
       // 别名：将“湖北盛荣建设有限公司”映射到“湖北盛荣建设集团有限公司”
       COMPANY_ALIASES.put(
               normalizeCompanyName("湖北盛荣建设有限公司"),
               normalizeCompanyName("湖北盛荣建设集团有限公司")
       );
       
       COMPANY_MAP.put(normalizeCompanyName("宜昌鹏顺置业有限公司"), new CompanyInfo("91420500MA490LT044", "006"));

       COMPANY_MAP.put(normalizeCompanyName("湖北华雷工程项目管理有限公司"), new CompanyInfo("91420500714689698D", "007"));
       COMPANY_MAP.put(normalizeCompanyName("宜昌金猇置业有限公司"), new CompanyInfo("91420505MAD19KHK7P", "009"));

    }
    

    // 常见公司后缀，按长到短匹配，确保“集团有限公司”等先于“有限公司”被去掉
 

    // 公司名归一化：小写、去空白、统一括号、去常见后缀、去常见中性词片段
    private static String normalizeCompanyName(String name) {
        if (isBlank(name)) { return null; }
        String s = name.toLowerCase()
                .replace("（", "(")
                .replace("）", ")")
                .replaceAll("\\s+", "");
        
        return s;
    }



    private static CompanyInfo lookupCompanyInfo(String organName) {
        String norm = normalizeCompanyName(organName);
        if (norm == null) { return null; }
        // 别名归并：先查别名得到规范名
        String canonical = COMPANY_ALIASES.getOrDefault(norm, norm);
        return COMPANY_MAP.get(canonical);
    }

    // 工种 like 匹配规则
    private static final String WORKTYPE_DEFAULT = "030"; // 其他（非空但未命中规则时）
    private static final String WORKTYPE_WHEN_BLANK = "908"; // 管理人员（当 worktype 为空时）

    private static final class WorkTypeRule {
        final String code;
        final String[] keywords;
        WorkTypeRule(String code, String... keywords) {
            this.code = code;
            this.keywords = keywords;
        }
    }

    private static final java.util.List<WorkTypeRule> WORKTYPE_RULES = new java.util.ArrayList<>();

    static {
        // 更具体的放前面，避免被泛化词命中
        // —— 管理岗 ——
        WORKTYPE_RULES.add(new WorkTypeRule("909", "业主管理人员", "业主管理"));
        WORKTYPE_RULES.add(new WorkTypeRule("908", "管理人员"));
        WORKTYPE_RULES.add(new WorkTypeRule("910", "资料员"));
        WORKTYPE_RULES.add(new WorkTypeRule("911", "专职安全员"));
        WORKTYPE_RULES.add(new WorkTypeRule("913", "安全员"));
        WORKTYPE_RULES.add(new WorkTypeRule("912", "材料员"));
        WORKTYPE_RULES.add(new WorkTypeRule("914", "测量员")); // 与 020 区分
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

    private static String normalizeWorkType(String s) {
        if (isBlank(s)) { return null; }
        String t = s.trim();
        // 将常见“无/未填/空”等表达直接视为空
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

    public static final Relationship REL_SUCCESS = new Relationship.Builder()
            .name("success")
            .description("Success relationship")
            .build();

    public static final Relationship REL_FAILURE = new Relationship.Builder()
            .name("failure")
            .description("Failure relationship")
            .build();

    public static final Relationship REL_PERSON_DATA = new Relationship.Builder()
            .name("person")
            .description("Person relationship")
            .build();

    public static final Relationship REL_TEAM_DATA = new Relationship.Builder()
            .name("team")
            .description("Team relationship")
            .build();

    public static final Relationship REL_PERSON_BASIC_DATA = new Relationship.Builder()
            .name("personInfo")
            .description("Person basic info relationship")
            .build();

    public static final Relationship REL_COMPANY_DATA = new Relationship.Builder()
            .name("company")
            .description("Company relationship")
            .build();

    // 固定的人脸/头像Base64图片
    private static final String DEFAULT_BASE64_IMAGE = "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";

    // 空值处理策略
    private enum EmptyFieldPolicy {
        OMIT_BLANKS,
        INCLUDE_NULLS;

        static EmptyFieldPolicy fromString(String s) {
            if (s == null) { return OMIT_BLANKS; }
            if ("includeNulls".equals(s)) { return INCLUDE_NULLS; }
            return OMIT_BLANKS;
        }
    }
    private static final PropertyDescriptor PROP_APP_KEY = new PropertyDescriptor.Builder()
            .name("App Key")
            .description("B系统的appKey，可使用表达式从FlowFile属性读取")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .build();

    private static final PropertyDescriptor PROP_APP_SECRET = new PropertyDescriptor.Builder()
            .name("App Secret")
            .description("B系统的appSecret，可使用表达式从FlowFile属性读取")
            .required(true)
            .sensitive(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .build();

    private static final PropertyDescriptor PROP_SUPPLIER = new PropertyDescriptor.Builder()
            .name("Supplier")
            .description("B系统的supplier，可使用表达式从FlowFile属性读取")
            .required(false)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .build();

    private static final PropertyDescriptor PROP_PERSON_METHOD = new PropertyDescriptor.Builder()
            .name("Person Method")
            .description("下游人员接口method，默认 ProjectWorkerEntryExit.Upload")
            .required(false)
            .defaultValue("ProjectWorkerEntryExit.Upload")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .build();

    private static final PropertyDescriptor PROP_TEAM_METHOD = new PropertyDescriptor.Builder()
            .name("Team Method")
            .description("下游班组接口method，默认 ProjectTeam.Upload")
            .required(false)
            .defaultValue("ProjectTeam.Upload")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .build();

    private static final PropertyDescriptor PROP_PERSON_BASIC_METHOD = new PropertyDescriptor.Builder()
            .name("Person Basic Method")
            .description("下游人员基本信息接口method，默认 ProjectWorker.Upload")
            .required(false)
            .defaultValue("ProjectWorker.Upload")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .build();

    private static final PropertyDescriptor PROP_COMPANY_METHOD = new PropertyDescriptor.Builder()
            .name("Company Method")
            .description("下游企业接口method，默认 ProjectCorp.Upload")
            .required(false)
            .defaultValue("ProjectCorp.Upload")
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .expressionLanguageSupported(ExpressionLanguageScope.FLOWFILE_ATTRIBUTES)
            .build();

    private static final PropertyDescriptor PROP_EMPTY_POLICY = new PropertyDescriptor.Builder()
            .name("Empty Field Policy")
            .description("空值输出策略：omitBlanks(默认，忽略空白) 或 includeNulls(输出null/空串)")
            .required(false)
            .allowableValues("omitBlanks", "includeNulls")
            .defaultValue("omitBlanks")
            .build();

    @Override
    protected List<PropertyDescriptor> getSupportedPropertyDescriptors() {
        List<PropertyDescriptor> descriptors = new ArrayList<>();
        descriptors.add(PROP_APP_KEY);
        descriptors.add(PROP_APP_SECRET);
        descriptors.add(PROP_SUPPLIER);
        descriptors.add(PROP_PERSON_METHOD);
        descriptors.add(PROP_TEAM_METHOD);
        descriptors.add(PROP_PERSON_BASIC_METHOD);
        descriptors.add(PROP_COMPANY_METHOD);
        descriptors.add(PROP_EMPTY_POLICY);
        return descriptors;
    }

    @Override
    public Set<Relationship> getRelationships() {
        Set<Relationship> relationships = new HashSet<>();
        relationships.add(REL_SUCCESS);
        relationships.add(REL_FAILURE);
        relationships.add(REL_PERSON_DATA);
        relationships.add(REL_TEAM_DATA);
        relationships.add(REL_PERSON_BASIC_DATA);
        relationships.add(REL_COMPANY_DATA);
        return relationships;
    }

    @Override
    public void onTrigger(ProcessContext context, ProcessSession session) throws ProcessException {
        FlowFile originalFlowFile = session.get();
        if (originalFlowFile == null) {
            return;
        }

        try {
            final ObjectMapper objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
            final ObjectMapper compactMapper = new ObjectMapper();

            // 读取原始内容
            final byte[] originalContent = readFlowFileContent(session, originalFlowFile);

                        // 解析为人员列表（List<Map>）
                        List<Map<String, Object>> personList = objectMapper.readValue(
                    originalContent,
                                new TypeReference<List<Map<String, Object>>>() {}
                        );

            // 选择空值策略
            String emptyPolicyStr = context.getProperty(PROP_EMPTY_POLICY).getValue();
            EmptyFieldPolicy emptyPolicy = EmptyFieldPolicy.fromString(emptyPolicyStr);

            // 处理得到团队和人员记录
            List<Map<String, Object>> teamDataList = processTeamData(personList, emptyPolicy);
            List<Map<String, Object>> personTeamDataList = processPersonTeamData(personList, emptyPolicy);
            List<Map<String, Object>> personBasicDataList = processPersonBasicData(personList, emptyPolicy);
            List<Map<String, Object>> companyDataList = processCompanyData(personList, emptyPolicy);

            // 生成公共属性（签名等）
            Map<String, String> commonAttributes = generateCommonAttributes(context, originalFlowFile);

            String appKeyAttr = commonAttributes.get("b.system.appKey");
            String timestampAttr = commonAttributes.get("b.system.timestamp");
            String signAttr = commonAttributes.get("b.system.sign");

            String teamMethod = context.getProperty(PROP_TEAM_METHOD).evaluateAttributeExpressions(originalFlowFile).getValue();
            if (isBlank(teamMethod)) {
                teamMethod = "ProjectTeam.Upload";
            }
            String personMethod = context.getProperty(PROP_PERSON_METHOD).evaluateAttributeExpressions(originalFlowFile).getValue();
            if (isBlank(personMethod)) {
                personMethod = "ProjectWorkerEntryExit.Upload";
            }
            String personBasicMethod = context.getProperty(PROP_PERSON_BASIC_METHOD).evaluateAttributeExpressions(originalFlowFile).getValue();
            if (isBlank(personBasicMethod)) {
                personBasicMethod = "ProjectWorker.Upload";
            }
            String companyMethod = context.getProperty(PROP_COMPANY_METHOD).evaluateAttributeExpressions(originalFlowFile).getValue();
            if (isBlank(companyMethod)) {
                companyMethod = "ProjectCorp.Upload";
            }

            // 输出团队数组（一个FlowFile）
            FlowFile teamFlowFile = session.create(originalFlowFile);
            teamFlowFile = session.putAllAttributes(teamFlowFile, commonAttributes);
            teamFlowFile = session.putAttribute(teamFlowFile, "mime.type", "application/json");

            String teamDataJsonString = compactMapper.writeValueAsString(teamDataList);
            Map<String, Object> teamEnvelope = new HashMap<>();
            teamEnvelope.put("appKey", appKeyAttr);
            teamEnvelope.put("timestamp", timestampAttr);
            teamEnvelope.put("method", teamMethod);
            teamEnvelope.put("data", teamDataJsonString);
            teamEnvelope.put("sign", signAttr);

            final byte[] teamBytes = objectMapper.writeValueAsBytes(teamEnvelope);
            teamFlowFile = session.write(teamFlowFile, out -> out.write(teamBytes));
            session.transfer(teamFlowFile, REL_TEAM_DATA);

            // 输出人员数组（一个FlowFile）
            FlowFile personFlowFile = session.create(originalFlowFile);
            personFlowFile = session.putAllAttributes(personFlowFile, commonAttributes);
            personFlowFile = session.putAttribute(personFlowFile, "mime.type", "application/json");

            String personDataJsonString = compactMapper.writeValueAsString(personTeamDataList);
            Map<String, Object> personEnvelope = new HashMap<>();
            personEnvelope.put("appKey", appKeyAttr);
            personEnvelope.put("timestamp", timestampAttr);
            personEnvelope.put("method", personMethod);
            personEnvelope.put("data", personDataJsonString);
            personEnvelope.put("sign", signAttr);

            final byte[] personBytes = objectMapper.writeValueAsBytes(personEnvelope);
            personFlowFile = session.write(personFlowFile, out -> out.write(personBytes));
            session.transfer(personFlowFile, REL_PERSON_DATA);

            // 输出人员基本信息数组（一个FlowFile）
            FlowFile personBasicFlowFile = session.create(originalFlowFile);
            personBasicFlowFile = session.putAllAttributes(personBasicFlowFile, commonAttributes);
            personBasicFlowFile = session.putAttribute(personBasicFlowFile, "mime.type", "application/json");

            String personBasicJsonString = compactMapper.writeValueAsString(personBasicDataList);
            Map<String, Object> personBasicEnvelope = new HashMap<>();
            personBasicEnvelope.put("appKey", appKeyAttr);
            personBasicEnvelope.put("timestamp", timestampAttr);
            personBasicEnvelope.put("method", personBasicMethod);
            personBasicEnvelope.put("data", personBasicJsonString);
            personBasicEnvelope.put("sign", signAttr);

            final byte[] personBasicBytes = objectMapper.writeValueAsBytes(personBasicEnvelope);
            personBasicFlowFile = session.write(personBasicFlowFile, out -> out.write(personBasicBytes));
            session.transfer(personBasicFlowFile, REL_PERSON_BASIC_DATA);

            // 输出企业数组（一个FlowFile）
            FlowFile companyFlowFile = session.create(originalFlowFile);
            companyFlowFile = session.putAllAttributes(companyFlowFile, commonAttributes);
            companyFlowFile = session.putAttribute(companyFlowFile, "mime.type", "application/json");

            String companyJsonString = compactMapper.writeValueAsString(companyDataList);
            Map<String, Object> companyEnvelope = new HashMap<>();
            companyEnvelope.put("appKey", appKeyAttr);
            companyEnvelope.put("timestamp", timestampAttr);
            companyEnvelope.put("method", companyMethod);
            companyEnvelope.put("data", companyJsonString);
            companyEnvelope.put("sign", signAttr);

            final byte[] companyBytes = objectMapper.writeValueAsBytes(companyEnvelope);
            companyFlowFile = session.write(companyFlowFile, out -> out.write(companyBytes));
            session.transfer(companyFlowFile, REL_COMPANY_DATA);

            getLogger().info("LaborTeamProcessor 生成 team数组={} 条, person数组={} 条, personInfo数组={} 条, company数组={} 条", new Object[]{teamDataList.size(), personTeamDataList.size(), personBasicDataList.size(), companyDataList.size()});
            // 移除原始FlowFile
            session.remove(originalFlowFile);
        } catch (Exception e) {
            getLogger().error("数据处理失败: {}", new Object[]{e.getMessage()}, e);
            try {
                originalFlowFile = session.putAttribute(originalFlowFile, "processing.status", "error");
                originalFlowFile = session.putAttribute(originalFlowFile, "processing.error", safeMessage(e));
            } catch (Exception ignore) {
                // ignore
            }
            session.transfer(originalFlowFile, REL_FAILURE);
        }
    }

    // 生成公共属性（签名等）
    private static Map<String, String> generateCommonAttributes(ProcessContext context, FlowFile flowFile) throws Exception {
        Map<String, String> attributes = new HashMap<>();

        String appKey = context.getProperty(PROP_APP_KEY).evaluateAttributeExpressions(flowFile).getValue();
        String appSecret = context.getProperty(PROP_APP_SECRET).evaluateAttributeExpressions(flowFile).getValue();
        String supplier = context.getProperty(PROP_SUPPLIER).evaluateAttributeExpressions(flowFile).getValue();

        if (isBlank(appKey) || isBlank(appSecret)) {
            throw new IllegalArgumentException("生成签名失败：App Key 或 App Secret 为空");
        }

        String bTimestamp = nowEpochMilliString();
        String stringToSign = (appSecret + appKey + bTimestamp + nvl(supplier) + appSecret).toLowerCase();
        String bSign = sha256Hex(stringToSign, StandardCharsets.UTF_8).toLowerCase();

        attributes.put("b.system.appKey", appKey);
        attributes.put("b.system.timestamp", bTimestamp);
        attributes.put("b.system.supplier", nvl(supplier));
        attributes.put("b.system.sign", bSign);
        attributes.put("processing.status", "success");

        return attributes;
    }

    // 处理团队数据
    private static List<Map<String, Object>> processTeamData(List<Map<String, Object>> personList, EmptyFieldPolicy emptyPolicy) {
                        Map<String, List<Map<String, Object>>> teamMap = new HashMap<>();
                        for (Map<String, Object> person : personList) {
                            if (person == null) {
                                continue;
                            }
                            String teamName = toStringOrNull(person.get("team"));
                            if (teamName != null) {
                                teamMap.computeIfAbsent(teamName, k -> new ArrayList<>()).add(person);
                            }
                        }

                        List<Map<String, Object>> teamDataList = new ArrayList<>();
                        for (Map.Entry<String, List<Map<String, Object>>> entry : teamMap.entrySet()) {
                            String teamName = entry.getKey();
                            List<Map<String, Object>> teamMembers = entry.getValue();
                            if (isBlank(teamName) || teamMembers.isEmpty()) {
                                continue;
                            }

                            Map<String, Object> firstMember = teamMembers.get(0);

                            // 查找班组长
                            String leaderName = null;
                            String cardNo = null;
                            for (Map<String, Object> member : teamMembers) {
                                String isHeadMan = toStringOrNull(member.get("isHeadMan"));
                                if ("是".equals(isHeadMan)) {
                                    leaderName = toStringOrNull(member.get("name"));
                                    cardNo = toStringOrNull(member.get("idCardNumber"));
                                    break;
                                }
                            }

                            String externalId = toStringOrNull(firstMember.get("id"));
                            String engId = toStringOrNull(firstMember.get("engId"));
                            String organName = toStringOrNull(firstMember.get("organName"));
                            String workType = toStringOrNull(firstMember.get("worktype"));

                            String teamCode = String.valueOf(teamName.hashCode()) + "_" + nvl(engId);

            Map<String, Object> teamData = new java.util.LinkedHashMap<>();
            putField(teamData, "externalId", externalId, emptyPolicy);
                            teamData.put("teamCode", teamCode);
                            teamData.put("teamName", teamName);
            CompanyInfo info = lookupCompanyInfo(organName);
            String corpCode = info == null ? "" : info.creditCode;
            String corpType = info == null ? "" : info.corpType;
            putField(teamData, "corpCode", corpCode, emptyPolicy);
            putField(teamData, "corpName", organName, emptyPolicy);
            putField(teamData, "corpType", corpType, emptyPolicy);
            teamData.put("teamType", isBlank(teamName) ? "410002":"410001");
            putField(teamData, "workType", getWorkTypeForToD6C(workType), emptyPolicy);
            putField(teamData, "leaderName", leaderName, emptyPolicy);
            putField(teamData, "cardNo", cardNo, emptyPolicy);
                            teamData.put("amount", teamMembers.size());
                            teamData.put("isPass", 1);
                            teamData.put("isBlack", 0);

                            teamDataList.add(teamData);
        }
        return teamDataList;
                        }

    // 处理人员数据
    private static List<Map<String, Object>> processPersonTeamData(List<Map<String, Object>> personList, EmptyFieldPolicy emptyPolicy) {
                        List<Map<String, Object>> personTeamDataList = new ArrayList<>();
                        for (Map<String, Object> person : personList) {
                            if (person == null) {
                                continue;
                            }
                            String teamName = toStringOrNull(person.get("team"));
                            String engId = toStringOrNull(person.get("engId"));
                            String organName = toStringOrNull(person.get("organName"));
                            String isHeadMan = toStringOrNull(person.get("isHeadMan"));

            String teamCode = (teamName != null ? String.valueOf(teamName.hashCode()) : "") +"_"+ nvl(engId);

            Map<String, Object> personTeam = new java.util.LinkedHashMap<>();
            putField(personTeam, "externalId", toStringOrNull(person.get("id")), emptyPolicy);
                            personTeam.put("status", 1);
            putField(personTeam, "idCardNumber", toStringOrNull(person.get("idCardNumber")), emptyPolicy);
            putField(personTeam, "workerName", toStringOrNull(person.get("name")), emptyPolicy);
                            personTeam.put("teamCode", teamCode);
            putField(personTeam, "teamName", teamName, emptyPolicy);
            CompanyInfo info2 = lookupCompanyInfo(organName);
            String corpCode2 = info2 == null ? "" : info2.creditCode;
            String corpType2 = info2 == null ? "" : info2.corpType;
            putField(personTeam, "corpCode", corpCode2, emptyPolicy);
            putField(personTeam, "corpName", organName, emptyPolicy);
            putField(personTeam, "corpType", corpType2, emptyPolicy);
            putField(personTeam, "workType", getWorkTypeForToD6C(toStringOrNull(person.get("worktype"))), emptyPolicy);
            personTeam.put("measureWay", "1");
            personTeam.put("measureUnit", "02");
            personTeam.put("unitPrice", 300.0);
                            personTeam.put("isTeamLeader", "是".equals(isHeadMan) ? 1 : 0);
                            personTeam.put("entryExitTime", nowFormatted());

                            personTeamDataList.add(personTeam);
        }
        return personTeamDataList;
    }

    // 处理人员基本信息数据（唯一键：身份证号）
    private static List<Map<String, Object>> processPersonBasicData(List<Map<String, Object>> personList, EmptyFieldPolicy emptyPolicy) {
        java.util.LinkedHashMap<String, Map<String, Object>> idToBasic = new java.util.LinkedHashMap<>();
        for (Map<String, Object> person : personList) {
            if (person == null) {
                continue;
            }
            String idCardNumber = toStringOrNull(person.get("idCardNumber"));
            if (isBlank(idCardNumber)) {
                // 唯一键缺失，跳过
                continue;
            }

            // 新增字段及别名回退
            String externalId = toStringOrNull(person.get("externalId"));
            if (isBlank(externalId)) {
                externalId = toStringOrNull(person.get("id"));
            }
            String workerName = toStringOrNull(person.get("workerName"));
            if (isBlank(workerName)) {
                workerName = toStringOrNull(person.get("name"));
            }
            String workerCode = toStringOrNull(person.get("workerCode"));
            String cellPhone = toStringOrNull(person.get("cellPhone"));
            if (isBlank(cellPhone)) {
                String altPhone = toStringOrNull(person.get("phone"));
                cellPhone = isBlank(altPhone) ? toStringOrNull(person.get("mobile")) : altPhone;
                if (isBlank(cellPhone)) {
                    cellPhone = toStringOrNull(person.get("mobilePhone"));
                }
                if (isBlank(cellPhone)) {
                    cellPhone = "";
                }

            }

            // 校验必填字段（含你列出的新增必填）
            String address = toStringOrDefault(person, "address","");
            String birthPlace = toStringOrDefault(person, "birthPlace","");
            // 带默认值示例：nation 默认 "02"
            String nation = toStringOrDefault(person, "nation", "02");
            String grantOrg = toStringOrDefault(person, "grantOrg", "");
            String cardStartDate = toStringOrDefault(person, "cardStartDate", "");
            String cardExpiryDate = toStringOrDefault(person, "cardExpiryDate", "");
            // 固定使用默认Base64
            String faceImage = DEFAULT_BASE64_IMAGE;
            String headImage = DEFAULT_BASE64_IMAGE;
            String politicsType = toStringOrDefault(person, "politicsType", "");
            String cultureLevelType = toStringOrDefault(person, "cultureLevelType", "");

            // if (isBlank(workerName) || isBlank(cellPhone)
            //         || isBlank(address) || isBlank(birthPlace) || isBlank(nation) || isBlank(grantOrg)
            //         || isBlank(cardStartDate) || isBlank(cardExpiryDate) || isBlank(faceImage)
            //         || isBlank(headImage) || isBlank(politicsType) || isBlank(cultureLevelType)) {
            //     // 任一必填缺失，跳过该人员
            //     continue;
            // }

            // 使用有序Map，严格保证字段输出顺序（仅传非空字段）
            Map<String, Object> basic = new java.util.LinkedHashMap<>();
            // 顺序：externalId, idCardNumber, workerName, workerCode, cellPhone
            putField(basic, "externalId", externalId, emptyPolicy);
            basic.put("idCardNumber", idCardNumber);
            basic.put("workerName", workerName);
            putField(basic, "workerCode", workerCode, emptyPolicy);
            basic.put("cellPhone", cellPhone);
            // 其余字段按文档列出顺序，仅在非空时包含
            putField(basic, "address", address, emptyPolicy);
            putField(basic, "birthPlace", birthPlace, emptyPolicy);
            putField(basic, "nation", nation, emptyPolicy);
            putField(basic, "grantOrg", grantOrg, emptyPolicy);
            putField(basic, "cardStartDate", cardStartDate, emptyPolicy);
            putField(basic, "cardExpiryDate", cardExpiryDate, emptyPolicy);
            // 人像、头像强制输出固定Base64
            basic.put("faceImage", faceImage);
            basic.put("headImage", headImage);
            putField(basic, "positiveIdCardImage", toStringOrNull(person.get("positiveIdCardImage")), emptyPolicy);
            putField(basic, "negativeIdCardImage", toStringOrNull(person.get("negativeIdCardImage")), emptyPolicy);
            putField(basic, "politicsType", politicsType, emptyPolicy);
            putField(basic, "cultureLevelType", cultureLevelType, emptyPolicy);
            putField(basic, "bloodType", toStringOrNull(person.get("bloodType")), emptyPolicy);
            putField(basic, "urgentLinkMan", toStringOrNull(person.get("urgentLinkMan")), emptyPolicy);
            putField(basic, "urgentLinkManPhone", toStringOrNull(person.get("urgentLinkManPhone")), emptyPolicy);

            Integer isEntryUnionVal = toIntegerOrNull(person.get("isEntryUnion"));
            if (isEntryUnionVal != null) { basic.put("isEntryUnion", isEntryUnionVal); }
            putField(basic, "entryUnionTime", toStringOrNull(person.get("entryUnionTime")), emptyPolicy);
            Integer isHaveLaborCertificateVal = toIntegerOrNull(person.get("isHaveLaborCertificate"));
            if (isHaveLaborCertificateVal != null) { basic.put("isHaveLaborCertificate", isHaveLaborCertificateVal); }
            Integer isHaveMajorMedicalHistoryVal = toIntegerOrNull(person.get("isHaveMajorMedicalHistory"));
            if (isHaveMajorMedicalHistoryVal != null) { basic.put("isHaveMajorMedicalHistory", isHaveMajorMedicalHistoryVal); }
            putField(basic, "cardNo", toStringOrNull(person.get("cardNo")), emptyPolicy);
            putField(basic, "maritalStatus", toStringOrNull(person.get("maritalStatus")), emptyPolicy);
            putField(basic, "healthCode", toStringOrNull(person.get("healthCode")), emptyPolicy);
            Integer isBlackVal = toIntegerOrNull(person.get("isBlack"));
            if (isBlackVal != null) { basic.put("isBlack", isBlackVal); }
            putField(basic, "bankCode", toStringOrNull(person.get("bankCode")), emptyPolicy);
            putField(basic, "bankName", toStringOrNull(person.get("bankName")), emptyPolicy);
            putField(basic, "bankCardNo", toStringOrNull(person.get("bankCardNo")), emptyPolicy);

            Object extend = person.get("extendObject");
            if (extend instanceof Map && !((Map<?, ?>) extend).isEmpty()) {
                basic.put("extendObject", extend);
            }

            // 按身份证去重，后出现的覆盖先前记录
            idToBasic.put(idCardNumber, basic);
        }
        return new ArrayList<>(idToBasic.values());
    }

    // 处理企业数据（去重按 corpCode 或 corpName 规范化）
    private static List<Map<String, Object>> processCompanyData(List<Map<String, Object>> personList, EmptyFieldPolicy emptyPolicy) {
        java.util.LinkedHashMap<String, Map<String, Object>> keyToCompany = new java.util.LinkedHashMap<>();
        for (Map<String, Object> person : personList) {
            if (person == null) {
                continue;
            }
            String organName = toStringOrNull(person.get("organName"));
            if (isBlank(organName)) {
                continue;
            }
            CompanyInfo info = lookupCompanyInfo(organName);
            String corpCode = info == null ? "" : info.creditCode;
            String corpType = info == null ? "" : info.corpType;

            // 组装记录
            Map<String, Object> company = new java.util.LinkedHashMap<>();
            // externalId: 使用上游原始 id/companyId 如有，否则为空
            String externalId = toStringOrNull(person.get("companyExternalId"));
            if (isBlank(externalId)) {
                externalId = toStringOrNull(person.get("corpExternalId"));
            }
            putField(company, "externalId", externalId, emptyPolicy);
            putField(company, "corpCode", corpCode, emptyPolicy);
            putField(company, "corpName", organName, emptyPolicy);
            putField(company, "corpType", corpType, emptyPolicy);
            // 上下级、发包方等可从人员侧透传，如无则为空
            putField(company, "supCorpCode", toStringOrNull(person.get("supCorpCode")), emptyPolicy);
            putField(company, "supCorpName", toStringOrNull(person.get("supCorpName")), emptyPolicy);
            putField(company, "contractCorpCode", toStringOrNull(person.get("contractCorpCode")), emptyPolicy);
            putField(company, "contractCorpName", toStringOrNull(person.get("contractCorpName")), emptyPolicy);
            putField(company, "contractCode", toStringOrNull(person.get("contractCode")), emptyPolicy);
            Integer statusVal = toIntegerOrNull(person.get("corpStatus"));
            if (statusVal != null) { company.put("status", statusVal); }
            putField(company, "entryTime", toStringOrNull(person.get("corpEntryTime")), emptyPolicy);
            putField(company, "exitTime", toStringOrNull(person.get("corpExitTime")), emptyPolicy);

            // 去重优先依据 corpCode，否则用标准化 corpName
            //String key = !isBlank(corpCode) ? ("code:" + corpCode) : ("name:" + normalizeCompanyName(organName));
            keyToCompany.put(organName, company);
        }
        return new ArrayList<>(keyToCompany.values());
    }

    private static byte[] readFlowFileContent(ProcessSession session, FlowFile flowFile) {
        final java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        session.read(flowFile, in -> {
            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        });
        return baos.toByteArray();
    }

    private static byte[] readAllBytes(InputStream in) throws Exception {
        byte[] buffer = new byte[8192];
        int len;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        while ((len = in.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }

    private static String toStringOrNull(Object value) {
        if (value == null) {
            return null;
        }
        String s = String.valueOf(value);
        return "null".equalsIgnoreCase(s) ? null : s;
    }

    // 当 key 缺失或值为空白/null 时，返回 defaultValue
    private static String toStringOrDefault(Map<String, Object> map, String key, String defaultValue) {
        if (map == null) { return defaultValue; }
        Object val = map.get(key);
        String str = toStringOrNull(val);
        return isBlank(str) ? defaultValue : str;
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String nvl(String s) {
        return s == null ? "" : s;
    }

    private static void putIfNotBlank(Map<String, Object> map, String key, String value) {
        if (!isBlank(value)) {
            map.put(key, value);
        }
    }

    // 根据空值策略写入：
    // - OMIT_BLANKS: 空白不写入
    // - INCLUDE_NULLS: 空白也写入（保持key，值可能为空串或null）
    private static void putField(Map<String, Object> map, String key, String value, EmptyFieldPolicy policy) {
        if (policy == EmptyFieldPolicy.INCLUDE_NULLS) {
            map.put(key, value);
            return;
        }
        // 默认策略：忽略空白
        if (!isBlank(value)) {
            map.put(key, value);
        }
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

    private static String nowFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    private static String nowEpochMilliString() {
        return String.valueOf(System.currentTimeMillis());
    }

    // 已废弃：使用 lookupCompanyInfo 取数

    private static String sha256Hex(String data, Charset charset) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = data.getBytes(charset);
        byte[] hash = digest.digest(bytes);
        StringBuilder sb = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            sb.append(Character.forDigit((b >> 4) & 0xF, 16));
            sb.append(Character.forDigit((b & 0xF), 16));
        }
        return sb.toString();
    }

    private static int toInt(Object value, int defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        try {
            String s = String.valueOf(value).trim();
            if (s.isEmpty() || "null".equalsIgnoreCase(s)) {
                return defaultValue;
            }
            return Integer.parseInt(s);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static String safeMessage(Exception e) {
        String msg = e.getMessage();
        return msg == null ? e.getClass().getSimpleName() : msg;
    }

    private static String getWorkTypeForToD6C(String workType) {
        String norm = normalizeWorkType(workType);
        if (norm == null) { return WORKTYPE_WHEN_BLANK; }
        for (WorkTypeRule rule : WORKTYPE_RULES) {
            if (containsAny(norm, rule.keywords)) {
                return rule.code;
            }
        }
        return WORKTYPE_DEFAULT;
    }
}


