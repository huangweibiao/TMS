package com.tms.service;

import com.tms.entity.Dispatch;
import com.tms.entity.Driver;
import com.tms.entity.Vehicle;
import com.tms.entity.Waybill;
import com.tms.service.DispatchRuleEngine.DispatchPlan;
import com.tms.service.DispatchRuleEngine.RoutePlan;
import com.tms.service.DispatchRuleEngine.StrategyType;
import com.tms.repository.*;
import com.tms.service.impl.DispatchRuleEngineImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DispatchRuleEngineTest {

    @Mock
    private WaybillRepository waybillRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarrierRepository carrierRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private DispatchRuleEngineImpl dispatchRuleEngine;

    private Waybill waybill;
    private Vehicle vehicle;
    private Driver driver;

    @BeforeEach
    void setUp() {
        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");
        waybill.setTotalWeight(new BigDecimal("5.00"));
        waybill.setTotalVolume(new BigDecimal("10.00"));
        waybill.setShipperAddress("北京市");
        waybill.setConsigneeAddress("上海市");
        waybill.setNeedTemperature(0);
        waybill.setIsHazardous(0);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setPlateNumber("京A12345");
        vehicle.setVehicleType("厢式货车");
        vehicle.setLoadCapacity(new BigDecimal("10.00"));
        vehicle.setVolumeCapacity(new BigDecimal("20.00"));
        vehicle.setStatus(1);
        vehicle.setCarrierId(1L);

        driver = new Driver();
        driver.setId(1L);
        driver.setDriverName("张三");
        driver.setLicenseType("A1");
        driver.setStatus(1);
        driver.setVehicleId(1L);
    }

    @Test
    void testAssignVehicle_Success() {
        when(vehicleRepository.findByStatus(1)).thenReturn(Arrays.asList(vehicle));
        when(driverRepository.findByStatus(1)).thenReturn(Arrays.asList(driver));

        DispatchPlan result = dispatchRuleEngine.assignVehicle(waybill, StrategyType.BALANCE);

        assertNotNull(result);
        assertEquals(waybill.getId(), result.getWaybillId());
        assertEquals(vehicle.getId(), result.getVehicleId());
        assertEquals(driver.getId(), result.getDriverId());
    }

    @Test
    void testAssignVehicle_NoAvailableVehicles() {
        when(vehicleRepository.findByStatus(1)).thenReturn(Arrays.asList());

        DispatchPlan result = dispatchRuleEngine.assignVehicle(waybill, StrategyType.BALANCE);

        assertNull(result);
    }

    @Test
    void testAssignVehicle_NoAvailableDrivers() {
        when(vehicleRepository.findByStatus(1)).thenReturn(Arrays.asList(vehicle));
        when(driverRepository.findByStatus(1)).thenReturn(Arrays.asList());

        DispatchPlan result = dispatchRuleEngine.assignVehicle(waybill, StrategyType.BALANCE);

        assertNull(result);
    }

    @Test
    void testAssignVehicle_VehicleOverweight() {
        waybill.setTotalWeight(new BigDecimal("15.00"));
        when(vehicleRepository.findByStatus(1)).thenReturn(Arrays.asList(vehicle));

        DispatchPlan result = dispatchRuleEngine.assignVehicle(waybill, StrategyType.BALANCE);

        assertNull(result);
    }

    @Test
    void testBatchAssignVehicles() {
        Waybill waybill2 = new Waybill();
        waybill2.setId(2L);
        waybill2.setWaybillNo("WB202401010002");
        waybill2.setTotalWeight(new BigDecimal("3.00"));
        waybill2.setTotalVolume(new BigDecimal("5.00"));
        waybill2.setShipperAddress("北京市");
        waybill2.setConsigneeAddress("天津市");

        when(vehicleRepository.findByStatus(1)).thenReturn(Arrays.asList(vehicle));
        when(driverRepository.findByStatus(1)).thenReturn(Arrays.asList(driver));

        List<DispatchPlan> result = dispatchRuleEngine.batchAssignVehicles(Arrays.asList(waybill, waybill2), StrategyType.BALANCE);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testPlanRoute() {
        List<String> waypoints = Arrays.asList("天津市");

        RoutePlan result = dispatchRuleEngine.planRoute("北京市", "上海市", waypoints);

        assertNotNull(result);
        assertEquals("北京市", result.getStartAddress());
        assertEquals("上海市", result.getEndAddress());
        assertNotNull(result.getTotalDistance());
        assertTrue(result.getEstimatedTime() > 0);
        assertFalse(result.getSegments().isEmpty());
    }

    @Test
    void testPlanRoute_NoWaypoints() {
        RoutePlan result = dispatchRuleEngine.planRoute("北京市", "上海市", null);

        assertNotNull(result);
        assertEquals(1, result.getSegments().size());
    }

    @Test
    void testCalculateETA() {
        int result = dispatchRuleEngine.calculateETA("北京市", "上海市", new BigDecimal("300"), "普通");

        assertTrue(result > 0);
    }

    @Test
    void testCalculateETA_ColdChain() {
        int result = dispatchRuleEngine.calculateETA("北京市", "上海市", new BigDecimal("300"), "冷藏");

        assertTrue(result > 0);
    }

    @Test
    void testCalculateETA_Hazardous() {
        int result = dispatchRuleEngine.calculateETA("北京市", "上海市", new BigDecimal("300"), "危险品");

        assertTrue(result > 0);
    }

    @Test
    void testCalculateMatchScore_PerfectMatch() {
        int score = dispatchRuleEngine.calculateMatchScore(waybill, vehicle, driver);

        assertTrue(score > 0);
        assertTrue(score <= 100);
    }

    @Test
    void testCalculateMatchScore_ColdChain() {
        waybill.setNeedTemperature(1);
        vehicle.setVehicleType("冷藏");

        int score = dispatchRuleEngine.calculateMatchScore(waybill, vehicle, driver);

        assertTrue(score > 0);
    }

    @Test
    void testCalculateMatchScore_Hazardous() {
        waybill.setIsHazardous(1);
        vehicle.setVehicleType("危险品");

        int score = dispatchRuleEngine.calculateMatchScore(waybill, vehicle, driver);

        assertTrue(score > 0);
    }

    @Test
    void testIsVehicleSuitable_Normal() {
        boolean result = dispatchRuleEngine.isVehicleSuitable(waybill, vehicle);

        assertTrue(result);
    }

    @Test
    void testIsVehicleSuitable_Overweight() {
        waybill.setTotalWeight(new BigDecimal("15.00"));

        boolean result = dispatchRuleEngine.isVehicleSuitable(waybill, vehicle);

        assertFalse(result);
    }

    @Test
    void testIsVehicleSuitable_OverVolume() {
        waybill.setTotalVolume(new BigDecimal("25.00"));

        boolean result = dispatchRuleEngine.isVehicleSuitable(waybill, vehicle);

        assertFalse(result);
    }

    @Test
    void testIsVehicleSuitable_ColdChainRequired() {
        waybill.setNeedTemperature(1);

        boolean result = dispatchRuleEngine.isVehicleSuitable(waybill, vehicle);

        assertFalse(result);
    }

    @Test
    void testIsVehicleSuitable_HazardousRequired() {
        waybill.setIsHazardous(1);

        boolean result = dispatchRuleEngine.isVehicleSuitable(waybill, vehicle);

        assertFalse(result);
    }
}
