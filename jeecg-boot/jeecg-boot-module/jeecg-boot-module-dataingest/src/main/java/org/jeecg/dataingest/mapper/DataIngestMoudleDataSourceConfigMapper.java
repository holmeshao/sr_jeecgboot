package org.jeecg.dataingest.mapper;

import java.util.List;
import org.jeecg.dataingest.entity.DataIngestMoudleDataSourceConfig;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 数据接入模块的数据源配置
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
public interface DataIngestMoudleDataSourceConfigMapper extends BaseMapper<DataIngestMoudleDataSourceConfig> {

	/**
	 * 通过主表id删除子表数据
	 *
	 * @param mainId 主表id
	 * @return boolean
	 */
	public boolean deleteByMainId(@Param("mainId") String mainId);

  /**
   * 通过主表id查询子表数据
   *
   * @param mainId 主表id
   * @return List<DataIngestMoudleDataSourceConfig>
   */
	public List<DataIngestMoudleDataSourceConfig> selectByMainId(@Param("mainId") String mainId);
}
