package org.jeecg.modules.workflow.strategy;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformField;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformFieldService;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 🎯 智能默认字段权限策略
 * 基于JeecgBoot在线表单系统，实现字段的智能分类和权限分配
 * 
 * 核心理念：
 * - 发起节点：业务字段可编辑，通用字段隐藏
 * - 审批节点：业务字段只读，通用字段可编辑
 * - 查看节点：所有字段只读
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Slf4j
@Service
public class DefaultFieldPermissionStrategy {

    @Autowired
    private IOnlCgformFieldService onlCgformFieldService;

    @Autowired
    private IOnlCgformHeadService onlCgformHeadService;

    // =============== 字段识别规则 ===============

    /**
     * 通用流程字段名称模式
     * 用于识别审批意见、处理说明等工作流通用字段
     */
    private static final Pattern COMMON_FIELD_NAME_PATTERN = Pattern.compile(
        "(audit|approve|process|review|handle|comment|opinion|remark|note)_.*|" +
        ".*(opinion|comment|remark|note|memo|desc|description|reason|result)$",
        Pattern.CASE_INSENSITIVE
    );

    /**
     * 通用流程字段注释关键词
     */
    private static final String[] COMMON_FIELD_COMMENT_KEYWORDS = {
        "审批", "意见", "备注", "说明", "处理", "审核", "批注", "评论", 
        "原因", "结果", "建议", "反馈", "记录", "操作", "流程"
    };

    /**
     * 系统内置字段
     * 这些字段通常不需要用户编辑
     */
    private static final Pattern SYSTEM_FIELD_PATTERN = Pattern.compile(
        "(id|create_by|create_time|update_by|update_time|del_flag|version|" +
        "process_instance_id|bmp_status|tenant_id|dept_id)$",
        Pattern.CASE_INSENSITIVE
    );

    /**
     * 文件上传字段
     */
    private static final Pattern FILE_FIELD_PATTERN = Pattern.compile(
        ".*(file|attachment|upload|photo|image|picture|doc|pdf).*",
        Pattern.CASE_INSENSITIVE
    );

    // =============== 核心权限生成方法 ===============

    /**
     * 🎯 生成智能默认权限配置
     * 
     * @param formId 表单ID
     * @param nodeId 节点ID
     * @return 权限配置
     */
    public FormPermissionConfig generateDefaultPermission(String formId, String nodeId) {
        log.debug("为表单 {} 节点 {} 生成智能默认权限", formId, nodeId);

        try {
            // 1. 根据formId获取表单头信息
            OnlCgformHead cgformHead = onlCgformHeadService.getById(formId);
            if (cgformHead == null) {
                log.warn("表单头信息不存在：formId={}", formId);
                return new FormPermissionConfig();
            }

            // 2. 根据表单头ID获取字段信息（使用正确的MyBatis-Plus API）
            List<OnlCgformField> allFields = onlCgformFieldService.list(
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<OnlCgformField>()
                    .eq(OnlCgformField::getCgformHeadId, cgformHead.getId())
                    .orderByAsc(OnlCgformField::getOrderNum)
            );
            
            if (oConvertUtils.isEmpty(allFields)) {
                log.warn("表单 {} 未找到字段信息", formId);
                return new FormPermissionConfig();
            }

            // 2. 智能分类字段
            FieldClassification classification = classifyFields(allFields);
            log.debug("字段分类完成：业务字段{}个，通用字段{}个，系统字段{}个", 
                     classification.businessFields.size(),
                     classification.commonFields.size(), 
                     classification.systemFields.size());

            // 3. 根据节点类型生成权限
            FormPermissionConfig config = generatePermissionByNodeType(nodeId, classification);

            log.debug("智能默认权限生成完成：可编辑{}个，只读{}个，隐藏{}个", 
                     config.getEditableFields().size(),
                     config.getReadonlyFields().size(),
                     config.getHiddenFields().size());

            return config;

        } catch (Exception e) {
            log.error("生成智能默认权限失败：表单{} 节点{}", formId, nodeId, e);
            return createFallbackConfig();
        }
    }

    /**
     * 🎯 智能字段分类
     * 
     * @param allFields 所有字段
     * @return 分类结果
     */
    private FieldClassification classifyFields(List<OnlCgformField> allFields) {
        FieldClassification classification = new FieldClassification();

        for (OnlCgformField field : allFields) {
            String fieldName = field.getDbFieldName();
            String fieldComment = field.getDbFieldTxt();

            if (isSystemField(fieldName, fieldComment)) {
                classification.systemFields.add(fieldName);
            } else if (isCommonProcessField(fieldName, fieldComment)) {
                classification.commonFields.add(fieldName);
            } else {
                classification.businessFields.add(fieldName);
            }
        }

        return classification;
    }

    /**
     * 🎯 根据节点类型生成权限配置
     */
    private FormPermissionConfig generatePermissionByNodeType(String nodeId, FieldClassification classification) {
        FormPermissionConfig config = new FormPermissionConfig();

        if (isStartNode(nodeId)) {
            // 发起节点：业务字段可编辑，通用字段隐藏，系统字段隐藏
            config.setEditableFields(new ArrayList<>(classification.businessFields));
            config.setHiddenFields(new ArrayList<>(classification.commonFields));
            config.getHiddenFields().addAll(classification.systemFields);
            config.setFormMode("EDIT");
            log.debug("发起节点权限：业务字段可编辑，通用字段隐藏");

        } else if (isEndNode(nodeId)) {
            // 结束节点：所有字段只读
            config.setReadonlyFields(new ArrayList<>(classification.businessFields));
            config.getReadonlyFields().addAll(classification.commonFields);
            config.setHiddenFields(new ArrayList<>(classification.systemFields));
            config.setFormMode("VIEW");
            log.debug("结束节点权限：所有字段只读");

        } else {
            // 中间节点（审批节点）：业务字段只读，通用字段可编辑，系统字段隐藏
            config.setReadonlyFields(new ArrayList<>(classification.businessFields));
            config.setEditableFields(new ArrayList<>(classification.commonFields));
            config.setHiddenFields(new ArrayList<>(classification.systemFields));
            config.setFormMode("OPERATE");
            log.debug("审批节点权限：业务字段只读，通用字段可编辑");
        }

        return config;
    }

    // =============== 字段识别方法 ===============

    /**
     * 🎯 智能识别通用流程字段
     * 根据字段名和注释智能判断是否为工作流通用字段
     */
    private boolean isCommonProcessField(String fieldName, String comment) {
        if (oConvertUtils.isEmpty(fieldName)) {
            return false;
        }

        // 1. 按字段名模式识别
        if (COMMON_FIELD_NAME_PATTERN.matcher(fieldName).find()) {
            log.debug("字段 {} 通过名称模式识别为通用字段", fieldName);
            return true;
        }

        // 2. 按注释关键词识别
        if (oConvertUtils.isNotEmpty(comment)) {
            for (String keyword : COMMON_FIELD_COMMENT_KEYWORDS) {
                if (comment.contains(keyword)) {
                    log.debug("字段 {} 通过注释关键词 '{}' 识别为通用字段", fieldName, keyword);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 🎯 识别系统内置字段
     */
    private boolean isSystemField(String fieldName, String comment) {
        if (oConvertUtils.isEmpty(fieldName)) {
            return false;
        }
        return SYSTEM_FIELD_PATTERN.matcher(fieldName).find();
    }

    /**
     * 🎯 识别文件上传字段
     */
    private boolean isFileField(String fieldName, String comment) {
        if (oConvertUtils.isEmpty(fieldName)) {
            return false;
        }
        return FILE_FIELD_PATTERN.matcher(fieldName).find();
    }

    /**
     * 🎯 判断是否为发起节点
     */
    private boolean isStartNode(String nodeId) {
        if (oConvertUtils.isEmpty(nodeId)) {
            return false;
        }
        String lowerNodeId = nodeId.toLowerCase();
        return lowerNodeId.contains("start") || lowerNodeId.contains("begin") || 
               lowerNodeId.equals("startevent1") || lowerNodeId.equals("start_1");
    }

    /**
     * 🎯 判断是否为结束节点
     */
    private boolean isEndNode(String nodeId) {
        if (oConvertUtils.isEmpty(nodeId)) {
            return false;
        }
        String lowerNodeId = nodeId.toLowerCase();
        return lowerNodeId.contains("end") || lowerNodeId.contains("finish") ||
               lowerNodeId.equals("endevent1") || lowerNodeId.equals("end_1");
    }

    // =============== 高级策略方法 ===============

    /**
     * 🎯 生成基于角色的权限配置
     */
    public FormPermissionConfig generateRoleBasedPermission(String formId, String nodeId, List<String> userRoles) {
        FormPermissionConfig baseConfig = generateDefaultPermission(formId, nodeId);
        
        // 根据用户角色调整权限
        if (userRoles.contains("admin") || userRoles.contains("super_admin")) {
            // 管理员拥有更多权限
            enhanceAdminPermissions(baseConfig);
        }
        
        return baseConfig;
    }

    /**
     * 🎯 增强管理员权限
     */
    private void enhanceAdminPermissions(FormPermissionConfig config) {
        // 管理员可以看到更多字段，但仍然遵循基本的权限逻辑
        // 例如：可以查看系统字段，但不能编辑
        List<String> hiddenFields = config.getHiddenFields();
        if (hiddenFields != null && !hiddenFields.isEmpty()) {
            // 将部分隐藏字段改为只读
            List<String> systemFields = hiddenFields.stream()
                .filter(field -> SYSTEM_FIELD_PATTERN.matcher(field).find())
                .collect(Collectors.toList());
            
            config.getHiddenFields().removeAll(systemFields);
            config.getReadonlyFields().addAll(systemFields);
            
            log.debug("管理员权限增强：{}个系统字段从隐藏改为只读", systemFields.size());
        }
    }

    /**
     * 🎯 创建兜底配置
     * 当出现异常时使用的安全配置
     */
    private FormPermissionConfig createFallbackConfig() {
        FormPermissionConfig config = new FormPermissionConfig();
        config.setFormMode("VIEW");
        config.setEditableFields(Collections.emptyList());
        config.setReadonlyFields(Collections.emptyList());
        config.setHiddenFields(Collections.emptyList());
        config.setRequiredFields(Collections.emptyList());
        log.warn("使用兜底权限配置：所有字段只读");
        return config;
    }

    // =============== 内部类：字段分类结果 ===============

    /**
     * 字段分类结果
     */
    private static class FieldClassification {
        List<String> businessFields = new ArrayList<>();    // 业务字段
        List<String> commonFields = new ArrayList<>();      // 通用流程字段  
        List<String> systemFields = new ArrayList<>();      // 系统字段
        List<String> fileFields = new ArrayList<>();        // 文件字段
    }
}