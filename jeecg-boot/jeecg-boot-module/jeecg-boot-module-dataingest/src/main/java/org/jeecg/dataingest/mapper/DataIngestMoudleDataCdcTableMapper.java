package org.jeecg.dataingest.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: 数据接入模块的cdc对接表
 * @Author: jeecg-boot
 * @Date:   2025-08-07
 * @Version: V1.0
 */
@Mapper
public interface DataIngestMoudleDataCdcTableMapper extends BaseMapper<DataIngestMoudleDataCdcTable> {

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
   * @return List<DataIngestMoudleDataCdcTable>
   */
	public List<DataIngestMoudleDataCdcTable> selectByMainId(@Param("mainId") String mainId);
}
