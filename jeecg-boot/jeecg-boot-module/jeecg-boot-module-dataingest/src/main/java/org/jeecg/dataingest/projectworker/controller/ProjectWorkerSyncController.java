package org.jeecg.dataingest.projectworker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.dataingest.projectworker.service.ProjectWorkerSyncService;
import org.springframework.web.bind.annotation.*;

/**
 * 工地项目人员信息同步控制器
 * 提供从宜昌实名制平台系统到D6C系统的数据同步接口
 */
@Slf4j
@RestController
@RequestMapping("/projectworker/sync")
@RequiredArgsConstructor
public class ProjectWorkerSyncController {

    private final ProjectWorkerSyncService syncService;

    /**
     * 同步工地项目人员信息
     * @param engId 工程ID
     * @param code 项目代码（可选，如果不传会从项目注册表查找）
     * @return 同步结果
     */
    @PostMapping("/sync")
    public Result<ProjectWorkerSyncService.SyncResult> sync(
            @RequestParam String engId,
            @RequestParam(required = false) String code) {
        try {
            log.info("开始同步工地项目人员信息，engId: {}, code: {}", engId, code);
            ProjectWorkerSyncService.SyncResult result = syncService.sync(engId, code);
            log.info("同步完成，结果: {}", result);
            return Result.OK(result);
        } catch (Exception e) {
            log.error("同步工地项目人员信息失败", e);
            return Result.error("同步失败: " + e.getMessage());
        }
    }

    /**
     * 获取同步状态（健康检查）
     */
    @GetMapping("/health")
    public Result<String> health() {
        return Result.OK("工地项目人员信息同步服务运行正常");
    }
}
