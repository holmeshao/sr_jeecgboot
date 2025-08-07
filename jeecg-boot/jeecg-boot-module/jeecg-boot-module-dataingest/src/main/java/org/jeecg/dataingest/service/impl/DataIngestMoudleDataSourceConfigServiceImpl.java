package org.jeecg.dataingest.service.impl;

import org.jeecg.dataingest.entity.DataIngestMoudleDataSourceConfig;
import org.jeecg.dataingest.mapper.DataIngestMoudleDataSourceConfigMapper;
import org.jeecg.dataingest.service.IDataIngestMoudleDataSourceConfigService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 数据接入模块的数据源配置
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Service
public class DataIngestMoudleDataSourceConfigServiceImpl extends ServiceImpl<DataIngestMoudleDataSourceConfigMapper, DataIngestMoudleDataSourceConfig> implements IDataIngestMoudleDataSourceConfigService {
	
	@Autowired
	private DataIngestMoudleDataSourceConfigMapper dataIngestMoudleDataSourceConfigMapper;
	
	@Override
	public List<DataIngestMoudleDataSourceConfig> selectByMainId(String mainId) {
		return dataIngestMoudleDataSourceConfigMapper.selectByMainId(mainId);
	}
}
