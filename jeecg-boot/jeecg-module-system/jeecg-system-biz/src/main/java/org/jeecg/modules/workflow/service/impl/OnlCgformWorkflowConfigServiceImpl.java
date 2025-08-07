package org.jeecg.modules.workflow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.online.cgform.entity.OnlCgformHead;
import org.jeecg.modules.online.cgform.service.IOnlCgformHeadService;
import org.jeecg.modules.workflow.entity.OnlCgformWorkflowConfig;
import org.jeecg.modules.workflow.mapper.OnlCgformWorkflowConfigMapper;
import org.jeecg.modules.workflow.service.IOnlCgformWorkflowConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 🎯 工作流配置服务实现
 * 基于JeecgBoot标准ServiceImpl，遵循现有架构模式
 * 
 * @author JeecgBoot工作流集成
 * @version 1.0
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class OnlCgformWorkflowConfigServiceImpl 
    extends ServiceImpl<OnlCgformWorkflowConfigMapper, OnlCgformWorkflowConfig> 
    implements IOnlCgformWorkflowConfigService {

    @Autowired
    private OnlCgformWorkflowConfigMapper configMapper;

    @Autowired
    private IOnlCgformHeadService cgformHeadService;

    @Override
    public OnlCgformWorkflowConfig getByFormId(String formId) {
        if (oConvertUtils.isEmpty(formId)) {
            return null;
        }
        return configMapper.selectByFormId(formId);
    }

    @Override
    public OnlCgformWorkflowConfig getByTableName(String tableName) {
        if (oConvertUtils.isEmpty(tableName)) {
            return null;
        }
        
        // 通过tableName获取表单ID，然后查询工作流配置
        OnlCgformHead cgformHead = cgformHeadService.getOne(
            new LambdaQueryWrapper<OnlCgformHead>()
                .eq(OnlCgformHead::getTableName, tableName)
        );
        
        if (cgformHead == null) {
            log.warn("未找到表单配置: {}", tableName);
            return null;
        }
        
        return getByFormId(cgformHead.getId());
    }

    @Override
    public List<OnlCgformWorkflowConfig> getByProcessKey(String processKey) {
        if (oConvertUtils.isEmpty(processKey)) {
            return null;
        }
        
        return list(new LambdaQueryWrapper<OnlCgformWorkflowConfig>()
            .eq(OnlCgformWorkflowConfig::getProcessDefinitionKey, processKey)
            .eq(OnlCgformWorkflowConfig::getStatus, 1)
        );
    }

    @Override
    public boolean hasWorkflowConfig(String formId) {
        if (oConvertUtils.isEmpty(formId)) {
            return false;
        }
        return configMapper.checkWorkflowEnabled(formId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String id, Integer status) {
        if (oConvertUtils.isEmpty(id) || status == null) {
            return;
        }
        
        OnlCgformWorkflowConfig config = getById(id);
        if (config != null) {
            config.setStatus(status);
            updateById(config);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByFormId(String formId) {
        if (oConvertUtils.isEmpty(formId)) {
            return;
        }
        
        remove(new LambdaQueryWrapper<OnlCgformWorkflowConfig>()
            .eq(OnlCgformWorkflowConfig::getCgformHeadId, formId)
        );
    }

    @Override
    public List<OnlCgformWorkflowConfig> getActiveConfigs() {
        return list(new LambdaQueryWrapper<OnlCgformWorkflowConfig>()
            .eq(OnlCgformWorkflowConfig::getStatus, 1)
            .eq(OnlCgformWorkflowConfig::getWorkflowEnabled, 1)
            .orderByDesc(OnlCgformWorkflowConfig::getUpdateTime)
        );
    }
}