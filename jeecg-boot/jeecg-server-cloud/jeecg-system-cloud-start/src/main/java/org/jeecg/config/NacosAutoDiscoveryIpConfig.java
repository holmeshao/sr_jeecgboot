package org.jeecg.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.net.InetAddress;

/**
 * Ensure Spring Cloud Nacos registers a container-reachable IP when running locally.
 * Priority: explicit property -> env variables -> host.docker.internal (if resolvable).
 */
@Configuration
@ConditionalOnProperty(name = "jeecg.discovery.auto-ip.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class NacosAutoDiscoveryIpConfig {

    private final NacosDiscoveryProperties nacosDiscoveryProperties;
    private final Environment environment;

    @Value("${server.port:8080}")
    private int serverPort;

    public NacosAutoDiscoveryIpConfig(NacosDiscoveryProperties nacosDiscoveryProperties, Environment environment) {
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.environment = environment;
    }

    @PostConstruct
    public void applyDiscoveryIp() {
        final boolean forceOverride = environment.getProperty("jeecg.discovery.auto-ip.force", Boolean.class, true);

        String current = nacosDiscoveryProperties.getIp();
        if (!forceOverride && current != null && !current.isEmpty()) {
            log.info("[jeecg] Nacos discovery.ip already set to {}. Skip auto detection (force=false).", current);
            return;
        }

        // 1) Prefer env-driven overrides
        String env1 = environment.getProperty("spring.cloud.nacos.discovery.ip");
        String env2 = environment.getProperty("NACOS_DISCOVERY_IP");
        String chosen = firstNonBlank(env1, env2);

        // 2) Fallback to host.docker.internal if available (works in Docker Desktop with extra_hosts mapping)
        if (isBlank(chosen)) {
            try {
                InetAddress host = InetAddress.getByName("host.docker.internal");
                chosen = host.getHostAddress();
                log.info("[jeecg] Resolved host.docker.internal -> {} for Nacos discovery.ip", chosen);
            } catch (Exception e) {
                log.debug("[jeecg] host.docker.internal not resolvable: {}", e.getMessage());
            }
        }

        if (isBlank(chosen)) {
            log.warn("[jeecg] Could not determine discovery.ip automatically. Nacos will use default network address.");
            return;
        }

        nacosDiscoveryProperties.setIp(chosen);
        int currentPort;
        try {
            currentPort = nacosDiscoveryProperties.getPort();
        } catch (Throwable ignore) {
            currentPort = 0;
        }
        if (currentPort <= 0) {
            nacosDiscoveryProperties.setPort(serverPort);
            currentPort = serverPort;
        }
        log.info("[jeecg] Set Nacos discovery.ip={} port={} (force={})", chosen, currentPort, forceOverride);
    }

    private static String firstNonBlank(String a, String b) {
        return !isBlank(a) ? a : (!isBlank(b) ? b : null);
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}