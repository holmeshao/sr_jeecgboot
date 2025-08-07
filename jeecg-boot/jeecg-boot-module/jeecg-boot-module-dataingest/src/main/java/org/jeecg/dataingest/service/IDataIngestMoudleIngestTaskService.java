package org.jeecg.dataingest.service;

import org.jeecg.dataingest.entity.DataIngestMoudleDataSourceConfig;
import org.jeecg.dataingest.entity.DataIngestMoudleFieldMapping;
import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;
import org.jeecg.dataingest.entity.DataIngestMoudleIngestTask;
import com.baomidou.mybatisplus.extension.service.IService;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Description: 数据接入模块的任务主信息表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
public interface IDataIngestMoudleIngestTaskService extends IService<DataIngestMoudleIngestTask> {

	/**
	 * 添加一对多
	 *
	 * @param dataIngestMoudleIngestTask
	 * @param dataIngestMoudleDataSourceConfigList
	 * @param dataIngestMoudleFieldMappingList
	 * @param dataIngestMoudleDataCdcTableList
	 */
	public void saveMain(DataIngestMoudleIngestTask dataIngestMoudleIngestTask,List<DataIngestMoudleDataSourceConfig> dataIngestMoudleDataSourceConfigList,List<DataIngestMoudleFieldMapping> dataIngestMoudleFieldMappingList,List<DataIngestMoudleDataCdcTable> dataIngestMoudleDataCdcTableList) ;
	
	/**
	 * 修改一对多
	 *
   * @param dataIngestMoudleIngestTask
   * @param dataIngestMoudleDataSourceConfigList
   * @param dataIngestMoudleFieldMappingList
   * @param dataIngestMoudleDataCdcTableList
	 */
	public void updateMain(DataIngestMoudleIngestTask dataIngestMoudleIngestTask,List<DataIngestMoudleDataSourceConfig> dataIngestMoudleDataSourceConfigList,List<DataIngestMoudleFieldMapping> dataIngestMoudleFieldMappingList,List<DataIngestMoudleDataCdcTable> dataIngestMoudleDataCdcTableList);
	
	/**
	 * 删除一对多
	 *
	 * @param id
	 */
	public void delMain (String id);
	
	/**
	 * 批量删除一对多
	 *
	 * @param idList
	 */
	public void delBatchMain (Collection<? extends Serializable> idList);
	
}
