package org.jeecg.dataingest.projectworker.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 工地项目人员信息集成配置
 * A系统：宜昌实名制平台系统
 * B系统：D6C系统
 */
@Data
@Component
@ConfigurationProperties(prefix = "projectworker")
public class ProjectWorkerIntegrationProperties {

    private YichangApi yichangApi = new YichangApi();
    private D6CApi d6cApi = new D6CApi();
    private Defaults defaults = new Defaults();
    private ProjectConfig projectConfig = new ProjectConfig();

    /**
     * 宜昌实名制平台系统API配置
     */
    @Data
    public static class YichangApi {
        private String base = "https://zhcjsmz.sanxiacloud.com";
        private String personPath = "/labor/externalCall/person";
        // 可选：人员照片与人脸特征接口路径（若未提供则不启用照片补充）
        private String photoPath;
        // 是否启用从A系统补充人脸与证件头像（默认关闭，仍使用默认Base64图片）
        private boolean enablePhotoEnrich = false;
    }

    /**
     * D6C系统API配置
     */
    @Data
    public static class D6CApi {
        private String appKey;
        private String appSecret;
        private String supplier;
        // D6C网关（统一入口URL）。例如：https://d6c.example.com/api
        private String gateway;
        private Method method = new Method();

        @Data
        public static class Method {
            private String companyBasic = "Corp.Upload";
            private String company = "ProjectCorp.Upload";
            private String team = "ProjectTeam.Upload";
            private String personBasic = "ProjectWorker.Upload";
            private String person = "ProjectWorkerEntryExit.Upload";
        }
    }

    /**
     * 默认配置
     */
    @Data
    public static class Defaults {
        // 可配置的工种规则和企业映射（键值对）
        private Map<String, String> corpTypeMapping;   // organName -> corpType
        private Map<String, String> corpCodeMapping;   // organName -> corpCode
        private List<WorkTypeRule> workTypeRules;      // 自定义覆盖/追加的工种关键字规则
        private String defaultBase64Image = "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";

        private String defaultCellPhone = "12345678911";
        //籍贯
        private String defaultbirthPlace = "unknown";
        //发证机关
        private String defaultGrantOrg = "unknown";
        private String defaultAddress = "unknown";


        private String defaultTeamName = "管理⼈员";

    }

    /**
     * 工种规则配置
     */
    @Data
    public static class WorkTypeRule {
        private String code;
        private List<String> keywords;
    }

    /**
     * 项目配置
     */
    @Data
    public static class ProjectConfig {
        // 支持从文件加载或直接在 yml 中内联
        private String jsonPath; // e.g. classpath:projectworker/project-projects.json or file:/etc/jeecg/project-projects.json
        private List<Project> items;
        private Nacos nacos = new Nacos();
    }

    /**
     * Nacos 配置
     */
    @Data
    public static class Nacos {
        /** 是否启用从 Nacos 加载并监听热更新 */
        private boolean enabled = false;
        /** dataId，例如：projectworker-projects.json */
        private String dataId;
        /** 分组，默认 DEFAULT_GROUP */
        private String group = "DEFAULT_GROUP";
        /** 获取配置超时（毫秒） */
        private long timeoutMs = 3000;
    }

    /**
     * 项目信息
     */
    @Data
    public static class Project {
        private String engId;
        private String code;
        private String appkey;
        private String appSecret;
        private String projectName;
    }
}
