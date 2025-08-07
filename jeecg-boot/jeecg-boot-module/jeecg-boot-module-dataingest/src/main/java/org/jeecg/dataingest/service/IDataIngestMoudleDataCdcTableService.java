package org.jeecg.dataingest.service;

import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 数据接入模块的cdc对接表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
public interface IDataIngestMoudleDataCdcTableService extends IService<DataIngestMoudleDataCdcTable> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<DataIngestMoudleDataCdcTable>
	 */
	public List<DataIngestMoudleDataCdcTable> selectByMainId(String mainId);
}
