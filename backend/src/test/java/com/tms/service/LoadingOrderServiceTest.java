package com.tms.service;

import com.tms.dto.LoadingOrderDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.LoadingOrder;
import com.tms.exception.BusinessException;
import com.tms.repository.DispatchRepository;
import com.tms.repository.LoadingOrderRepository;
import com.tms.repository.WarehouseRepository;
import com.tms.service.impl.LoadingOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoadingOrderServiceTest {

    @Mock
    private LoadingOrderRepository loadingOrderRepository;

    @Mock
    private DispatchRepository dispatchRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private LoadingOrderServiceImpl loadingOrderService;

    private LoadingOrder loadingOrder;
    private LoadingOrderDTO loadingOrderDTO;
    private Dispatch dispatch;

    @BeforeEach
    void setUp() {
        loadingOrder = new LoadingOrder();
        loadingOrder.setId(1L);
        loadingOrder.setLoadingNo("LH202401010001");
        loadingOrder.setDispatchId(1L);
        loadingOrder.setWarehouseId(1L);
        loadingOrder.setOperator("张三");
        loadingOrder.setStatus(1);
        loadingOrder.setRemark("测试装货单");

        loadingOrderDTO = new LoadingOrderDTO();
        loadingOrderDTO.setId(1L);
        loadingOrderDTO.setDispatchId(1L);
        loadingOrderDTO.setWarehouseId(1L);
        loadingOrderDTO.setOperator("张三");
        loadingOrderDTO.setRemark("测试装货单");

        dispatch = new Dispatch();
        dispatch.setId(1L);
        dispatch.setDispatchNo("DP202401010001");
    }

    @Test
    void testFindById_Success() {
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));

        Optional<LoadingOrder> result = loadingOrderService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(loadingOrder.getId(), result.get().getId());
        verify(loadingOrderRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<LoadingOrder> result = loadingOrderService.findById(1L);

        assertFalse(result.isPresent());
        verify(loadingOrderRepository).findById(1L);
    }

    @Test
    void testCreateLoadingOrder_Success() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(warehouseRepository.existsById(1L)).thenReturn(true);
        when(loadingOrderRepository.findMaxLoadingNoByPrefix(any())).thenReturn(null);
        when(loadingOrderRepository.save(any(LoadingOrder.class))).thenReturn(loadingOrder);

        LoadingOrder result = loadingOrderService.createLoadingOrder(loadingOrderDTO);

        assertNotNull(result);
        assertEquals(loadingOrder.getDispatchId(), result.getDispatchId());
        verify(dispatchRepository).findById(1L);
        verify(warehouseRepository).existsById(1L);
        verify(loadingOrderRepository).save(any(LoadingOrder.class));
    }

    @Test
    void testCreateLoadingOrder_DispatchNotFound() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loadingOrderService.createLoadingOrder(loadingOrderDTO);
        });

        assertEquals("调度单不存在", exception.getMessage());
        verify(dispatchRepository).findById(1L);
        verify(loadingOrderRepository, never()).save(any());
    }

    @Test
    void testCreateLoadingOrder_WarehouseNotFound() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(warehouseRepository.existsById(1L)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loadingOrderService.createLoadingOrder(loadingOrderDTO);
        });

        assertEquals("仓库不存在", exception.getMessage());
        verify(warehouseRepository).existsById(1L);
        verify(loadingOrderRepository, never()).save(any());
    }

    @Test
    void testUpdateLoadingOrder_Success() {
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));
        when(warehouseRepository.existsById(1L)).thenReturn(true);
        when(loadingOrderRepository.save(any(LoadingOrder.class))).thenReturn(loadingOrder);

        LoadingOrder result = loadingOrderService.updateLoadingOrder(1L, loadingOrderDTO);

        assertNotNull(result);
        verify(loadingOrderRepository).findById(1L);
        verify(warehouseRepository).existsById(1L);
        verify(loadingOrderRepository).save(any(LoadingOrder.class));
    }

    @Test
    void testUpdateLoadingOrder_NotFound() {
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loadingOrderService.updateLoadingOrder(1L, loadingOrderDTO);
        });

        assertEquals("装货单不存在", exception.getMessage());
    }

    @Test
    void testUpdateLoadingOrder_InvalidStatus() {
        loadingOrder.setStatus(2);
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loadingOrderService.updateLoadingOrder(1L, loadingOrderDTO);
        });

        assertEquals("只有待装货状态的装货单可以修改", exception.getMessage());
    }

    @Test
    void testDeleteLoadingOrder_Success() {
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));

        loadingOrderService.deleteLoadingOrder(1L);

        verify(loadingOrderRepository).findById(1L);
        verify(loadingOrderRepository).deleteById(1L);
    }

    @Test
    void testDeleteLoadingOrder_NotFound() {
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loadingOrderService.deleteLoadingOrder(1L);
        });

        assertEquals("装货单不存在", exception.getMessage());
    }

    @Test
    void testDeleteLoadingOrder_InvalidStatus() {
        loadingOrder.setStatus(2);
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loadingOrderService.deleteLoadingOrder(1L);
        });

        assertEquals("只有待装货状态的装货单可以删除", exception.getMessage());
    }

    @Test
    void testFindLoadingOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<LoadingOrder> page = new PageImpl<>(Arrays.asList(loadingOrder));
        when(loadingOrderRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<LoadingOrder> result = loadingOrderService.findLoadingOrders("LH", 1, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testStartLoading_Success() {
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));
        when(loadingOrderRepository.save(any(LoadingOrder.class))).thenReturn(loadingOrder);

        LoadingOrder result = loadingOrderService.startLoading(1L, "李四");

        assertNotNull(result);
        assertEquals(2, result.getStatus());
        verify(loadingOrderRepository).save(any(LoadingOrder.class));
    }

    @Test
    void testStartLoading_InvalidStatus() {
        loadingOrder.setStatus(2);
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loadingOrderService.startLoading(1L, "李四");
        });

        assertEquals("只有待装货状态的装货单可以开始装货", exception.getMessage());
    }

    @Test
    void testCompleteLoading_Success() {
        loadingOrder.setStatus(2);
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));
        when(loadingOrderRepository.save(any(LoadingOrder.class))).thenReturn(loadingOrder);

        LoadingOrder result = loadingOrderService.completeLoading(1L, "李四");

        assertNotNull(result);
        assertEquals(3, result.getStatus());
        verify(loadingOrderRepository).save(any(LoadingOrder.class));
    }

    @Test
    void testCompleteLoading_InvalidStatus() {
        when(loadingOrderRepository.findById(1L)).thenReturn(Optional.of(loadingOrder));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            loadingOrderService.completeLoading(1L, "李四");
        });

        assertEquals("只有装货中状态的装货单可以完成装货", exception.getMessage());
    }

    @Test
    void testFindByLoadingNo() {
        when(loadingOrderRepository.findByLoadingNo("LH202401010001")).thenReturn(Optional.of(loadingOrder));

        Optional<LoadingOrder> result = loadingOrderService.findByLoadingNo("LH202401010001");

        assertTrue(result.isPresent());
        assertEquals(loadingOrder.getLoadingNo(), result.get().getLoadingNo());
    }

    @Test
    void testFindByDispatchId() {
        when(loadingOrderRepository.findByDispatchId(1L)).thenReturn(Arrays.asList(loadingOrder));

        var result = loadingOrderService.findByDispatchId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
