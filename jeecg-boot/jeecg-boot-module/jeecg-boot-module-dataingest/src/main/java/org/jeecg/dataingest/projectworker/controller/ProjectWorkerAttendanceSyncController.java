package org.jeecg.dataingest.projectworker.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.dataingest.projectworker.service.ProjectWorkerAttendanceSyncService;
import org.springframework.web.bind.annotation.*;

/**
 * 工地项目人员考勤同步控制器
 */
@Slf4j
@RestController
@RequestMapping("/projectworker/attendance")
@RequiredArgsConstructor
public class ProjectWorkerAttendanceSyncController {

    private final ProjectWorkerAttendanceSyncService svc;

    /**
     * 同步某人某日考勤
     */
    @PostMapping("/sync")
    public Result<ProjectWorkerAttendanceSyncService.SyncResult> sync(
            @RequestParam String engId,
            @RequestParam(required = false) String code,
            @RequestParam String idCardNumber,
            @RequestParam(required = false) String verifyDate
    ) {
        try {
            log.info("开始同步考勤 engId={}, code={}, idCardNumber={}, date={}", engId, code, idCardNumber, verifyDate);
            ProjectWorkerAttendanceSyncService.SyncResult r = svc.sync(engId, code, idCardNumber, verifyDate);
            return Result.OK(r);
        } catch (Exception e) {
            log.error("同步考勤失败", e);
            return Result.error("同步失败: " + e.getMessage());
        }
    }
}


