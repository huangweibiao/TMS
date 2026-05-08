package com.tms.service;

import com.tms.common.PageResult;
import com.tms.entity.OperationLog;
import com.tms.exception.BusinessException;
import com.tms.repository.OperationLogRepository;
import com.tms.service.impl.OperationLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OperationLogServiceTest {

    @Mock
    private OperationLogRepository operationLogRepository;

    @InjectMocks
    private OperationLogServiceImpl operationLogService;

    private OperationLog operationLog;

    @BeforeEach
    void setUp() {
        operationLog = new OperationLog();
        operationLog.setId(1L);
        operationLog.setUsername("admin");
        operationLog.setOperationType("CREATE");
        operationLog.setOperationDesc("创建运单");
        operationLog.setRequestUrl("/api/waybill");
        operationLog.setIpAddress("127.0.0.1");
        operationLog.setCreateTime(LocalDateTime.now());
    }

    @Test
    void testGetOperationLogById_Success() {
        when(operationLogRepository.findById(1L)).thenReturn(Optional.of(operationLog));

        OperationLog result = operationLogService.getOperationLogById(1L);

        assertNotNull(result);
        assertEquals(operationLog.getId(), result.getId());
        assertEquals(operationLog.getUsername(), result.getUsername());
        verify(operationLogRepository).findById(1L);
    }

    @Test
    void testGetOperationLogById_NotFound() {
        when(operationLogRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            operationLogService.getOperationLogById(1L);
        });

        assertEquals("操作日志不存在", exception.getMessage());
    }

    @Test
    void testGetOperationLogList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OperationLog> page = new PageImpl<>(Arrays.asList(operationLog), pageable, 1);
        when(operationLogRepository.findAllByOrderByCreateTimeDesc(pageable)).thenReturn(page);

        PageResult<OperationLog> result = operationLogService.getOperationLogList(null, null, null, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
    }

    @Test
    void testGetOperationLogList_ByUsername() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OperationLog> page = new PageImpl<>(Arrays.asList(operationLog), pageable, 1);
        when(operationLogRepository.findByUsernameContainingOrderByCreateTimeDesc("admin", pageable)).thenReturn(page);

        PageResult<OperationLog> result = operationLogService.getOperationLogList("admin", null, null, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetOperationLogList_ByTimeRange() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OperationLog> page = new PageImpl<>(Arrays.asList(operationLog), pageable, 1);
        LocalDateTime startTime = LocalDateTime.now().minusDays(1);
        LocalDateTime endTime = LocalDateTime.now();
        when(operationLogRepository.findByCreateTimeBetweenOrderByCreateTimeDesc(startTime, endTime, pageable)).thenReturn(page);

        PageResult<OperationLog> result = operationLogService.getOperationLogList(null, null, startTime, endTime, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
    }

    @Test
    void testDeleteOperationLog_Success() {
        when(operationLogRepository.existsById(1L)).thenReturn(true);

        operationLogService.deleteOperationLog(1L);

        verify(operationLogRepository).existsById(1L);
        verify(operationLogRepository).deleteById(1L);
    }

    @Test
    void testDeleteOperationLog_NotFound() {
        when(operationLogRepository.existsById(1L)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            operationLogService.deleteOperationLog(1L);
        });

        assertEquals("操作日志不存在", exception.getMessage());
    }

    @Test
    void testDeleteOperationLogs() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        operationLogService.deleteOperationLogs(ids);

        verify(operationLogRepository).deleteAllById(ids);
    }

    @Test
    void testClearOperationLogs() {
        operationLogService.clearOperationLogs();

        verify(operationLogRepository).deleteAll();
    }
}
