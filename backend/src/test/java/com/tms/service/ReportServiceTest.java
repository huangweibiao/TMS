package com.tms.service;

import com.tms.dto.ReportDTO;
import com.tms.entity.CostDetail;
import com.tms.entity.Dispatch;
import com.tms.entity.OnwayEvent;
import com.tms.entity.Vehicle;
import com.tms.entity.Waybill;
import com.tms.repository.*;
import com.tms.service.impl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private WaybillRepository waybillRepository;

    @Mock
    private CostDetailRepository costDetailRepository;

    @Mock
    private DispatchRepository dispatchRepository;

    @Mock
    private OnwayEventRepository onwayEventRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private Waybill waybill;
    private CostDetail costDetail;
    private Dispatch dispatch;
    private OnwayEvent onwayEvent;
    private Vehicle vehicle;

    @BeforeEach
    void setUp() {
        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");
        waybill.setCreateTime(LocalDateTime.now());

        costDetail = new CostDetail();
        costDetail.setId(1L);
        costDetail.setWaybillId(1L);
        costDetail.setCostType(1);
        costDetail.setDirection(1);
        costDetail.setAmount(new BigDecimal("1000.00"));
        costDetail.setCreateTime(LocalDateTime.now());

        dispatch = new Dispatch();
        dispatch.setId(1L);
        dispatch.setDispatchNo("DP202401010001");
        dispatch.setActualStartTime(LocalDateTime.now().minusHours(2));
        dispatch.setActualEndTime(LocalDateTime.now());
        dispatch.setActualDistance(new BigDecimal("100.00"));

        onwayEvent = new OnwayEvent();
        onwayEvent.setId(1L);
        onwayEvent.setDispatchId(1L);
        onwayEvent.setEventType(1);
        onwayEvent.setIsHandled(0);
        onwayEvent.setEventTime(LocalDateTime.now());

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setPlateNumber("京A12345");
        vehicle.setStatus(1);
    }

    @Test
    void testGetWaybillStatistics() {
        when(waybillRepository.findAll()).thenReturn(Arrays.asList(waybill));
        when(costDetailRepository.findByWaybillId(1L)).thenReturn(Arrays.asList(costDetail));

        List<ReportDTO> result = reportService.getWaybillStatistics(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.get(0).getWaybillCount());
    }

    @Test
    void testGetCostAnalysis() {
        when(costDetailRepository.findAll()).thenReturn(Arrays.asList(costDetail));

        Map<String, Object> result = reportService.getCostAnalysis(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertTrue(result.containsKey("costByType"));
        assertTrue(result.containsKey("totalIncome"));
        assertTrue(result.containsKey("totalExpense"));
        assertTrue(result.containsKey("profit"));
    }

    @Test
    void testGetTransportEfficiency() {
        when(dispatchRepository.findAll()).thenReturn(Arrays.asList(dispatch));

        Map<String, Object> result = reportService.getTransportEfficiency(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertTrue(result.containsKey("totalDispatches"));
        assertTrue(result.containsKey("totalDistance"));
        assertTrue(result.containsKey("avgTransportTime"));
        assertTrue(result.containsKey("completedCount"));
    }

    @Test
    void testGetVehicleUtilization() {
        when(vehicleRepository.count()).thenReturn(10L);
        when(vehicleRepository.findByStatus(1)).thenReturn(Arrays.asList(vehicle, vehicle, vehicle));
        when(vehicleRepository.findByStatus(2)).thenReturn(Arrays.asList(vehicle, vehicle));
        when(vehicleRepository.findByStatus(3)).thenReturn(Arrays.asList(vehicle));

        Map<String, Object> result = reportService.getVehicleUtilization(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertEquals(10L, result.get("totalVehicles"));
        assertEquals(3L, result.get("availableVehicles"));
        assertEquals(2L, result.get("inTransitVehicles"));
        assertEquals(1L, result.get("maintenanceVehicles"));
        assertTrue(result.containsKey("utilizationRate"));
    }

    @Test
    void testGetExceptionStatistics() {
        when(onwayEventRepository.findAll()).thenReturn(Arrays.asList(onwayEvent));

        Map<String, Object> result = reportService.getExceptionStatistics(LocalDate.now(), LocalDate.now());

        assertNotNull(result);
        assertTrue(result.containsKey("totalExceptions"));
        assertTrue(result.containsKey("exceptionByType"));
        assertTrue(result.containsKey("handledCount"));
        assertTrue(result.containsKey("unhandledCount"));
    }

    @Test
    void testGetKpiDashboard() {
        when(waybillRepository.findAll()).thenReturn(Arrays.asList(waybill));
        when(vehicleRepository.findByStatus(2)).thenReturn(Arrays.asList(vehicle));
        when(onwayEventRepository.findByIsHandledOrderByEventTimeDesc(eq(0), any()))
                .thenReturn(new PageImpl<>(Arrays.asList(onwayEvent)));
        when(costDetailRepository.findAll()).thenReturn(Arrays.asList(costDetail));

        Map<String, Object> result = reportService.getKpiDashboard();

        assertNotNull(result);
        assertTrue(result.containsKey("todayWaybillCount"));
        assertTrue(result.containsKey("monthWaybillCount"));
        assertTrue(result.containsKey("inTransitVehicles"));
        assertTrue(result.containsKey("pendingExceptions"));
        assertTrue(result.containsKey("monthIncome"));
    }
}
