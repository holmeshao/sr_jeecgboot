package org.jeecg.modules.workflow.exception;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.exception.JeecgBootException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import javax.validation.ConstraintViolationException;

/**
 * 🎯 工作流模块统一异常处理器
 * 基于JeecgBoot异常处理模式，提供用户友好的错误信息
 * 
 * @author jeecg
 * @since 2024-12-25
 */
@Slf4j
@RestControllerAdvice("org.jeecg.modules.workflow")
public class WorkflowExceptionHandler {

    /**
     * 🎯 工作流业务异常处理
     */
    @ExceptionHandler(WorkflowBusinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleWorkflowBusinessException(WorkflowBusinessException e) {
        log.warn("工作流业务异常: {}", e.getMessage());
        return Result.error(e.getCode(), e.getMessage());
    }

    /**
     * 🎯 通用业务异常处理
     */
    @ExceptionHandler(JeecgBootException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleJeecgBootException(JeecgBootException e) {
        log.warn("业务异常: {}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 🎯 数据访问异常处理
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleDataAccessException(DataAccessException e) {
        log.error("数据访问异常", e);
        
        // 检查是否是重复键异常
        if (e instanceof DuplicateKeyException) {
            return Result.error("数据已存在，请检查配置是否重复");
        }
        
        return Result.error("数据操作失败，请稍后重试");
    }

    /**
     * 🎯 参数验证异常处理
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("参数验证失败: {}", e.getMessage());
        
        StringBuilder errorMsg = new StringBuilder("参数验证失败：");
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMsg.append(error.getField()).append("：").append(error.getDefaultMessage()).append("；");
        });
        
        return Result.error(errorMsg.toString());
    }

    /**
     * 🎯 约束违反异常处理
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleConstraintViolationException(ConstraintViolationException e) {
        log.warn("约束验证失败: {}", e.getMessage());
        return Result.error("数据验证失败：" + e.getMessage());
    }

    /**
     * 🎯 REST客户端异常处理（调用JeecgBoot API失败）
     */
    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleRestClientException(RestClientException e) {
        log.error("调用JeecgBoot API失败", e);
        
        String errorMsg = "系统服务调用失败";
        if (e.getMessage() != null) {
            if (e.getMessage().contains("404")) {
                errorMsg = "请求的资源不存在，请检查表单配置";
            } else if (e.getMessage().contains("403")) {
                errorMsg = "访问权限不足，请联系管理员";
            } else if (e.getMessage().contains("500")) {
                errorMsg = "服务器内部错误，请稍后重试";
            }
        }
        
        return Result.error(errorMsg);
    }

    /**
     * 🎯 流程引擎异常处理
     */
    @ExceptionHandler(org.flowable.common.engine.api.FlowableException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleFlowableException(org.flowable.common.engine.api.FlowableException e) {
        log.error("Flowable引擎异常", e);
        
        String errorMsg = "工作流引擎异常";
        if (e.getMessage() != null) {
            if (e.getMessage().contains("not found")) {
                errorMsg = "流程定义或实例不存在";
            } else if (e.getMessage().contains("suspended")) {
                errorMsg = "流程已被挂起，无法操作";
            } else if (e.getMessage().contains("completed")) {
                errorMsg = "流程已完成，无法再次操作";
            }
        }
        
        return Result.error(errorMsg);
    }

    /**
     * 🎯 非法参数异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("非法参数: {}", e.getMessage());
        return Result.error("参数错误：" + e.getMessage());
    }

    /**
     * 🎯 空指针异常处理
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleNullPointerException(NullPointerException e) {
        log.error("空指针异常", e);
        return Result.error("系统内部错误，请联系管理员");
    }

    /**
     * 🎯 通用异常处理
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result<?> handleGenericException(Exception e) {
        log.error("系统异常", e);
        return Result.error("系统异常，请联系管理员");
    }

    /**
     * 🎯 工作流权限异常处理
     */
    @ExceptionHandler(WorkflowPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result<?> handleWorkflowPermissionException(WorkflowPermissionException e) {
        log.warn("工作流权限异常: {}", e.getMessage());
        return Result.error(403, "权限不足：" + e.getMessage());
    }

    /**
     * 🎯 工作流状态异常处理
     */
    @ExceptionHandler(WorkflowStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<?> handleWorkflowStateException(WorkflowStateException e) {
        log.warn("工作流状态异常: {}", e.getMessage());
        return Result.error("状态错误：" + e.getMessage());
    }
}