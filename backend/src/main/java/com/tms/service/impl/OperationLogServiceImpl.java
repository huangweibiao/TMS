package com.tms.service.impl;

import com.tms.common.PageResult;
import com.tms.entity.OperationLog;
import com.tms.exception.BusinessException;
import com.tms.repository.OperationLogRepository;
import com.tms.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class OperationLogServiceImpl implements OperationLogService {

    private final OperationLogRepository operationLogRepository;

    @Autowired
    public OperationLogServiceImpl(OperationLogRepository operationLogRepository) {
        this.operationLogRepository = operationLogRepository;
    }

    @Override
    public PageResultResult<OperationLog> getOperationLogList(String username, String operationType,
                                                        LocalDateTime startTime, LocalDateTime endTime,
                                                        int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);

        Page Page<OperationLog> page;

        // 根据条件组合查询
        if (username != null && !username.isEmpty()) {
            // 根据用户名查询
            page = operationLogRepository.findByUsernameContainingOrderByCreateTimeDesc(username, pageable);
        } else if (startTime != null && endTime != null) {
            // 根据时间范围查询
            page = operationLogRepository.findByCreateTimeBetweenOrderByCreateTimeDesc(startTime, endTime, pageable);
        } else {
            // 查询全部
            page = operationLogRepository.findAllByOrderByCreateTimeDesc(pageable);
        }

        return new PageResult<>(pageNum, pageSize, page.getTotalElements(), page.getContent());
    }

    @Override
    public OperationLog getOperationLogById(Long id) {
        return operationLogRepository.findById(id)
                .orElseThrow(() -> new BusinessException("操作日志不存在"));
    }

    @Override
    @Transactional
    public void deleteOperationLog(Long id) {
        if (!operationLogRepository.existsById(id)) {
            throw new BusinessException("操作日志不存在");
        }
        operationLogRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteOperationLogs(List<Long> ids) {
        operationLogRepository.deleteAllById(ids);
    }

    @Override
    @Transactional
    public void clearOperationLogs() {
        operationLogRepository.deleteAll();
    }
}
