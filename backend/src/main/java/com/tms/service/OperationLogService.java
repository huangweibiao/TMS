package com.tms.service;

import com.tms.common.PageResult;
import com.tms.entity.OperationLog;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志服务接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface OperationLogService {

    /**
     * 分页查询操作日志
     *
     * @param username   用户名
     * @param operationType 操作类型
     * @param startTime  开始时间
     * @param endTime    结束时间
     * @param pageNum    页码
     * @param pageSize   每页大小
     * @return 操作日志分页列表
     */
    PageResultResult<OperationLog> getOperationLogList(String username, String operationType,
                                                  LocalDateTime startTime, LocalDateTime endTime,
                                                  int pageNum, int pageSize);

    /**
     * 根据ID查询操作日志
     *
     * @param id 日志ID
     * @return 操作日志
     */
    OperationLog getOperationLogById(Long id);

    /**
     * 删除操作日志
     *
     * @param id 日志ID
     */
    void deleteOperationLog(Long id);

    /**
     * 批量删除操作日志
     *
     * @param ids 日志ID列表
     */
    void deleteOperationLogs(List<Long> ids);

    /**
     * 清空操作日志
     */
    void clearOperationLogs();
}
