package org.jeecg.dataingest.service;

import org.jeecg.dataingest.entity.DataIngestMoudleDataSourceConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 数据接入模块的数据源配置
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
public interface IDataIngestMoudleDataSourceConfigService extends IService<DataIngestMoudleDataSourceConfig> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<DataIngestMoudleDataSourceConfig>
	 */
	public List<DataIngestMoudleDataSourceConfig> selectByMainId(String mainId);
}
