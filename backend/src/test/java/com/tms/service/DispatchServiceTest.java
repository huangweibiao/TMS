package com.tms.service;

import com.tms.dto.DispatchDTO;
import com.tms.entity.Dispatch;
import com.tms.entity.Driver;
import com.tms.entity.Vehicle;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.*;
import com.tms.service.impl.DispatchServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 调度服务单元测试
 *
 * @author TMS Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class DispatchServiceTest {

    @Mock
    private DispatchRepository dispatchRepository;

    @Mock
    private WaybillRepository waybillRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private DispatchServiceImpl dispatchService;

    private Waybill waybill;
    private Vehicle vehicle;
    private Driver driver;
    private Dispatch dispatch;
    private DispatchDTO dispatchDTO;

    @BeforeEach
    void setUp() {
        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");
        waybill.setWaybillStatus(1);

        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setPlateNumber("京A12345");
        vehicle.setStatus(1);
        vehicle.setCarrierId(1L);

        driver = new Driver();
        driver.setId(1L);
        driver.setDriverName("张三");
        driver.setStatus(1);

        dispatch = new Dispatch();
        dispatch.setId(1L);
        dispatch.setDispatchNo("DP202401010001");
        dispatch.setWaybillId(1L);
        dispatch.setVehicleId(1L);
        dispatch.setDriverId(1L);
        dispatch.setDispatchStatus(1);

        dispatchDTO = new DispatchDTO();
        dispatchDTO.setId(1L);
        dispatchDTO.setDispatchNo("DP202401010001");
        dispatchDTO.setWaybillId(1L);
        dispatchDTO.setVehicleId(1L);
        dispatchDTO.setDriverId(1L);
        dispatchDTO.setDispatchStatus(1);
    }

    @Test
    void testAssignDispatch_Success() {
        // 准备
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(dispatchRepository.findMaxDispatchNoByPrefix(any())).thenReturn(null);
        when(dispatchRepository.save(any(Dispatch.class))).thenReturn(dispatch);
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        // 执行
        DispatchDTO result = dispatchService.assignDispatch(1L, 1L, 1L, 1);

        // 验证
        assertNotNull(result);
        verify(dispatchRepository, times(1)).save(any(Dispatch.class));
        verify(waybillRepository, times(1)).save(any(Waybill.class));
    }

    @Test
    void testAssignDispatch_WaybillNotFound() {
        // 准备
        when(waybillRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            dispatchService.assignDispatch(1L, 1L, 1L, 1);
        });
    }

    @Test
    void testAssignDispatch_WaybillNotPending() {
        // 准备
        waybill.setWaybillStatus(2);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            dispatchService.assignDispatch(1L, 1L, 1L, 1);
        });
    }

    @Test
    void testAssignDispatch_VehicleNotAvailable() {
        // 准备
        vehicle.setStatus(2);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            dispatchService.assignDispatch(1L, 1L, 1L, 1);
        });
    }

    @Test
    void testGetDispatchById_Success() {
        // 准备
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        // 执行
        DispatchDTO result = dispatchService.getDispatchById(1L);

        // 验证
        assertNotNull(result);
        assertEquals("DP202401010001", result.getDispatchNo());
    }

    @Test
    void testGetDispatchById_NotFound() {
        // 准备
        when(dispatchRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            dispatchService.getDispatchById(1L);
        });
    }

    @Test
    void testStartDispatch_Success() {
        // 准备
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(dispatchRepository.save(any(Dispatch.class))).thenReturn(dispatch);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);

        // 执行
        dispatchService.startDispatch(1L);

        // 验证
        verify(dispatchRepository, times(1)).save(any(Dispatch.class));
    }

    @Test
    void testCompleteDispatch_Success() {
        // 准备
        dispatch.setDispatchStatus(2);
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(dispatchRepository.save(any(Dispatch.class))).thenReturn(dispatch);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        // 执行
        dispatchService.completeDispatch(1L);

        // 验证
        verify(dispatchRepository, times(1)).save(any(Dispatch.class));
    }

    @Test
    void testCancelDispatch_Success() {
        // 准备
        when(dispatchRepository.findById(1L)).thenReturn(Optional.of(dispatch));
        when(dispatchRepository.save(any(Dispatch.class))).thenReturn(dispatch);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        // 执行
        dispatchService.cancelDispatch(1L, "客户取消");

        // 验证
        verify(dispatchRepository, times(1)).save(any(Dispatch.class));
    }
}
