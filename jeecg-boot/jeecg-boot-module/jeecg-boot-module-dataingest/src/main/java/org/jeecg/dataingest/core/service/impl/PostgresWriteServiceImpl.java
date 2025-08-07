package org.jeecg.dataingest.core.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.dataingest.core.service.IPostgresWriteService;
import org.jeecg.dataingest.service.INiFiNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * PostgreSQL数据写入服务实现
 * @Description: PostgreSQL数据写入服务实现
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Slf4j
@Service
public class PostgresWriteServiceImpl implements IPostgresWriteService {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private INiFiNotificationService niFiNotificationService;
    
    @Override
    public boolean writeCreateData(String tableName, JsonNode data, String taskId) {
        log.info("写入创建数据到表: {}, 任务ID: {}", tableName, taskId);
        
        try {
            // 检查表是否存在，不存在则创建
            if (!tableExists(tableName)) {
                if (!createTable(tableName, data)) {
                    log.error("创建表失败: {}", tableName);
                    return false;
                }
            }
            
            // 构建插入SQL
            StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
            StringBuilder values = new StringBuilder(" VALUES (");
            List<Object> params = new ArrayList<>();
            
            Iterator<Map.Entry<String, JsonNode>> fields = data.fields();
            boolean first = true;
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (!first) {
                    sql.append(", ");
                    values.append(", ");
                }
                sql.append(field.getKey());
                values.append("?");
                params.add(getFieldValue(field.getValue()));
                first = false;
            }
            
            // 添加数据血缘字段
            sql.append(", data_source_task_id, data_ingest_time");
            values.append(", ?, NOW())");
            params.add(taskId);
            
            sql.append(")").append(values).append(")");
            
            // 执行插入
            int result = jdbcTemplate.update(sql.toString(), params.toArray());
            
            if (result > 0) {
                log.info("成功插入数据到表: {}, 影响行数: {}", tableName, result);
                // 通知NiFi处理
                notifyNiFi(tableName, "CREATE", taskId);
                return true;
            } else {
                log.warn("插入数据失败，影响行数为0: {}", tableName);
                return false;
            }
            
        } catch (Exception e) {
            log.error("写入创建数据失败: " + tableName, e);
            return false;
        }
    }
    
    @Override
    public boolean writeUpdateData(String tableName, JsonNode beforeData, JsonNode afterData, String taskId) {
        log.info("写入更新数据到表: {}, 任务ID: {}", tableName, taskId);
        
        try {
            // 检查表是否存在
            if (!tableExists(tableName)) {
                log.error("目标表不存在: {}", tableName);
                return false;
            }
            
            // 构建更新SQL（这里简化处理，实际应该根据主键更新）
            StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
            List<Object> params = new ArrayList<>();
            
            Iterator<Map.Entry<String, JsonNode>> fields = afterData.fields();
            boolean first = true;
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                if (!first) {
                    sql.append(", ");
                }
                sql.append(field.getKey()).append(" = ?");
                params.add(getFieldValue(field.getValue()));
                first = false;
            }
            
            // 添加更新时间和任务ID
            sql.append(", data_update_time = NOW(), data_source_task_id = ?");
            params.add(taskId);
            
            // 这里需要根据主键进行WHERE条件，简化处理
            sql.append(" WHERE 1=1"); // 实际应该根据主键字段构建WHERE条件
            
            // 执行更新
            int result = jdbcTemplate.update(sql.toString(), params.toArray());
            
            if (result >= 0) {
                log.info("成功更新数据到表: {}, 影响行数: {}", tableName, result);
                // 通知NiFi处理
                notifyNiFi(tableName, "UPDATE", taskId);
                return true;
            } else {
                log.warn("更新数据失败: {}", tableName);
                return false;
            }
            
        } catch (Exception e) {
            log.error("写入更新数据失败: " + tableName, e);
            return false;
        }
    }
    
    @Override
    public boolean writeDeleteData(String tableName, JsonNode data, String taskId) {
        log.info("处理删除数据到表: {}, 任务ID: {}", tableName, taskId);
        
        try {
            // 对于删除操作，我们采用软删除方式
            // 检查表是否存在
            if (!tableExists(tableName)) {
                log.error("目标表不存在: {}", tableName);
                return false;
            }
            
            // 构建软删除SQL
            StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET ");
            sql.append("is_deleted = 1, delete_time = NOW(), data_source_task_id = ?");
            List<Object> params = new ArrayList<>();
            params.add(taskId);
            
            // 这里需要根据主键进行WHERE条件，简化处理
            sql.append(" WHERE 1=1"); // 实际应该根据主键字段构建WHERE条件
            
            // 执行软删除
            int result = jdbcTemplate.update(sql.toString(), params.toArray());
            
            if (result >= 0) {
                log.info("成功软删除数据到表: {}, 影响行数: {}", tableName, result);
                // 通知NiFi处理
                notifyNiFi(tableName, "DELETE", taskId);
                return true;
            } else {
                log.warn("软删除数据失败: {}", tableName);
                return false;
            }
            
        } catch (Exception e) {
            log.error("处理删除数据失败: " + tableName, e);
            return false;
        }
    }
    
    @Override
    public boolean tableExists(String tableName) {
        try {
            String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ? AND table_schema = 'public'";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName.toLowerCase());
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("检查表是否存在失败: " + tableName, e);
            return false;
        }
    }
    
    @Override
    public boolean createTable(String tableName, JsonNode sourceData) {
        log.info("创建表: {}", tableName);
        
        try {
            StringBuilder sql = new StringBuilder("CREATE TABLE ").append(tableName).append(" (");
            
            // 添加主键ID字段
            sql.append("id SERIAL PRIMARY KEY, ");
            
            // 根据源数据推断字段类型
            Iterator<Map.Entry<String, JsonNode>> fields = sourceData.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String fieldName = field.getKey();
                String fieldType = inferPostgresType(field.getValue());
                
                sql.append(fieldName).append(" ").append(fieldType).append(", ");
            }
            
            // 添加数据血缘字段
            sql.append("data_source_task_id VARCHAR(64), ");
            sql.append("data_ingest_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP, ");
            sql.append("data_update_time TIMESTAMP, ");
            sql.append("is_deleted INTEGER DEFAULT 0, ");
            sql.append("delete_time TIMESTAMP");
            
            sql.append(")");
            
            // 执行创建表SQL
            jdbcTemplate.execute(sql.toString());
            
            // 创建索引
            String indexSql = "CREATE INDEX idx_" + tableName + "_task_id ON " + tableName + "(data_source_task_id)";
            jdbcTemplate.execute(indexSql);
            
            String indexSql2 = "CREATE INDEX idx_" + tableName + "_ingest_time ON " + tableName + "(data_ingest_time)";
            jdbcTemplate.execute(indexSql2);
            
            log.info("成功创建表: {}", tableName);
            return true;
            
        } catch (Exception e) {
            log.error("创建表失败: " + tableName, e);
            return false;
        }
    }
    
    @Override
    public boolean notifyNiFi(String tableName, String operation, String taskId) {
        try {
            // 调用NiFi通知服务
            return niFiNotificationService.notifyDataReady(tableName, operation, taskId);
        } catch (Exception e) {
            log.error("通知NiFi失败: " + tableName, e);
            return false;
        }
    }
    
    /**
     * 推断PostgreSQL字段类型
     */
    private String inferPostgresType(JsonNode value) {
        if (value.isNull()) {
            return "VARCHAR(255)";
        } else if (value.isBoolean()) {
            return "BOOLEAN";
        } else if (value.isInt()) {
            return "INTEGER";
        } else if (value.isLong()) {
            return "BIGINT";
        } else if (value.isFloat() || value.isDouble()) {
            return "DECIMAL(18,6)";
        } else if (value.isTextual()) {
            String text = value.asText();
            if (text.length() > 255) {
                return "TEXT";
            } else {
                return "VARCHAR(255)";
            }
        } else {
            return "TEXT"; // JSON对象或数组存储为TEXT
        }
    }
    
    /**
     * 获取字段值
     */
    private Object getFieldValue(JsonNode value) {
        if (value.isNull()) {
            return null;
        } else if (value.isBoolean()) {
            return value.asBoolean();
        } else if (value.isInt()) {
            return value.asInt();
        } else if (value.isLong()) {
            return value.asLong();
        } else if (value.isFloat() || value.isDouble()) {
            return value.asDouble();
        } else if (value.isTextual()) {
            return value.asText();
        } else {
            return value.toString(); // JSON对象或数组转为字符串
        }
    }
}