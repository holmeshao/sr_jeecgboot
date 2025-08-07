package org.jeecg.dataingest.core.service;

import org.springframework.stereotype.Service;
import java.util.Map;

/**
 * PostgreSQL数据写入服务（简化版）
 */
@Service
public class PostgresWriteService {
    
    public void writeData(String tableName, Map<String, Object> data) {
        System.out.println("写入数据到表: " + tableName + ", 数据: " + data);
    }
    
    public void createTable(String tableName, Map<String, String> columns) {
        System.out.println("创建表: " + tableName + ", 字段: " + columns);
    }
    
    public boolean tableExists(String tableName) {
            return true;
    }
} 