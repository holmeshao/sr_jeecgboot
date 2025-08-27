package org.jeecg.modules.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.jeecg.modules.workflow.entity.WorkflowModel;
import org.jeecg.modules.workflow.mapper.WorkflowModelMapper;
import org.jeecg.modules.workflow.service.IWorkflowModelService;

@Service
public class WorkflowModelServiceImpl extends ServiceImpl<WorkflowModelMapper, WorkflowModel>
        implements IWorkflowModelService {}


