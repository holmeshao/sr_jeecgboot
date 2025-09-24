package org.jeecg.modules.workflow.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowConfig;
import org.jeecg.modules.workflow.service.IOnlCgformWorkflowConfigService;
import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 工作流配置（ui_mode / ui_schema_json）管理
 */
@Slf4j
@RestController
@RequestMapping("/workflow/config")
@Tag(name = "工作流配置管理")
public class WorkflowConfigController extends JeecgController<OnlCgformWorkflowConfig, IOnlCgformWorkflowConfigService> {

    @Autowired
    private OnlCgformWorkflowConfigMapper configMapper;

    @Data
    public static class UiSchemaDTO {
        private String cgformHeadId;
        private String processDefinitionKey;
        private String uiSchemaJson; // 原样存储（JSON字符串）
    }

    @Data
    public static class UiModeVO {
        private String uiMode; // SPLIT / INTEGRATED
    }

    @AutoLog("获取UI Schema")
    @Operation(summary = "获取UI Schema")
    @GetMapping("/uiSchema")
    public Result<String> getUiSchema(@RequestParam String cgformHeadId,
                                      @RequestParam String processDefinitionKey) {
        OnlCgformWorkflowConfig cfg = configMapper.selectOne(
            Wrappers.<OnlCgformWorkflowConfig>lambdaQuery()
                .eq(OnlCgformWorkflowConfig::getCgformHeadId, cgformHeadId)
                .eq(OnlCgformWorkflowConfig::getProcessDefinitionKey, processDefinitionKey)
                .last("limit 1")
        );
        if (cfg == null) {
            return Result.OK(null);
        }
        try {
            // 直接返回存储的 JSON 字符串
            java.lang.reflect.Field f = OnlCgformWorkflowConfig.class.getDeclaredField("uiSchemaJson");
            f.setAccessible(true);
            Object val = f.get(cfg);
            return Result.OK(val != null ? val.toString() : null);
        } catch (Exception e) {
            log.error("读取ui_schema_json失败", e);
            return Result.error("读取失败:" + e.getMessage());
        }
    }

    @AutoLog("保存UI Schema")
    @Operation(summary = "保存UI Schema")
    @PostMapping("/uiSchema")
    @RequiresPermissions("workflow:config:ui:save")
    public Result<Void> saveUiSchema(@RequestBody UiSchemaDTO dto) {
        try {
            OnlCgformWorkflowConfig cfg = configMapper.selectOne(
                Wrappers.<OnlCgformWorkflowConfig>lambdaQuery()
                    .eq(OnlCgformWorkflowConfig::getCgformHeadId, dto.getCgformHeadId())
                    .eq(OnlCgformWorkflowConfig::getProcessDefinitionKey, dto.getProcessDefinitionKey())
                    .last("limit 1")
            );
            if (cfg == null) {
                cfg = new OnlCgformWorkflowConfig();
                cfg.setCgformHeadId(dto.getCgformHeadId());
                cfg.setProcessDefinitionKey(dto.getProcessDefinitionKey());
                cfg.setStatus(1);
                // 反射写入 uiSchemaJson 以避免实体大改
                java.lang.reflect.Field f = OnlCgformWorkflowConfig.class.getDeclaredField("uiSchemaJson");
                f.setAccessible(true);
                f.set(cfg, dto.getUiSchemaJson());
                configMapper.insert(cfg);
            } else {
                java.lang.reflect.Field f = OnlCgformWorkflowConfig.class.getDeclaredField("uiSchemaJson");
                f.setAccessible(true);
                f.set(cfg, dto.getUiSchemaJson());
                configMapper.updateById(cfg);
            }
            return Result.OK(null);
        } catch (Exception e) {
            log.error("保存ui_schema_json失败", e);
            return Result.error("保存失败:" + e.getMessage());
        }
    }

    @AutoLog("获取UI模式")
    @Operation(summary = "获取UI模式")
    @GetMapping("/uiMode")
    public Result<UiModeVO> getUiMode(@RequestParam String cgformHeadId,
                                      @RequestParam(required = false) String processDefinitionKey) {
        OnlCgformWorkflowConfig cfg;
        if (org.jeecg.common.util.oConvertUtils.isNotEmpty(processDefinitionKey)) {
            cfg = configMapper.selectOne(
                Wrappers.<OnlCgformWorkflowConfig>lambdaQuery()
                    .eq(OnlCgformWorkflowConfig::getCgformHeadId, cgformHeadId)
                    .eq(OnlCgformWorkflowConfig::getProcessDefinitionKey, processDefinitionKey)
                    .eq(OnlCgformWorkflowConfig::getStatus, 1)
                    .last("limit 1")
            );
        } else {
            cfg = configMapper.selectOne(
                Wrappers.<OnlCgformWorkflowConfig>lambdaQuery()
                    .eq(OnlCgformWorkflowConfig::getCgformHeadId, cgformHeadId)
                    .eq(OnlCgformWorkflowConfig::getStatus, 1)
                    .last("limit 1")
            );
        }
        UiModeVO vo = new UiModeVO();
        vo.setUiMode(cfg != null && cfg.getUiMode() != null ? cfg.getUiMode() : "SPLIT");
        return Result.OK(vo);
    }
}


