package org.jeecg.dataingest.scheduler.config;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * XXL-Job配置类
 * @Description: XXL-Job配置类
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "xxl.job")
public class XXLJobConfig {
    
    /**调度中心地址*/
    private String adminAddresses;
    
    /**执行器名称*/
    private String appname;
    
    /**执行器注册地址*/
    private String address;
    
    /**执行器IP*/
    private String ip;
    
    /**执行器端口*/
    private int port;
    
    /**执行器运行日志文件存储磁盘路径*/
    private String logPath;
    
    /**执行器日志文件保存天数*/
    private int logRetentionDays;
    
    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
        xxlJobSpringExecutor.setAppname(appname);
        xxlJobSpringExecutor.setAddress(address);
        xxlJobSpringExecutor.setIp(ip);
        xxlJobSpringExecutor.setPort(port);
        xxlJobSpringExecutor.setLogPath(logPath);
        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
        return xxlJobSpringExecutor;
    }
} 