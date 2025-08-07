package org.jeecg.dataingest.service;

import com.alibaba.fastjson2.JSONObject;
import org.jeecg.dataingest.entity.DataIngestMoudleDataCdcTable;

/**
 * NiFi通知服务接口
 * @Description: 负责向NiFi发送CDC数据变更通知
 * @Author: jeecg-boot
 * @Date: 2025-01-01
 * @Version: V1.0
 */
public interface INiFiNotificationService {

    /**
     * 触发NiFi处理器
     * @param processorId NiFi处理器ID
     * @param notificationData 通知数据
     * @return 是否成功
     */
    boolean triggerNiFiProcessor(String processorId, JSONObject notificationData);

    /**
     * 触发DWD层处理
     * @param cdcConfig CDC表配置
     * @param changeData 变更数据
     * @return 是否成功
     */
    boolean triggerDwdProcessor(DataIngestMoudleDataCdcTable cdcConfig, JSONObject changeData);

    /**
     * 触发DWS层处理
     * @param cdcConfig CDC表配置
     * @param changeData 变更数据
     * @return 是否成功
     */
    boolean triggerDwsProcessor(DataIngestMoudleDataCdcTable cdcConfig, JSONObject changeData);

    /**
     * 批量触发处理器
     * @param processorId 处理器ID
     * @param batchData 批量数据
     * @return 是否成功
     */
    boolean triggerProcessorBatch(String processorId, JSONObject batchData);

    /**
     * 检查NiFi处理器状态
     * @param processorId 处理器ID
     * @return 处理器状态
     */
    String checkProcessorStatus(String processorId);

    /**
     * 根据业务域触发相关处理器
     * @param businessDomain 业务域
     * @param changeData 变更数据
     * @return 是否成功
     */
    boolean triggerByBusinessDomain(String businessDomain, JSONObject changeData);
} 