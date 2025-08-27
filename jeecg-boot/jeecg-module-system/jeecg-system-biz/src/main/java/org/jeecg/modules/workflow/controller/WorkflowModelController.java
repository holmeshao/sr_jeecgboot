package org.jeecg.modules.workflow.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.modules.workflow.entity.WorkflowModel;
import org.jeecg.modules.workflow.entity.WorkflowModelVersion;
import org.jeecg.modules.workflow.mapper.WorkflowModelMapper;
import org.jeecg.modules.workflow.mapper.WorkflowModelVersionMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/workflow/model")
@RequiredArgsConstructor
@Tag(name = "工作流模型管理")
public class WorkflowModelController extends org.jeecg.common.system.base.controller.JeecgController<WorkflowModel, org.jeecg.modules.workflow.service.IWorkflowModelService> {

    private final org.jeecg.modules.workflow.service.IWorkflowModelService modelService;
    private final org.jeecg.modules.workflow.service.IWorkflowModelVersionService versionService;

    /** 保存或更新模型基本信息 */
    @AutoLog(value = "工作流-保存模型")
    @Operation(summary = "保存或更新模型")
    @RequiresPermissions("workflow:model:save")
    @PostMapping
    public Result<String> saveModel(@RequestBody WorkflowModel model){
        Date now = new Date();
        if(model.getId()==null){
            model.setCreateTime(now);
            model.setUpdateTime(now);
            model.setLatestVersion(0);
            modelService.save(model);
        }else{
            model.setUpdateTime(now);
            modelService.updateById(model);
        }
        return Result.OK(model.getId());
    }

    /** 新增模型版本（保存草稿XML） */
    @AutoLog(value = "工作流-新增模型版本")
    @Operation(summary = "新增模型版本")
    @RequiresPermissions("workflow:model:version:add")
    @PostMapping("/{modelId}/versions")
    public Result<Integer> createVersion(@PathVariable String modelId,@RequestBody WorkflowModelVersion body){
        WorkflowModel model = modelService.getById(modelId);
        if(model==null){
            return Result.error("模型不存在");
        }
        int next = (model.getLatestVersion()==null?0:model.getLatestVersion())+1;
        WorkflowModelVersion v = new WorkflowModelVersion();
        v.setModelId(modelId);
        v.setVersion(next);
        v.setXml(body.getXml());
        v.setComment(body.getComment());
        v.setCreateTime(new Date());
        versionService.save(v);
        model.setLatestVersion(next);
        model.setUpdateTime(new Date());
        modelService.updateById(model);
        return Result.OK(next);
    }

    /** 获取模型最新版本XML */
    @AutoLog(value = "工作流-获取模型最新XML")
    @Operation(summary = "获取模型最新XML")
    @RequiresPermissions("workflow:model:xml")
    @GetMapping("/{modelId}/xml")
    public Result<String> getLatestXml(@PathVariable String modelId){
        WorkflowModel model = modelService.getById(modelId);
        if(model==null||model.getLatestVersion()==null){
            return Result.error("模型不存在或无版本");
        }
        WorkflowModelVersion v = versionService.getOne(new LambdaQueryWrapper<WorkflowModelVersion>()
                .eq(WorkflowModelVersion::getModelId, modelId)
                .eq(WorkflowModelVersion::getVersion, model.getLatestVersion()));
        return v==null? Result.error("无XML"): Result.OK(v.getXml());
    }

    /** 获取模型全部版本 */
    @AutoLog(value = "工作流-获取模型版本列表")
    @Operation(summary = "获取模型版本列表")
    @RequiresPermissions("workflow:model:version:list")
    @GetMapping("/{modelId}/versions")
    public Result<List<WorkflowModelVersion>> listVersions(@PathVariable String modelId){
        List<WorkflowModelVersion> list = versionService.list(new LambdaQueryWrapper<WorkflowModelVersion>()
                .eq(WorkflowModelVersion::getModelId, modelId)
                .orderByDesc(WorkflowModelVersion::getVersion));
        return Result.OK(list);
    }

    /** 按Key查询模型（用于定义页反查模型） */
    @AutoLog(value = "工作流-按Key查询模型")
    @Operation(summary = "按Key查询模型")
    @RequiresPermissions("workflow:model:queryByKey")
    @GetMapping("/byKey")
    public Result<WorkflowModel> getByKey(@RequestParam String modelKey){
        WorkflowModel m = modelService.getOne(new LambdaQueryWrapper<WorkflowModel>()
                .eq(WorkflowModel::getModelKey, modelKey)
                .last("limit 1"));
        return m==null? Result.error("未找到模型"): Result.OK(m);
    }

    /** 模型列表（简单关键字查询） */
    @AutoLog(value = "工作流-模型列表")
    @Operation(summary = "模型列表（按关键字）")
    @RequiresPermissions("workflow:model:list")
    @GetMapping("/list")
    public Result<List<WorkflowModel>> list(@RequestParam(required = false) String keyword){
        LambdaQueryWrapper<WorkflowModel> qw = new LambdaQueryWrapper<>();
        if(keyword!=null && !keyword.isEmpty()){
            qw.like(WorkflowModel::getModelKey, keyword).or().like(WorkflowModel::getName, keyword);
        }
        qw.orderByDesc(WorkflowModel::getUpdateTime);
        return Result.OK(modelService.list(qw));
    }

    /** 从模型版本部署流程（可选：当前前端也支持直接上传XML复用部署接口） */
    @AutoLog(value = "工作流-从模型版本部署")
    @Operation(summary = "从模型版本部署")
    @RequiresPermissions("workflow:model:deploy")
    @PostMapping("/{modelId}/deploy")
    public Result<String> deployFromModel(@PathVariable String modelId, @RequestParam Integer version){
        // 此接口仅返回占位成功，实际部署走 /workflow/definition/deploy 上传XML 更通用
        // 如需服务端直接部署，可注入 RepositoryService 并调用 .createDeployment()
        return Result.OK("已接收部署请求，请使用前端上传XML方式或扩展服务端部署逻辑");
    }
}


