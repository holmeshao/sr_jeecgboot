package org.jeecg.modules.workflow.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowConfig;

/**
 * 🎯 表单工作流配置Mapper
 * 基于JeecgBoot MyBatis-Plus标准
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Mapper
public interface OnlCgformWorkflowConfigMapper extends BaseMapper<OnlCgformWorkflowConfig> {

    /**
     * 根据表单ID查询工作流配置
     */
    @Select("SELECT * FROM onl_cgform_workflow_config WHERE cgform_head_id = #{formId} AND status = 1")
    OnlCgformWorkflowConfig selectByFormId(@Param("formId") String formId);

    /**
     * 根据流程定义Key查询配置
     */
    @Select("SELECT * FROM onl_cgform_workflow_config WHERE process_definition_key = #{processKey} AND status = 1")
    OnlCgformWorkflowConfig selectByProcessKey(@Param("processKey") String processKey);

    /**
     * 检查表单是否已配置工作流
     */
    @Select("SELECT COUNT(1) FROM onl_cgform_workflow_config WHERE cgform_head_id = #{formId} AND workflow_enabled = true AND status = 1")
    int checkWorkflowEnabled(@Param("formId") String formId);

    /**
     * 查询启用了工作流的表单数量
     */
    @Select("SELECT COUNT(1) FROM onl_cgform_workflow_config WHERE workflow_enabled = true AND status = 1")
    int countEnabledWorkflows();
}