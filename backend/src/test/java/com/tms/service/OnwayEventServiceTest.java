package com.tms.service;

import com.tms.dto.OnwayEventDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.OnwayEvent;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.DispatchRepository;
import com.tms.repository.OnwayEventRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.impl.OnwayEventServiceImpl;
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
public class OnwayEventServiceTest {

    @Mock
    private OnwayEventRepository onwayEventRepository;

    @Mock
    private DispatchRepository dispatchRepository;

    @Mock
    private WaybillRepository waybillRepository;

    @InjectMocks
    private OnwayEventServiceImpl onwayEventService;

    private OnwayEvent onwayEvent;
    private OnwayEventDTO onwayEventDTO;
    private Dispatch dispatch;
    private Waybill waybill;

    @BeforeEach
    void setUp() {
        dispatch = new Dispatch();
        dispatch.setId(1L);
        dispatch.setDispatchNo("DP202401010001");

        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");

        onwayEvent = new OnwayEvent();
        onwayEvent.setId(1L);
        onwayEvent.setDispatchId(1L);
        onwayEvent.setWaybillId(1L);
        onwayEvent.setEventType(1);
        onwayEvent.setEventLevel(1);
        onwayEvent.setEventContent("车辆故障");
        onwayEvent.setLocation("北京市朝阳区");
        onwayEvent.setEventTime(LocalDateTime.now());
        onwayEvent.setIsHandled(0);

        onwayEventDTO = new OnwayEventDTO();
        onwayEventDTO.setId(1L);
        onwayEventDTO.setDispatchId(1L);
        onwayEventDTO.setWaybillId(1L);
        onwayEventDTO.setEventType(1);
        onwayEventDTO.setEventLevel(1);
        onwayEventDTO.setEventContent("车辆故障");
        onwayEventDTO.setLocation("北京市朝阳区");
        onwayEventDTO.setEventTime(LocalDateTime.now());
    }

    @Test
    void testFindById_Success() {
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.of(onwayEvent));

        Optional<OnwayEvent> result = onwayEventService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(onwayEvent.getId(), result.get().getId());
        verify(onwayEventRepository).findById(1L);
    }

    @Test
    void testFindByDispatchId() {
        when(onwayEventRepository.findByDispatchIdOrderByEventTimeDesc(1L)).thenReturn(Arrays.asList(onwayEvent));

        List<OnwayEvent> result = onwayEventService.findByDispatchId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByDispatchIdAndIsHandled() {
        when(onwayEventRepository.findByDispatchIdAndIsHandled(1L, 0)).thenReturn(Arrays.asList(onwayEvent));

        List<OnwayEvent> result = onwayEventService.findByDispatchIdAndIsHandled(1L, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateEvent_Success() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(onwayEventRepository.save(any(OnwayEvent.class))).thenReturn(onwayEvent);

        OnwayEvent result = onwayEventService.createEvent(onwayEventDTO);

        assertNotNull(result);
        assertEquals(onwayEvent.getEventContent(), result.getEventContent());
        assertEquals(0, result.getIsHandled());
        verify(onwayEventRepository).save(any(OnwayEvent.class));
    }

    @Test
    void testCreateEvent_DispatchNotFound() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            onwayEventService.createEvent(onwayEventDTO);
        });

        assertEquals("调度单不存在", exception.getMessage());
    }

    @Test
    void testCreateEvent_WaybillNotFound() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(waybillRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            onwayEventService.createEvent(onwayEventDTO);
        });

        assertEquals("运单不存在", exception.getMessage());
    }

    @Test
    void testUpdateEvent_Success() {
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.of(onwayEvent));
        when(onwayEventRepository.save(any(OnwayEvent.class))).thenReturn(onwayEvent);

        OnwayEvent result = onwayEventService.updateEvent(1L, onwayEventDTO);

        assertNotNull(result);
        verify(onwayEventRepository).findById(1L);
        verify(onwayEventRepository).save(any(OnwayEvent.class));
    }

    @Test
    void testUpdateEvent_NotFound() {
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            onwayEventService.updateEvent(1L, onwayEventDTO);
        });

        assertEquals("事件不存在", exception.getMessage());
    }

    @Test
    void testUpdateEvent_AlreadyHandled() {
        onwayEvent.setIsHandled(1);
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.of(onwayEvent));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            onwayEventService.updateEvent(1L, onwayEventDTO);
        });

        assertEquals("已处理的事件不能修改", exception.getMessage());
    }

    @Test
    void testDeleteEvent_Success() {
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.of(onwayEvent));

        onwayEventService.deleteEvent(1L);

        verify(onwayEventRepository).findById(1L);
        verify(onwayEventRepository).deleteById(1L);
    }

    @Test
    void testDeleteEvent_NotFound() {
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            onwayEventService.deleteEvent(1L);
        });

        assertEquals("事件不存在", exception.getMessage());
    }

    @Test
    void testDeleteEvent_AlreadyHandled() {
        onwayEvent.setIsHandled(1);
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.of(onwayEvent));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            onwayEventService.deleteEvent(1L);
        });

        assertEquals("已处理的事件不能删除", exception.getMessage());
    }

    @Test
    void testHandleEvent_Success() {
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.of(onwayEvent));
        when(onwayEventRepository.save(any(OnwayEvent.class))).thenReturn(onwayEvent);

        OnwayEvent result = onwayEventService.handleEvent(1L, "已安排维修", "张三");

        assertNotNull(result);
        assertEquals(1, result.getIsHandled());
        assertEquals("已安排维修", result.getHandleResult());
        assertEquals("张三", result.getHandleBy());
        assertNotNull(result.getHandleTime());
    }

    @Test
    void testHandleEvent_NotFound() {
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            onwayEventService.handleEvent(1L, "已处理", "张三");
        });

        assertEquals("事件不存在", exception.getMessage());
    }

    @Test
    void testHandleEvent_AlreadyHandled() {
        onwayEvent.setIsHandled(1);
        when(onwayEventRepository.findById(1L)).thenReturn(Optional.of(onwayEvent));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            onwayEventService.handleEvent(1L, "已处理", "张三");
        });

        assertEquals("事件已处理", exception.getMessage());
    }

    @Test
    void testFindEvents() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OnwayEvent> page = new PageImpl<>(Arrays.asList(onwayEvent), pageable, 1);
        when(onwayEventRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(page);

        Page<OnwayEvent> result = onwayEventService.findEvents(1L, 1, 1, 0, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testCountUnhandledEvents() {
        Pageable pageable = Pageable.unpaged();
        Page<OnwayEvent> page = new PageImpl<>(Arrays.asList(onwayEvent), pageable, 5);
        when(onwayEventRepository.findByIsHandledOrderByEventTimeDesc(eq(0), any(Pageable.class))).thenReturn(page);

        long result = onwayEventService.countUnhandledEvents();

        assertEquals(5, result);
    }
}
