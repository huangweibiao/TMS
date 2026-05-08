package com.tms.service;

import com.tms.dto.TempHumidityDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.TempHumidity;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.DispatchRepository;
import com.tms.repository.TempHumidityRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.impl.TempHumidityServiceImpl;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TempHumidityServiceTest {

    @Mock
    private TempHumidityRepository tempHumidityRepository;

    @Mock
    private DispatchRepository dispatchRepository;

    @Mock
    private WaybillRepository waybillRepository;

    @InjectMocks
    private TempHumidityServiceImpl tempHumidityService;

    private TempHumidity tempHumidity;
    private TempHumidityDTO tempHumidityDTO;
    private Dispatch dispatch;
    private Waybill waybill;

    @BeforeEach
    void setUp() {
        dispatch = new Dispatch();
        dispatch.setId(1L);
        dispatch.setDispatchNo("DP202401010001");
        dispatch.setWaybillId(1L);

        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");
        waybill.setNeedTemperature(1);
        waybill.setMinTemp(new BigDecimal("-18.00"));
        waybill.setMaxTemp(new BigDecimal("-10.00"));

        tempHumidity = new TempHumidity();
        tempHumidity.setId(1L);
        tempHumidity.setDispatchId(1L);
        tempHumidity.setDeviceId("TEMP001");
        tempHumidity.setTemperature(new BigDecimal("-15.00"));
        tempHumidity.setHumidity(new BigDecimal("65.00"));
        tempHumidity.setRecordTime(LocalDateTime.now());

        tempHumidityDTO = new TempHumidityDTO();
        tempHumidityDTO.setId(1L);
        tempHumidityDTO.setDispatchId(1L);
        tempHumidityDTO.setDeviceId("TEMP001");
        tempHumidityDTO.setTemperature(new BigDecimal("-15.00"));
        tempHumidityDTO.setHumidity(new BigDecimal("65.00"));
        tempHumidityDTO.setRecordTime(LocalDateTime.now());
    }

    @Test
    void testFindById_Success() {
        when(tempHumidityRepository.findById(1L)).thenReturn(Optional.of(tempHumidity));

        Optional<TempHumidity> result = tempHumidityService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(tempHumidity.getId(), result.get().getId());
        verify(tempHumidityRepository).findById(1L);
    }

    @Test
    void testFindByDispatchId() {
        when(tempHumidityRepository.findByDispatchIdOrderByRecordTimeAsc(1L)).thenReturn(Arrays.asList(tempHumidity));

        List<TempHumidity> result = tempHumidityService.findByDispatchId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByDispatchIdAndTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        when(tempHumidityRepository.findByDispatchIdAndRecordTimeBetween(1L, startTime, endTime))
                .thenReturn(Arrays.asList(tempHumidity));

        List<TempHumidity> result = tempHumidityService.findByDispatchIdAndTimeRange(1L, startTime, endTime);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateRecord_Success() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(tempHumidityRepository.save(any(TempHumidity.class))).thenReturn(tempHumidity);

        TempHumidity result = tempHumidityService.createRecord(tempHumidityDTO);

        assertNotNull(result);
        assertEquals(tempHumidity.getTemperature(), result.getTemperature());
        assertEquals(tempHumidity.getHumidity(), result.getHumidity());
        verify(tempHumidityRepository).save(any(TempHumidity.class));
    }

    @Test
    void testCreateRecord_DispatchNotFound() {
        when(dispatchRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            tempHumidityService.createRecord(tempHumidityDTO);
        });

        assertEquals("调度单不存在", exception.getMessage());
    }

    @Test
    void testCreateRecord_TemperatureAbnormal() {
        tempHumidityDTO.setTemperature(new BigDecimal("-5.00"));
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(tempHumidityRepository.save(any(TempHumidity.class))).thenReturn(tempHumidity);

        TempHumidity result = tempHumidityService.createRecord(tempHumidityDTO);

        assertNotNull(result);
    }

    @Test
    void testBatchCreateRecords_Success() {
        TempHumidityDTO dto2 = new TempHumidityDTO();
        dto2.setDispatchId(1L);
        dto2.setDeviceId("TEMP001");
        dto2.setTemperature(new BigDecimal("-14.00"));
        dto2.setHumidity(new BigDecimal("66.00"));
        dto2.setRecordTime(LocalDateTime.now());

        List<TempHumidityDTO> dtoList = Arrays.asList(tempHumidityDTO, dto2);
        when(tempHumidityRepository.saveAll(any())).thenReturn(Arrays.asList(tempHumidity, tempHumidity));

        List<TempHumidity> result = tempHumidityService.batchCreateRecords(dtoList);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(tempHumidityRepository).saveAll(any());
    }

    @Test
    void testFindRecords() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<TempHumidity> page = new PageImpl<>(Arrays.asList(tempHumidity), pageable, 1);
        when(tempHumidityRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(page);

        Page<TempHumidity> result = tempHumidityService.findRecords(1L, "TEMP001", null, null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testFindRecords_WithTimeRange() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now();
        Pageable pageable = PageRequest.of(0, 10);
        Page<TempHumidity> page = new PageImpl<>(Arrays.asList(tempHumidity), pageable, 1);
        when(tempHumidityRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(page);

        Page<TempHumidity> result = tempHumidityService.findRecords(1L, "TEMP001", startTime, endTime, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testGetLatestRecord_Success() {
        when(tempHumidityRepository.findByDispatchIdOrderByRecordTimeAsc(1L)).thenReturn(Arrays.asList(tempHumidity));

        Optional<TempHumidity> result = tempHumidityService.getLatestRecord(1L);

        assertTrue(result.isPresent());
        assertEquals(tempHumidity.getId(), result.get().getId());
    }

    @Test
    void testGetLatestRecord_Empty() {
        when(tempHumidityRepository.findByDispatchIdOrderByRecordTimeAsc(1L)).thenReturn(Arrays.asList());

        Optional<TempHumidity> result = tempHumidityService.getLatestRecord(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testIsTemperatureAbnormal_TooLow() {
        boolean result = tempHumidityService.isTemperatureAbnormal(1L, new BigDecimal("-20.00"),
                new BigDecimal("-18.00"), new BigDecimal("-10.00"));

        assertTrue(result);
    }

    @Test
    void testIsTemperatureAbnormal_TooHigh() {
        boolean result = tempHumidityService.isTemperatureAbnormal(1L, new BigDecimal("-5.00"),
                new BigDecimal("-18.00"), new BigDecimal("-10.00"));

        assertTrue(result);
    }

    @Test
    void testIsTemperatureAbnormal_Normal() {
        boolean result = tempHumidityService.isTemperatureAbnormal(1L, new BigDecimal("-15.00"),
                new BigDecimal("-18.00"), new BigDecimal("-10.00"));

        assertFalse(result);
    }

    @Test
    void testIsTemperatureAbnormal_NullValues() {
        boolean result = tempHumidityService.isTemperatureAbnormal(1L, null,
                new BigDecimal("-18.00"), new BigDecimal("-10.00"));

        assertFalse(result);
    }

    @Test
    void testDeleteRecordsBefore() {
        LocalDateTime beforeTime = LocalDateTime.now().minusDays(30);
        when(tempHumidityRepository.findAll()).thenReturn(Arrays.asList(tempHumidity));

        long result = tempHumidityService.deleteRecordsBefore(beforeTime);

        assertEquals(0, result);
        verify(tempHumidityRepository).deleteAll(any());
    }
}
