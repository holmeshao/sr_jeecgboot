package org.jeecg.dataingest.service.impl;

import org.jeecg.dataingest.entity.DataIngestMoudleFieldMapping;
import org.jeecg.dataingest.mapper.DataIngestMoudleFieldMappingMapper;
import org.jeecg.dataingest.service.IDataIngestMoudleFieldMappingService;
import org.springframework.stereotype.Service;
import java.util.List;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Description: 数据接入模块的字段映射表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Service
public class DataIngestMoudleFieldMappingServiceImpl extends ServiceImpl<DataIngestMoudleFieldMappingMapper, DataIngestMoudleFieldMapping> implements IDataIngestMoudleFieldMappingService {
	
	@Autowired
	private DataIngestMoudleFieldMappingMapper dataIngestMoudleFieldMappingMapper;
	
	@Override
	public List<DataIngestMoudleFieldMapping> selectByMainId(String mainId) {
		return dataIngestMoudleFieldMappingMapper.selectByMainId(mainId);
	}
}
