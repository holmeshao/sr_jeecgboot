package org.jeecg.modules.workflow.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowNode;
import org.jeecg.modules.workflow.model.FormPermissionConfig;
import org.jeecg.modules.workflow.service.IOnlCgformWorkflowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 🎯 工作流节点权限配置控制器
 * 基于JeecgBoot标准控制器，提供字段权限的可视化配置界面
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Tag(name = "工作流节点权限配置")
@RestController
@RequestMapping("/workflow/node-permission")
@Slf4j
public class WorkflowNodePermissionController extends JeecgController<OnlCgformWorkflowNode, IOnlCgformWorkflowNodeService> {

    @Autowired
    private IOnlCgformWorkflowNodeService nodePermissionService;

    // =============== 标准CRUD接口 ===============

    /**
     * 🎯 分页查询节点权限配置
     */
    @AutoLog(value = "节点权限配置-分页列表查询")
    @Operation(summary = "节点权限配置-分页列表查询", description = "节点权限配置-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<OnlCgformWorkflowNode>> queryPageList(OnlCgformWorkflowNode nodePermission,
                                                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                              @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                              HttpServletRequest req) {
        QueryWrapper<OnlCgformWorkflowNode> queryWrapper = QueryGenerator.initQueryWrapper(nodePermission, req.getParameterMap());
        Page<OnlCgformWorkflowNode> page = new Page<OnlCgformWorkflowNode>(pageNo, pageSize);
        IPage<OnlCgformWorkflowNode> pageList = nodePermissionService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 🎯 添加节点权限配置
     */
    @AutoLog(value = "节点权限配置-添加")
    @Operation(summary = "节点权限配置-添加", description = "节点权限配置-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody OnlCgformWorkflowNode nodePermission) {
        nodePermissionService.save(nodePermission);
        return Result.OK("添加成功！");
    }

    /**
     * 🎯 编辑节点权限配置
     */
    @AutoLog(value = "节点权限配置-编辑")
    @Operation(summary = "节点权限配置-编辑", description = "节点权限配置-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody OnlCgformWorkflowNode nodePermission) {
        nodePermissionService.updateById(nodePermission);
        return Result.OK("编辑成功!");
    }

    /**
     * 🎯 通过id删除节点权限配置
     */
    @AutoLog(value = "节点权限配置-通过id删除")
    @Operation(summary = "节点权限配置-通过id删除", description = "节点权限配置-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        nodePermissionService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 🎯 批量删除节点权限配置
     */
    @AutoLog(value = "节点权限配置-批量删除")
    @Operation(summary = "节点权限配置-批量删除", description = "节点权限配置-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.nodePermissionService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 🎯 通过id查询节点权限配置
     */
    @AutoLog(value = "节点权限配置-通过id查询")
    @Operation(summary = "节点权限配置-通过id查询", description = "节点权限配置-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<OnlCgformWorkflowNode> queryById(@RequestParam(name = "id", required = true) String id) {
        OnlCgformWorkflowNode nodePermission = nodePermissionService.getById(id);
        if (nodePermission == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(nodePermission);
    }

    // =============== 业务专用接口 ===============

    /**
     * 🎯 根据表单ID获取节点权限配置
     */
    @AutoLog(value = "根据表单ID获取节点权限配置")
    @Operation(summary = "根据表单ID获取节点权限配置", description = "根据表单ID获取节点权限配置")
    @GetMapping(value = "/getByFormId")
    public Result<List<OnlCgformWorkflowNode>> getByFormId(@RequestParam(name = "formId", required = true) String formId) {
        List<OnlCgformWorkflowNode> list = nodePermissionService.getByFormId(formId);
        return Result.OK(list);
    }

    /**
     * 🎯 根据流程定义Key获取节点权限配置
     */
    @AutoLog(value = "根据流程Key获取节点权限配置")
    @Operation(summary = "根据流程Key获取节点权限配置", description = "根据流程Key获取节点权限配置")
    @GetMapping(value = "/getByProcessKey")
    public Result<List<OnlCgformWorkflowNode>> getByProcessKey(@RequestParam(name = "processKey", required = true) String processKey) {
        List<OnlCgformWorkflowNode> list = nodePermissionService.getByProcessKey(processKey);
        return Result.OK(list);
    }

    /**
     * 🎯 获取表单字段列表（用于权限配置界面）
     */
    @AutoLog(value = "获取表单字段列表")
    @Operation(summary = "获取表单字段列表", description = "获取表单字段列表用于权限配置")
    @GetMapping(value = "/getFormFields")
    public Result<List<Map<String, Object>>> getFormFields(@RequestParam(name = "formId", required = true) String formId) {
        List<Map<String, Object>> fields = nodePermissionService.getFormFieldsForPermissionConfig(formId);
        return Result.OK(fields);
    }

    /**
     * 🎯 保存节点权限配置
     */
    @AutoLog(value = "保存节点权限配置")
    @Operation(summary = "保存节点权限配置", description = "保存或更新节点的字段权限配置")
    @PostMapping(value = "/saveNodePermission")
    public Result<String> saveNodePermission(@RequestBody Map<String, Object> params) {
        try {
            String formId = (String) params.get("formId");
            String processKey = (String) params.get("processKey");
            String nodeId = (String) params.get("nodeId");
            String nodeName = (String) params.get("nodeName");

            if (oConvertUtils.isEmpty(formId) || oConvertUtils.isEmpty(nodeId)) {
                return Result.error("表单ID和节点ID不能为空");
            }

            // 解析权限配置
            FormPermissionConfig permissionConfig = parsePermissionConfig(params);

            // 保存配置
            nodePermissionService.saveOrUpdateNodePermission(formId, processKey, nodeId, nodeName, permissionConfig);

            return Result.OK("保存成功！");

        } catch (Exception e) {
            log.error("保存节点权限配置失败", e);
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 🎯 批量保存节点权限配置
     */
    @AutoLog(value = "批量保存节点权限配置")
    @Operation(summary = "批量保存节点权限配置", description = "批量保存多个节点的权限配置")
    @PostMapping(value = "/batchSaveNodePermissions")
    public Result<String> batchSaveNodePermissions(@RequestBody Map<String, Object> params) {
        try {
            String formId = (String) params.get("formId");
            String processKey = (String) params.get("processKey");
            @SuppressWarnings("unchecked")
            Map<String, Object> nodePermissions = (Map<String, Object>) params.get("nodePermissions");

            if (oConvertUtils.isEmpty(formId) || nodePermissions == null || nodePermissions.isEmpty()) {
                return Result.error("表单ID和节点权限配置不能为空");
            }

            // 解析所有节点的权限配置
            Map<String, FormPermissionConfig> permissionConfigs = new HashMap<>();
            for (Map.Entry<String, Object> entry : nodePermissions.entrySet()) {
                String nodeId = entry.getKey();
                @SuppressWarnings("unchecked")
                Map<String, Object> nodeConfig = (Map<String, Object>) entry.getValue();
                FormPermissionConfig config = parsePermissionConfig(nodeConfig);
                permissionConfigs.put(nodeId, config);
            }

            // 批量保存
            nodePermissionService.batchSaveNodePermissions(formId, processKey, permissionConfigs);

            return Result.OK("批量保存成功！");

        } catch (Exception e) {
            log.error("批量保存节点权限配置失败", e);
            return Result.error("批量保存失败：" + e.getMessage());
        }
    }

    /**
     * 🎯 复制权限配置到新版本
     */
    @AutoLog(value = "复制权限配置到新版本")
    @Operation(summary = "复制权限配置到新版本", description = "将权限配置复制到新版本流程")
    @PostMapping(value = "/copyToNewVersion")
    public Result<String> copyToNewVersion(@RequestParam(name = "sourceProcessKey", required = true) String sourceProcessKey,
                                          @RequestParam(name = "targetProcessKey", required = true) String targetProcessKey) {
        try {
            nodePermissionService.copyPermissionsToNewVersion(sourceProcessKey, targetProcessKey);
            return Result.OK("复制成功！");
        } catch (Exception e) {
            log.error("复制权限配置失败", e);
            return Result.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 🎯 生成智能默认权限配置预览
     */
    @AutoLog(value = "生成智能默认权限配置")
    @Operation(summary = "生成智能默认权限配置", description = "为指定节点生成智能默认权限配置预览")
    @GetMapping(value = "/generateDefaultPermission")
    public Result<Map<String, Object>> generateDefaultPermission(@RequestParam(name = "formId", required = true) String formId,
                                                                 @RequestParam(name = "nodeId", required = true) String nodeId) {
        try {
            // 这里需要调用智能默认策略
            // 由于DefaultFieldPermissionStrategy在engine中使用，这里提供一个简化版本
            Map<String, Object> result = new HashMap<>();
            result.put("formId", formId);
            result.put("nodeId", nodeId);
            result.put("message", "智能默认权限配置功能正在开发中");
            
            return Result.OK(result);
            
        } catch (Exception e) {
            log.error("生成智能默认权限配置失败", e);
            return Result.error("生成失败：" + e.getMessage());
        }
    }

    // =============== 辅助方法 ===============

    /**
     * 🎯 解析权限配置参数
     */
    @SuppressWarnings("unchecked")
    private FormPermissionConfig parsePermissionConfig(Map<String, Object> params) {
        FormPermissionConfig config = new FormPermissionConfig();

        // 解析各种权限字段列表
        Object editableFields = params.get("editableFields");
        if (editableFields instanceof List) {
            config.setEditableFields((List<String>) editableFields);
        } else if (editableFields instanceof String) {
            config.setEditableFields(JSON.parseArray((String) editableFields, String.class));
        }

        Object readonlyFields = params.get("readonlyFields");
        if (readonlyFields instanceof List) {
            config.setReadonlyFields((List<String>) readonlyFields);
        } else if (readonlyFields instanceof String) {
            config.setReadonlyFields(JSON.parseArray((String) readonlyFields, String.class));
        }

        Object hiddenFields = params.get("hiddenFields");
        if (hiddenFields instanceof List) {
            config.setHiddenFields((List<String>) hiddenFields);
        } else if (hiddenFields instanceof String) {
            config.setHiddenFields(JSON.parseArray((String) hiddenFields, String.class));
        }

        Object requiredFields = params.get("requiredFields");
        if (requiredFields instanceof List) {
            config.setRequiredFields((List<String>) requiredFields);
        } else if (requiredFields instanceof String) {
            config.setRequiredFields(JSON.parseArray((String) requiredFields, String.class));
        }

        // 解析其他配置
        String formMode = (String) params.get("formMode");
        if (oConvertUtils.isNotEmpty(formMode)) {
            config.setFormMode(formMode);
        }

        String conditionalPermissions = (String) params.get("conditionalPermissions");
        if (oConvertUtils.isNotEmpty(conditionalPermissions)) {
            config.setConditionalPermissions(conditionalPermissions);
        }

        return config;
    }
}