package com.tms.service;

import com.tms.dto.VehicleDTO;
import com.tms.common.PageResult;
import com.tms.entity.Carrier;
import com.tms.entity.Vehicle;
import com.tms.exception.BusinessException;
import com.tms.repository.CarrierRepository;
import com.tms.repository.VehicleRepository;
import com.tms.service.impl.VehicleServiceImpl;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle vehicle;
    private VehicleDTO vehicleDTO;
    private Carrier carrier;

    @BeforeEach
    void setUp() {
        vehicle = new Vehicle();
        vehicle.setId(1L);
        vehicle.setPlateNumber("京A12345");
        vehicle.setVehicleType("厢式货车");
        vehicle.setVehicleBrand("东风");
        vehicle.setLoadCapacity(new BigDecimal("5.00"));
        vehicle.setVolumeCapacity(new BigDecimal("20.00"));
        vehicle.setLength(new BigDecimal("6.00"));
        vehicle.setWidth(new BigDecimal("2.50"));
        vehicle.setHeight(new BigDecimal("2.50"));
        vehicle.setOwnerType(1);
        vehicle.setCarrierId(null);
        vehicle.setGpsDeviceId("GPS001");
        vehicle.setStatus(1);
        vehicle.setLastMaintenance(LocalDate.now());

        vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1L);
        vehicleDTO.setPlateNumber("京A12345");
        vehicleDTO.setVehicleType("厢式货车");
        vehicleDTO.setVehicleBrand("东风");
        vehicleDTO.setLoadCapacity(new BigDecimal("5.00"));
        vehicleDTO.setVolumeCapacity(new BigDecimal("20.00"));
        vehicleDTO.setLength(new BigDecimal("6.00"));
        vehicleDTO.setWidth(new BigDecimal("2.50"));
        vehicleDTO.setHeight(new BigDecimal("2.50"));
        vehicleDTO.setOwnerType(1);
        vehicleDTO.setCarrierId(null);
        vehicleDTO.setGpsDeviceId("GPS001");
        vehicleDTO.setStatus(1);
        vehicleDTO.setLastMaintenance(LocalDate.now());

        carrier = new Carrier();
        carrier.setId(1L);
        carrier.setCarrierCode("CA001");
        carrier.setCarrierName("测试承运商");
        carrier.setStatus(1);
    }

    @Test
    void testGetVehicleById_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        VehicleDTO result = vehicleService.getVehicleById(1L);

        assertNotNull(result);
        assertEquals(vehicle.getId(), result.getId());
        assertEquals(vehicle.getPlateNumber(), result.getPlateNumber());
        verify(vehicleRepository).findById(1L);
    }

    @Test
    void testGetVehicleById_NotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.getVehicleById(1L);
        });

        assertEquals("车辆不存在", exception.getMessage());
        verify(vehicleRepository).findById(1L);
    }

    @Test
    void testGetVehicleByPlateNumber_Success() {
        when(vehicleRepository.findByPlateNumber("京A12345")).thenReturn(Optional.of(vehicle));

        VehicleDTO result = vehicleService.getVehicleByPlateNumber("京A12345");

        assertNotNull(result);
        assertEquals(vehicle.getPlateNumber(), result.getPlateNumber());
        verify(vehicleRepository).findByPlateNumber("京A12345");
    }

    @Test
    void testGetVehicleByPlateNumber_NotFound() {
        when(vehicleRepository.findByPlateNumber("京A12345")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.getVehicleByPlateNumber("京A12345");
        });

        assertEquals("车辆不存在", exception.getMessage());
    }

    @Test
    void testCreateVehicle_Success() {
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleDTO result = vehicleService.createVehicle(vehicleDTO);

        assertNotNull(result);
        assertEquals(vehicle.getPlateNumber(), result.getPlateNumber());
        verify(vehicleRepository).existsByPlateNumber("京A12345");
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void testCreateVehicle_PlateNumberExists() {
        when(vehicleRepository.existsByPlateNumber("京A12345")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.createVehicle(vehicleDTO);
        });

        assertEquals("车牌号已存在", exception.getMessage());
        verify(vehicleRepository, never()).save(any());
    }

    @Test
    void testCreateVehicle_ExternalCarrier_Success() {
        vehicleDTO.setOwnerType(2);
        vehicleDTO.setCarrierId(1L);

        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleDTO result = vehicleService.createVehicle(vehicleDTO);

        assertNotNull(result);
        verify(carrierRepository).findById(1L);
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void testCreateVehicle_ExternalCarrier_MissingCarrierId() {
        vehicleDTO.setOwnerType(2);
        vehicleDTO.setCarrierId(null);


        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.createVehicle(vehicleDTO);
        });

        assertEquals("外协车辆必须选择承运商", exception.getMessage());
    }

    @Test
    void testCreateVehicle_ExternalCarrier_CarrierNotFound() {
        vehicleDTO.setOwnerType(2);
        vehicleDTO.setCarrierId(1L);

        when(carrierRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.createVehicle(vehicleDTO);
        });

        assertEquals("承运商不存在", exception.getMessage());
    }

    @Test
    void testCreateVehicle_ExternalCarrier_CarrierDisabled() {
        vehicleDTO.setOwnerType(2);
        vehicleDTO.setCarrierId(1L);
        carrier.setStatus(0);

        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.createVehicle(vehicleDTO);
        });

        assertEquals("承运商已停用，无法关联", exception.getMessage());
    }

    @Test
    void testUpdateVehicle_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(vehicle);

        VehicleDTO result = vehicleService.updateVehicle(1L, vehicleDTO);

        assertNotNull(result);
        verify(vehicleRepository).findById(1L);
        verify(vehicleRepository).save(any(Vehicle.class));
    }

    @Test
    void testUpdateVehicle_NotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.updateVehicle(1L, vehicleDTO);
        });

        assertEquals("车辆不存在", exception.getMessage());
    }

    @Test
    void testUpdateVehicle_PlateNumberExists() {
        Vehicle existingVehicle = new Vehicle();
        existingVehicle.setId(1L);
        existingVehicle.setPlateNumber("京A12345");

        vehicleDTO.setPlateNumber("京B67890");

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(existingVehicle));
        when(vehicleRepository.existsByPlateNumber("京B67890")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.updateVehicle(1L, vehicleDTO);
        });

        assertEquals("车牌号已存在", exception.getMessage());
    }

    @Test
    void testDeleteVehicle_Success() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        vehicleService.deleteVehicle(1L);

        verify(vehicleRepository).findById(1L);
        verify(vehicleRepository).deleteById(1L);
    }

    @Test
    void testDeleteVehicle_NotFound() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.deleteVehicle(1L);
        });

        assertEquals("车辆不存在", exception.getMessage());
    }

    @Test
    void testDeleteVehicle_ScrapStatus() {
        vehicle.setStatus(3);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            vehicleService.deleteVehicle(1L);
        });

        assertEquals("已报废车辆不能删除", exception.getMessage());
    }

    @Test
    void testGetVehicleList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Vehicle> page = new PageImpl<>(Arrays.asList(vehicle));
        when(vehicleRepository.findByPlateNumberContainingAndStatus(any(), any(), eq(pageable))).thenReturn(page);

        PageResult<VehicleDTO> result = vehicleService.getVehicleList("京A", 1, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
    }

    @Test
    void testGetAvailableVehicles() {
        when(vehicleRepository.findByStatus(1)).thenReturn(Arrays.asList(vehicle));

        List<VehicleDTO> result = vehicleService.getAvailableVehicles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(vehicle.getPlateNumber(), result.get(0).getPlateNumber());
    }
}
