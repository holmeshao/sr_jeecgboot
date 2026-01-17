package org.jeecg.modules.online.ext.linkchain;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.online.cgform.enhance.CgformEnhanceJavaListInter;
import org.jeecg.modules.online.config.exception.BusinessException;
import org.jeecg.modules.workflow.service.LinkChainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 链式关联字段列表增强
 * 在列表查询时自动填充链式关联的派生字段值
 * 
 * Spring Bean 名称：linkTableChainListEnhance
 * 配置方式：在线表单 → Java增强 → 按钮：查询 → 事件：结束 → 类型：spring-key → 内容：linkTableChainListEnhance
 * 
 * @author jeecg
 * @since 2024-12-26
 */
@Slf4j
@Component("linkTableChainListEnhance")
public class LinkChainListEnhance implements CgformEnhanceJavaListInter {
    
    @Autowired
    private LinkChainService linkChainService;
    
    @Override
    public void execute(String tableName, List<Map<String, Object>> list) throws BusinessException {
        if (list == null || list.isEmpty()) {
            return;
        }
        
        try {
            // 批量填充链式关联字段
            linkChainService.fillLinkChainFields(tableName, list);
            log.debug("链式关联字段填充完成: tableName={}, count={}", tableName, list.size());
        } catch (Exception e) {
            log.error("链式关联字段填充失败: tableName={}", tableName, e);
            // 不抛出异常，避免影响列表查询
        }
    }
}
