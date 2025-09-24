package org.jeecg.dataingest.projectworker.util;

import com.alibaba.fastjson.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/**
 * Utilities for Project Worker module.
 * Semantics are preserved to match existing service helpers exactly.
 */
public final class ProjectWorkerUtils {

    private ProjectWorkerUtils() {}

    public static String nowFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static String urlEncode(String s) {
        return s == null ? "" : URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String nvl(String s) {
        return s == null ? "" : s;
    }

    public static String toStr(Object v) {
        return v == null ? null : String.valueOf(v);
    }

    public static void putIfNotBlank(Map<String, Object> m, String k, String v) {
        if (!isBlank(v)) m.put(k, v);
    }

    public static Integer toIntegerOrNull(Object value) {
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

    public static String toStringOrDefault(JSONObject obj, String key, String defaultValue) {
        if (obj == null) { return defaultValue; }
        Object val = obj.get(key);
        String str = toStr(val);
        return isBlank(str) ? defaultValue : str;
    }

    public static String sha256Hex(String s) {
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
}


