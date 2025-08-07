package org.jeecg.dataingest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.dataingest.entity.DataIngestModuleIngestTask;

/**
 * 🎯 数据接入任务Mapper接口
 * 
 * @Description: 数据接入任务数据访问层
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Mapper
public interface DataIngestModuleIngestTaskMapper extends BaseMapper<DataIngestModuleIngestTask> {

    /**
     * 根据任务名称查询任务
     * @param taskName 任务名称
     * @return 任务信息
     */
    DataIngestModuleIngestTask selectByTaskName(String taskName);

    /**
     * 更新任务状态
     * @param id 任务ID
     * @param status 新状态
     * @return 影响行数
     */
    int updateTaskStatus(String id, Integer status);
}