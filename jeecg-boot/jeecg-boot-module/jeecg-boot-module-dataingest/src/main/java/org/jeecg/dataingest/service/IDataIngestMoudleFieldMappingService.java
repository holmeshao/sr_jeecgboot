package org.jeecg.dataingest.service;

import org.jeecg.dataingest.entity.DataIngestMoudleFieldMapping;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;

/**
 * @Description: 数据接入模块的字段映射表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
public interface IDataIngestMoudleFieldMappingService extends IService<DataIngestMoudleFieldMapping> {

	/**
	 * 通过主表id查询子表数据
	 *
	 * @param mainId 主表id
	 * @return List<DataIngestMoudleFieldMapping>
	 */
	public List<DataIngestMoudleFieldMapping> selectByMainId(String mainId);
}
