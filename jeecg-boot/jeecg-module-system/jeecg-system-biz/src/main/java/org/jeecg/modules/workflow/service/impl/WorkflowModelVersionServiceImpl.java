package org.jeecg.modules.workflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.jeecg.modules.workflow.entity.WorkflowModelVersion;
import org.jeecg.modules.workflow.mapper.WorkflowModelVersionMapper;
import org.jeecg.modules.workflow.service.IWorkflowModelVersionService;

@Service
public class WorkflowModelVersionServiceImpl extends ServiceImpl<WorkflowModelVersionMapper, WorkflowModelVersion>
        implements IWorkflowModelVersionService {}


