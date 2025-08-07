package org.jeecg.dataingest.service.impl;

import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import org.jeecg.dataingest.entity.DataIngestMoudleDataSourceConfig;
import org.jeecg.dataingest.entity.DataIngestMoudleFieldMapping;
import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;
import org.jeecg.dataingest.mapper.DataIngestMoudleDataSourceConfigMapper;
import org.jeecg.dataingest.mapper.DataIngestMoudleFieldMappingMapper;
import org.jeecg.dataingest.mapper.DataIngestMoudleDataCdcTableMapper;
import org.jeecg.dataingest.mapper.DataIngestMoudleIngestTaskMapper;
import org.jeecg.dataingest.service.IDataIngestMoudleIngestTaskService;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.Serializable;
import java.util.List;
import java.util.Collection;

/**
 * @Description: 数据接入模块的任务主信息表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Service
public class DataIngestMoudleIngestTaskServiceImpl extends ServiceImpl<DataIngestMoudleIngestTaskMapper, DataIngestMoudleIngestTask> implements IDataIngestMoudleIngestTaskService {

	@Autowired
	private DataIngestMoudleIngestTaskMapper dataIngestMoudleIngestTaskMapper;
	@Autowired
	private DataIngestMoudleDataSourceConfigMapper dataIngestMoudleDataSourceConfigMapper;
	@Autowired
	private DataIngestMoudleFieldMappingMapper dataIngestMoudleFieldMappingMapper;
	@Autowired
	private DataIngestMoudleDataCdcTableMapper dataIngestMoudleDataCdcTableMapper;
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveMain(DataIngestMoudleIngestTask dataIngestMoudleIngestTask, List<DataIngestMoudleDataSourceConfig> dataIngestMoudleDataSourceConfigList,List<DataIngestMoudleFieldMapping> dataIngestMoudleFieldMappingList,List<DataIngestMoudleDataCdcTable> dataIngestMoudleDataCdcTableList) {
		dataIngestMoudleIngestTaskMapper.insert(dataIngestMoudleIngestTask);
		if(dataIngestMoudleDataSourceConfigList!=null && dataIngestMoudleDataSourceConfigList.size()>0) {
			for(DataIngestMoudleDataSourceConfig entity:dataIngestMoudleDataSourceConfigList) {
				//外键设置
				entity.setTaskId(dataIngestMoudleIngestTask.getId());
				dataIngestMoudleDataSourceConfigMapper.insert(entity);
			}
		}
		if(dataIngestMoudleFieldMappingList!=null && dataIngestMoudleFieldMappingList.size()>0) {
			for(DataIngestMoudleFieldMapping entity:dataIngestMoudleFieldMappingList) {
				//外键设置
				entity.setTaskId(dataIngestMoudleIngestTask.getId());
				dataIngestMoudleFieldMappingMapper.insert(entity);
			}
		}
		if(dataIngestMoudleDataCdcTableList!=null && dataIngestMoudleDataCdcTableList.size()>0) {
			for(DataIngestMoudleDataCdcTable entity:dataIngestMoudleDataCdcTableList) {
				//外键设置
				entity.setTaskId(dataIngestMoudleIngestTask.getId());
				dataIngestMoudleDataCdcTableMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void updateMain(DataIngestMoudleIngestTask dataIngestMoudleIngestTask,List<DataIngestMoudleDataSourceConfig> dataIngestMoudleDataSourceConfigList,List<DataIngestMoudleFieldMapping> dataIngestMoudleFieldMappingList,List<DataIngestMoudleDataCdcTable> dataIngestMoudleDataCdcTableList) {
		dataIngestMoudleIngestTaskMapper.updateById(dataIngestMoudleIngestTask);
		
		//1.先删除子表数据
		dataIngestMoudleDataSourceConfigMapper.deleteByMainId(dataIngestMoudleIngestTask.getId());
		dataIngestMoudleFieldMappingMapper.deleteByMainId(dataIngestMoudleIngestTask.getId());
		dataIngestMoudleDataCdcTableMapper.deleteByMainId(dataIngestMoudleIngestTask.getId());
		
		//2.子表数据重新插入
		if(dataIngestMoudleDataSourceConfigList!=null && dataIngestMoudleDataSourceConfigList.size()>0) {
			for(DataIngestMoudleDataSourceConfig entity:dataIngestMoudleDataSourceConfigList) {
				//外键设置
				entity.setTaskId(dataIngestMoudleIngestTask.getId());
				dataIngestMoudleDataSourceConfigMapper.insert(entity);
			}
		}
		if(dataIngestMoudleFieldMappingList!=null && dataIngestMoudleFieldMappingList.size()>0) {
			for(DataIngestMoudleFieldMapping entity:dataIngestMoudleFieldMappingList) {
				//外键设置
				entity.setTaskId(dataIngestMoudleIngestTask.getId());
				dataIngestMoudleFieldMappingMapper.insert(entity);
			}
		}
		if(dataIngestMoudleDataCdcTableList!=null && dataIngestMoudleDataCdcTableList.size()>0) {
			for(DataIngestMoudleDataCdcTable entity:dataIngestMoudleDataCdcTableList) {
				//外键设置
				entity.setTaskId(dataIngestMoudleIngestTask.getId());
				dataIngestMoudleDataCdcTableMapper.insert(entity);
			}
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delMain(String id) {
		dataIngestMoudleDataSourceConfigMapper.deleteByMainId(id);
		dataIngestMoudleFieldMappingMapper.deleteByMainId(id);
		dataIngestMoudleDataCdcTableMapper.deleteByMainId(id);
		dataIngestMoudleIngestTaskMapper.deleteById(id);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void delBatchMain(Collection<? extends Serializable> idList) {
		for(Serializable id:idList) {
			dataIngestMoudleDataSourceConfigMapper.deleteByMainId(id.toString());
			dataIngestMoudleFieldMappingMapper.deleteByMainId(id.toString());
			dataIngestMoudleDataCdcTableMapper.deleteByMainId(id.toString());
			dataIngestMoudleIngestTaskMapper.deleteById(id);
		}
	}
	
}
