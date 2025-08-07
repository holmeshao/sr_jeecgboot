package org.jeecg.dataingest.service.impl;

import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;
import org.jeecg.dataingest.mapper.DataIngestMoudleDataCdcTableMapper;
import org.jeecg.dataingest.service.IDataIngestMoudleDataCdcTableService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 数据接入模块的cdc对接表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Service
public class DataIngestMoudleDataCdcTableServiceImpl extends ServiceImpl<DataIngestMoudleDataCdcTableMapper, DataIngestMoudleDataCdcTable> implements IDataIngestMoudleDataCdcTableService {
	
	@Autowired
	private DataIngestMoudleDataCdcTableMapper dataIngestMoudleDataCdcTableMapper;
	
	@Override
	public List<DataIngestMoudleDataCdcTable> selectByMainId(String mainId) {
		return dataIngestMoudleDataCdcTableMapper.selectByMainId(mainId);
	}
}
