package org.jeecg.dataingest.debezium;

import org.springframework.stereotype.Component;

/**
 * Debezium任务管理器（简化版）
 */
@Component
public class DebeziumTaskManager {

    public void startTask(String taskId) {
        System.out.println("启动任务: " + taskId);
    }

    public void stopTask(String taskId) {
        System.out.println("停止任务: " + taskId);
    }

    public String getTaskStatus(String taskId) {
        return "RUNNING";
    }

    // 简化的内部类
    public static class DebeziumTaskConfig {
        private String taskId;
        private String datasourceType;
        private String targetTableNamePre;

        public String getTaskId() { return taskId; }
        public void setTaskId(String taskId) { this.taskId = taskId; }
        
        public String getDatasourceType() { return datasourceType; }
        public void setDatasourceType(String datasourceType) { this.datasourceType = datasourceType; }
        
        public String getTargetTableNamePre() { return targetTableNamePre; }
        public void setTargetTableNamePre(String targetTableNamePre) { this.targetTableNamePre = targetTableNamePre; }
    }
} 