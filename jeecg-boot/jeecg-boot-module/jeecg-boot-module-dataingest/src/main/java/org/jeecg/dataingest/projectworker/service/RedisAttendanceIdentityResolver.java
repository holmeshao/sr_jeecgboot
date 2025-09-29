package org.jeecg.dataingest.projectworker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisAttendanceIdentityResolver implements AttendanceIdentityResolver {

    private final StringRedisTemplate redis;

    private static String strongKey(String masked, String name, String engId) {
        return "att:resolve:" + n(masked) + ":" + n(name) + ":" + n(engId);
    }

    private static String candKey(String masked, String name, String engId) {
        return "att:cands:" + n(masked) + ":" + n(name) + ":" + n(engId);
    }

    private static String n(String s) { return s == null ? "" : s.trim(); }

    @Override
    public String resolveStrong(String maskedIdCard, String name, String engId) {
        try {
            String k = strongKey(maskedIdCard, name, engId);
            return redis.opsForValue().get(k);
        } catch (Exception e) {
            log.warn("Redis resolveStrong error: {}", e.getMessage());
            return null;
        }
    }

    @Override
    public void putResolved(String maskedIdCard, String name, String engId, String realIdCard, int ttlDays) {
        try {
            if (realIdCard == null || realIdCard.trim().isEmpty()) { return; }
            String k = strongKey(maskedIdCard, name, engId);
            Duration ttl = Duration.ofDays(Math.max(1, ttlDays));
            redis.opsForValue().set(k, realIdCard, ttl);
        } catch (Exception e) {
            log.warn("Redis putResolved error: {}", e.getMessage());
        }
    }

    @Override
    public void addCandidate(String maskedIdCard, String name, String engId, String candidateIdCard, int ttlHours) {
        try {
            if (candidateIdCard == null || candidateIdCard.trim().isEmpty()) { return; }
            String k = candKey(maskedIdCard, name, engId);
            Long added = redis.opsForSet().add(k, candidateIdCard);
            if (added != null && added > 0) {
                redis.expire(k, Duration.ofHours(Math.max(1, ttlHours)));
            }
        } catch (Exception e) {
            log.warn("Redis addCandidate error: {}", e.getMessage());
        }
    }

    @Override
    public Set<String> getCandidates(String maskedIdCard, String name, String engId) {
        try {
            String k = candKey(maskedIdCard, name, engId);
            Set<String> members = redis.opsForSet().members(k);
            return members == null ? Collections.emptySet() : members;
        } catch (Exception e) {
            log.warn("Redis getCandidates error: {}", e.getMessage());
            return Collections.emptySet();
        }
    }
}


