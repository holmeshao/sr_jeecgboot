package org.jeecg.modules.workflow.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * 表单快照
 *
 * @author jeecg
 * @since 2024-12-25
 */
@Data
public class FormSnapshot {
    
    /**
     * 节点代码
     */
    private String nodeCode;
    
    /**
     * 表单数据
     */
    private Map<String, Object> formData;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    /**
     * 操作人
     */
    private String operator;
    
    /**
     * 变更字段列表
     */
    private List<String> changedFields;
    
    /**
     * 快照描述
     */
    private String description;
} 