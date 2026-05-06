package com.tms.repository;

import com.tms.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

/**
 * 操作日志Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface OperationLogRepository extends JpaRepositoryRepository<OperationLog, Long> {

    /**
     * 根据用户ID分页查询操作日志
     *
     * @param userId   用户ID
     * @param pageable 分页参数
     * @return 操作日志分页列表
     */
    Page Page<OperationLog> findByUserIdOrderByCreateTimeDesc(Long userId, Pageable pageable);

    /**
     * 根据时间范围分页查询操作日志
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param pageable  分页参数
     * @return 操作日志分页列表
     */
    Page Page<OperationLog> findByCreateTimeBetweenOrderByCreateTimeDesc(LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
