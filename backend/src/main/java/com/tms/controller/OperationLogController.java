package com.tms.controller;

import com.tms.common.Result;
import com.tms.common.PageResult;
import com.tms.entity.OperationLog;
import com.tms.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志控制器
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/logs")
public class OperationLogController {

    private final OperationLogService operationLogService;

    @Autowired
    public OperationLogController(OperationLogService operationLogService) {
        this.operationLogService = operationLogService;
    }

    /**
     * 分页查询操作日志
     *
     * @param username      用户名
     * @param operationType 操作类型
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @param pageNum       页码
     * @param pageSize      每页大小
     * @return 操作日志分页列表
     */
    @GetMapping
    public Result<PageResult<OperationLog>> getOperationLogList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<OperationLog> result = operationLogService.getOperationLogList(
                username, operationType, startTime, endTime, pageNum, pageSize);
        return Result.success(result);
    }

    /**
     * 根据ID查询操作日志
     *
     * @param id 日志ID
     * @return 操作日志
     */
    @GetMapping("/{id}")
    public Result<OperationLog> getOperationLogById(@PathVariable Long id) {
        OperationLog result = operationLogService.getOperationLogById(id);
        return Result.success(result);
    }

    /**
     * 删除操作日志
     *
     * @param id 日志ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteOperationLog(@PathVariable Long id) {
        operationLogService.deleteOperationLog(id);
        return Result.success();
    }

    /**
     * 批量删除操作日志
     *
     * @param ids 日志ID列表
     * @return 删除结果
     */
    @DeleteMapping("/batch")
    public Result<Void> deleteOperationLogs(@RequestBody List<Long> ids) {
        operationLogService.deleteOperationLogs(ids);
        return Result.success();
    }

    /**
     * 清空操作日志
     *
     * @return 清空结果
     */
    @DeleteMapping("/clear")
    public Result<Void> clearOperationLogs() {
        operationLogService.clearOperationLogs();
        return Result.success();
    }
}

