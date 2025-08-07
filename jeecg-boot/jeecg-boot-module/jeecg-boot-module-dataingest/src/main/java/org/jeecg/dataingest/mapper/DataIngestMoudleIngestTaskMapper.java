package org.jeecg.dataingest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @Description: 数据接入模块的任务主信息表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Mapper
public interface DataIngestMoudleIngestTaskMapper extends BaseMapper<DataIngestMoudleIngestTask> {

}
