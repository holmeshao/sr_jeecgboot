package org.jeecg.modules.workflow.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 链式关联服务
 * 提供链式关联字段的批量填充功能
 * 
 * 核心特性：
 * 1. 批量查询优化（避免 N+1 问题）
 * 2. 支持多级链式关联
 * 3. 静默失败（查不出来返回空）
 * 4. 安全机制（表名和字段白名单）
 * 
 * @author jeecg
 * @since 2024-12-26
 */
@Slf4j
@Service
public class LinkChainService {
    
    @Autowired
    private IOnlCgformHeadService cgformHeadService;
    
    @Autowired
    private IOnlCgformFieldService cgformFieldService;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    /**
     * 🎯 批量填充链式关联字段（列表页使用）
     * 
     * @param tableName 表名
     * @param records 记录列表
     */
    public void fillLinkChainFields(String tableName, List<Map<String, Object>> records) {
        if (records == null || records.isEmpty()) {
            return;
        }
        
        try {
            // 1. 获取表单配置
            OnlCgformHead head = cgformHeadService.getOne(
                new LambdaQueryWrapper<OnlCgformHead>()
                    .eq(OnlCgformHead::getTableName, tableName)
            );
            if (head == null) {
                log.warn("未找到表单配置: {}", tableName);
                return;
            }
            
            // 2. 获取所有链式关联字段配置
            List<OnlCgformField> linkChainFields = cgformFieldService.list(
                new LambdaQueryWrapper<OnlCgformField>()
                    .eq(OnlCgformField::getCgformHeadId, head.getId())
                    .like(OnlCgformField::getFieldExtendJson, "valueFromField")
            );
            
            if (linkChainFields.isEmpty()) {
                return;
            }
            
            // 3. 解析链式关联配置
            List<LinkChainConfig> configs = new ArrayList<>();
            for (OnlCgformField field : linkChainFields) {
                try {
                    LinkChainConfig config = parseLinkChainConfig(field);
                    if (config != null) {
                        configs.add(config);
                    }
                } catch (Exception e) {
                    log.warn("解析链式关联配置失败: field={}, error={}", field.getDbFieldName(), e.getMessage());
                }
            }
            
            if (configs.isEmpty()) {
                return;
            }
            
            // 4. 批量填充（优化：批量查询 + 内存映射）
            for (LinkChainConfig config : configs) {
                try {
                    batchFillLinkChainField(records, config);
                } catch (Exception e) {
                    log.warn("批量填充链式关联字段失败: field={}, error={}", config.targetField, e.getMessage());
                    // 静默失败，不影响其他字段
                }
            }
            
            log.debug("链式关联字段填充完成: tableName={}, count={}", tableName, records.size());
            
        } catch (Exception e) {
            log.error("批量填充链式关联字段失败: tableName={}", tableName, e);
            // 静默失败，不影响列表查询
        }
    }
    
    /**
     * 🎯 批量填充单个链式关联字段（优化版）
     */
    private void batchFillLinkChainField(List<Map<String, Object>> records, LinkChainConfig config) {
        // 1. 收集所有需要查询的 key
        Set<String> keys = records.stream()
            .map(r -> r.get(config.valueFromField))
            .filter(Objects::nonNull)
            .filter(v -> !v.toString().isEmpty())
            .map(Object::toString)
            .collect(Collectors.toSet());
        
        if (keys.isEmpty()) {
            // 所有记录的来源字段都为空，直接设置目标字段为 null
            for (Map<String, Object> record : records) {
                record.put(config.targetField, null);
            }
            return;
        }
        
        // 2. 批量查询（使用 IN 语句）
        Map<String, Object> valueMap = batchQueryLinkChainValues(config, keys);
        
        // 3. 填充记录
        for (Map<String, Object> record : records) {
            Object keyValue = record.get(config.valueFromField);
            if (keyValue != null && !keyValue.toString().isEmpty()) {
                Object resultValue = valueMap.get(keyValue.toString());
                record.put(config.targetField, resultValue);
            } else {
                record.put(config.targetField, null);
            }
        }
    }
    
    /**
     * 🎯 批量查询链式关联值
     */
    private Map<String, Object> batchQueryLinkChainValues(LinkChainConfig config, Set<String> keys) {
        try {
            // 1. 安全校验
            String safeTable = resolveSafeTableName(config.tableName);
            String safeKeyColumn = resolveSafeColumnName(config.tableName, config.keyField);
            String safeResultColumn = resolveSafeColumnName(config.tableName, config.resultField);
            
            // 2. 构建批量查询 SQL
            String placeholders = String.join(",", Collections.nCopies(keys.size(), "?"));
            String sql = "SELECT " + safeKeyColumn + ", " + safeResultColumn +
                         " FROM " + safeTable +
                         " WHERE " + safeKeyColumn + " IN (" + placeholders + ")";
            
            // 3. 执行查询
            List<Map<String, Object>> results = jdbcTemplate.queryForList(sql, keys.toArray());
            
            // 4. 构建映射
            Map<String, Object> valueMap = new HashMap<>();
            for (Map<String, Object> row : results) {
                Object keyVal = row.get(safeKeyColumn);
                Object resultVal = row.get(safeResultColumn);
                if (keyVal != null) {
                    valueMap.put(keyVal.toString(), resultVal);
                }
            }
            
            return valueMap;
            
        } catch (Exception e) {
            log.warn("批量查询链式关联值失败: table={}, error={}", config.tableName, e.getMessage());
            return new HashMap<>();
        }
    }
    
    /**
     * 🎯 解析单个链式关联值（表单详情页使用）
     * 支持多级链式关联
     * 
     * @param tableName 目标表名
     * @param keyField 关联字段
     * @param keyValue 关联值
     * @param resultField 显示字段
     * @return 显示值
     */
    public Result<String> resolveLinkChainValue(String tableName, String keyField, 
                                                String keyValue, String resultField) {
        try {
            // 1. 参数校验
            if (!StringUtils.hasText(tableName)) {
                return Result.OK(null); // 静默失败
            }
            if (!StringUtils.hasText(keyField) || !StringUtils.hasText(resultField)) {
                return Result.OK(null); // 静默失败
            }
            if (!StringUtils.hasText(keyValue)) {
                return Result.OK(null); // 空值直接返回
            }
            
            // 2. 安全校验
            String safeTable = resolveSafeTableName(tableName);
            String safeKeyColumn = resolveSafeColumnName(tableName, keyField);
            String safeResultColumn = resolveSafeColumnName(tableName, resultField);
            
            // 3. 安全查询
            String sql = "SELECT " + safeResultColumn +
                         " FROM " + safeTable +
                         " WHERE " + safeKeyColumn + " = ?";
            
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, keyValue);
            if (list == null || list.isEmpty()) {
                return Result.OK(null); // 查不到返回 null
            }
            
            Object value = list.get(0).get(safeResultColumn);
            return Result.OK(value == null ? null : String.valueOf(value));
            
        } catch (Exception e) {
            log.warn("链式派生查询失败: tableName={}, keyField={}, keyValue={}, resultField={}, error={}", 
                     tableName, keyField, keyValue, resultField, e.getMessage());
            return Result.OK(null); // 静默失败
        }
    }
    
    /**
     * 🎯 解析多级链式关联值
     * 
     * 配置格式：
     * {
     *   "chain": [
     *     {"tableName": "table1", "keyField": "id", "resultField": "field1"},
     *     {"tableName": "table2", "keyField": "field1", "resultField": "field2"}
     *   ]
     * }
     * 
     * @param chainConfig 链式配置（JSON 字符串）
     * @param initialValue 初始值
     * @return 最终显示值
     */
    public Result<String> resolveMultiLevelLinkChain(String chainConfig, String initialValue) {
        try {
            if (!StringUtils.hasText(chainConfig) || !StringUtils.hasText(initialValue)) {
                return Result.OK(null);
            }
            
            // 解析链式配置
            Map<String, Object> config = JSON.parseObject(chainConfig, Map.class);
            List<Map<String, String>> chain = (List<Map<String, String>>) config.get("chain");
            
            if (chain == null || chain.isEmpty()) {
                return Result.OK(null);
            }
            
            // 逐级查询
            String currentValue = initialValue;
            for (Map<String, String> step : chain) {
                String tableName = step.get("tableName");
                String keyField = step.get("keyField");
                String resultField = step.get("resultField");
                
                Result<String> result = resolveLinkChainValue(tableName, keyField, currentValue, resultField);
                if (result.getResult() == null) {
                    return Result.OK(null); // 任何一级查不到，返回 null
                }
                
                currentValue = result.getResult();
            }
            
            return Result.OK(currentValue);
            
        } catch (Exception e) {
            log.warn("多级链式关联查询失败: chainConfig={}, initialValue={}, error={}", 
                     chainConfig, initialValue, e.getMessage());
            return Result.OK(null); // 静默失败
        }
    }
    
    /**
     * 🎯 解析链式关联配置
     */
    private LinkChainConfig parseLinkChainConfig(OnlCgformField field) {
        if (!StringUtils.hasText(field.getFieldExtendJson())) {
            return null;
        }
        
        try {
            Map<String, Object> ext = JSON.parseObject(field.getFieldExtendJson(), Map.class);
            String valueFromField = (String) ext.get("valueFromField");
            if (!StringUtils.hasText(valueFromField)) {
                return null;
            }
            
            LinkChainConfig config = new LinkChainConfig();
            config.targetField = field.getDbFieldName();
            config.valueFromField = valueFromField;
            config.tableName = field.getDictTable();
            config.keyField = field.getDictField();
            config.resultField = field.getDictText();
            
            // 校验配置完整性
            if (!StringUtils.hasText(config.tableName) || 
                !StringUtils.hasText(config.keyField) || 
                !StringUtils.hasText(config.resultField)) {
                return null;
            }
            
            return config;
        } catch (Exception e) {
            log.warn("解析链式关联配置异常: field={}, error={}", field.getDbFieldName(), e.getMessage());
            return null;
        }
    }
    
    /**
     * 🎯 安全校验：表名白名单
     */
    private String resolveSafeTableName(String tableName) {
        // 1. 检查是否是已配置的 Online 表单
        OnlCgformHead head = cgformHeadService.getOne(
            new LambdaQueryWrapper<OnlCgformHead>()
                .eq(OnlCgformHead::getTableName, tableName)
        );
        
        if (head == null) {
            throw new IllegalArgumentException("表名不在白名单中: " + tableName);
        }
        
        return head.getTableName();
    }
    
    /**
     * 🎯 安全校验：字段白名单
     */
    private String resolveSafeColumnName(String tableName, String columnName) {
        // 1. 获取表单配置
        OnlCgformHead head = cgformHeadService.getOne(
            new LambdaQueryWrapper<OnlCgformHead>()
                .eq(OnlCgformHead::getTableName, tableName)
        );
        
        if (head == null) {
            throw new IllegalArgumentException("表名不存在: " + tableName);
        }
        
        // 2. 检查字段是否在元数据中
        OnlCgformField field = cgformFieldService.getOne(
            new LambdaQueryWrapper<OnlCgformField>()
                .eq(OnlCgformField::getCgformHeadId, head.getId())
                .eq(OnlCgformField::getDbFieldName, columnName)
        );
        
        if (field == null) {
            throw new IllegalArgumentException("字段不在白名单中: " + columnName);
        }
        
        return field.getDbFieldName();
    }
    
    /**
     * 链式关联配置
     */
    @Data
    private static class LinkChainConfig {
        String targetField;      // 目标字段（派生字段）
        String valueFromField;   // 来源字段
        String tableName;        // 目标表名
        String keyField;         // 关联字段
        String resultField;      // 显示字段
    }
}
