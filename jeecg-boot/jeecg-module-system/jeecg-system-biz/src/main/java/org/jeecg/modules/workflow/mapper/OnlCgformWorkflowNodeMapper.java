package org.jeecg.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;

import java.util.List;

/**
 * 🎯 工作流节点权限配置Mapper
 * 支持字段级别的精细化权限查询
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Mapper
public interface OnlCgformWorkflowNodeMapper extends BaseMapper<OnlCgformWorkflowNode> {

    /**
     * 根据表单ID和节点ID查询权限配置
     */
    @Select("SELECT * FROM onl_cgform_workflow_node " +
            "WHERE cgform_head_id = #{formId} AND node_id = #{nodeId} AND status = 1")
    OnlCgformWorkflowNode selectByFormAndNode(@Param("formId") String formId, @Param("nodeId") String nodeId);

    /**
     * 根据流程定义Key和节点ID查询权限配置
     */
    @Select("SELECT * FROM onl_cgform_workflow_node " +
            "WHERE process_definition_key = #{processKey} AND node_id = #{nodeId} AND status = 1")
    OnlCgformWorkflowNode selectByProcessAndNode(@Param("processKey") String processKey, @Param("nodeId") String nodeId);

    /**
     * 查询流程的所有节点权限配置
     */
    @Select("SELECT * FROM onl_cgform_workflow_node " +
            "WHERE process_definition_key = #{processKey} AND status = 1 " +
            "ORDER BY sort_order ASC, create_time ASC")
    List<OnlCgformWorkflowNode> selectByProcessKey(@Param("processKey") String processKey);

    /**
     * 查询表单的所有节点权限配置
     */
    @Select("SELECT * FROM onl_cgform_workflow_node " +
            "WHERE cgform_head_id = #{formId} AND status = 1 " +
            "ORDER BY sort_order ASC, create_time ASC")
    List<OnlCgformWorkflowNode> selectByFormId(@Param("formId") String formId);

    /**
     * 查询流程中配置了字段权限的节点数量
     */
    @Select("SELECT COUNT(1) FROM onl_cgform_workflow_node " +
            "WHERE process_definition_key = #{processKey} AND status = 1 " +
            "AND (editable_fields IS NOT NULL OR readonly_fields IS NOT NULL " +
            "OR hidden_fields IS NOT NULL OR required_fields IS NOT NULL)")
    int countConfiguredNodes(@Param("processKey") String processKey);

    /**
     * 检查节点是否已配置权限
     */
    @Select("SELECT COUNT(1) FROM onl_cgform_workflow_node " +
            "WHERE cgform_head_id = #{formId} AND node_id = #{nodeId} AND status = 1")
    int checkNodeConfigured(@Param("formId") String formId, @Param("nodeId") String nodeId);
}