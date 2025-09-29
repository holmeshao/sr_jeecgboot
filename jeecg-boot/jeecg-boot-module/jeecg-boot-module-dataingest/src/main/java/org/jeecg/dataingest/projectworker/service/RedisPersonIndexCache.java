package org.jeecg.dataingest.projectworker.service;

import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 以 engId+姓名+证件前4后4 作为索引，将人员明文身份证建立快速候选索引。
 * 用于考勤脱敏身份证解析的本地缓存加速，避免在双重循环中频繁拉取人员接口。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisPersonIndexCache {

    private final StringRedisTemplate redis;

    private static String n(String s) { return s == null ? "" : s.trim(); }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private static String prefix4(String id) {
        if (id == null) { return null; }
        String s = id.trim();
        return s.length() >= 4 ? s.substring(0, 4) : null;
    }

    private static String suffix4(String id) {
        if (id == null) { return null; }
        String s = id.trim();
        return s.length() >= 4 ? s.substring(s.length() - 4) : null;
    }

    private String key(String engId, String name, String p4, String s4) {
        return "att:personidx:" + n(engId) + ":" + n(name) + ":" + n(p4) + ":" + n(s4);
    }

    /**
     * 将人员清单建立索引：engId+姓名+证件前4后4 -> Set(真实身份证)
     */
    public void indexPersons(String engId, List<JSONObject> persons, int ttlDays) {
        if (isBlank(engId) || persons == null || persons.isEmpty()) { return; }
        Duration ttl = Duration.ofDays(Math.max(1, ttlDays));
        for (JSONObject p : persons) {
            try {
                String id = toStr(p.get("idCardNumber"));
                String name = toStr(p.get("name"));
                if (isBlank(name)) { name = toStr(p.get("workerName")); }
                if (isBlank(id) || isBlank(name)) { continue; }
                String p4 = prefix4(id), s4 = suffix4(id);
                if (p4 == null || s4 == null) { continue; }
                String k = key(engId, name, p4, s4);
                Long added = redis.opsForSet().add(k, id);
                if (added != null && added > 0) {
                    redis.expire(k, ttl);
                }
            } catch (Exception e) {
                log.warn("indexPersons error: {}", e.getMessage());
            }
        }
    }

    /**
     * 根据 engId + 姓名 + 脱敏串（取前4后4）获取候选真实身份证集合
     */
    public Set<String> getCandidates(String engId, String name, String masked) {
        try {
            if (isBlank(engId) || isBlank(name) || isBlank(masked)) { return Collections.emptySet(); }
            String s = masked.trim();
            if (s.length() < 8) { return Collections.emptySet(); }
            String p4 = s.substring(0, 4);
            String s4 = s.substring(s.length() - 4);
            String k = key(engId, name, p4, s4);
            Set<String> members = redis.opsForSet().members(k);
            return members == null ? Collections.emptySet() : members;
        } catch (Exception e) {
            log.warn("getCandidates error: {}", e.getMessage());
            return Collections.emptySet();
        }
    }

    private static String toStr(Object v) { return v == null ? null : String.valueOf(v); }
}


