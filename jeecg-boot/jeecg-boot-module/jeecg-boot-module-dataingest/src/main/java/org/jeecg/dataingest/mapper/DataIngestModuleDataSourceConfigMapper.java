package org.jeecg.dataingest.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.jeecg.dataingest.entity.DataIngestModuleDataSourceConfig;

import java.util.List;

/**
 * 🎯 数据接入模块数据源配置Mapper接口
 * 
 * @Description: 数据接入数据源配置数据访问层
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
@Mapper
public interface DataIngestModuleDataSourceConfigMapper extends BaseMapper<DataIngestModuleDataSourceConfig> {

    /**
     * 根据数据源名称查询配置
     * @param datasourceName 数据源名称
     * @return 数据源配置
     */
    @Select("SELECT * FROM data_ingest_module_datasource_config WHERE datasource_name = #{datasourceName} AND status = 1")
    DataIngestModuleDataSourceConfig selectByDatasourceName(@Param("datasourceName") String datasourceName);

    /**
     * 查询所有启用的数据源
     * @return 启用的数据源列表
     */
    @Select("SELECT * FROM data_ingest_module_datasource_config WHERE status = 1 ORDER BY create_time DESC")
    List<DataIngestModuleDataSourceConfig> selectEnabledDataSources();

    /**
     * 根据数据源类型查询配置
     * @param datasourceType 数据源类型
     * @return 数据源配置列表
     */
    @Select("SELECT * FROM data_ingest_module_datasource_config WHERE datasource_type = #{datasourceType} AND status = 1")
    List<DataIngestModuleDataSourceConfig> selectByDatasourceType(@Param("datasourceType") String datasourceType);

    /**
     * 更新数据源状态
     * @param id 数据源ID
     * @param status 新状态
     * @return 影响行数
     */
    @Select("UPDATE data_ingest_module_datasource_config SET status = #{status}, update_time = NOW() WHERE id = #{id}")
    int updateStatus(@Param("id") String id, @Param("status") Integer status);
}