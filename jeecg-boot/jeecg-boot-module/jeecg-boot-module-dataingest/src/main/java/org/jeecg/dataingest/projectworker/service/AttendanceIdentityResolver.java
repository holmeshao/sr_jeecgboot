package org.jeecg.dataingest.projectworker.service;

import java.util.Set;

/**
 * 脱敏身份证 + 姓名 + engId -> 真实身份证 解析器
 * 支持强键解析与候选集合管理
 */
public interface AttendanceIdentityResolver {

    /**
     * 解析真实身份证（强键：maskedIdCard + name + engId）
     * 返回真实身份证或 null
     */
    String resolveStrong(String maskedIdCard, String name, String engId);

    /**
     * 写入解析映射（当我们已知真实身份证时）
     */
    void putResolved(String maskedIdCard, String name, String engId, String realIdCard, int ttlDays);

    /**
     * 添加候选真实身份证集合（弱键：maskedIdCard + name + engId）
     */
    void addCandidate(String maskedIdCard, String name, String engId, String candidateIdCard, int ttlHours);

    /**
     * 读取候选集合
     */
    Set<String> getCandidates(String maskedIdCard, String name, String engId);
}
