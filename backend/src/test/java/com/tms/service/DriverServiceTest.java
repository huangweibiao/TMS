package com.tms.service;

import com.tms.dto.DriverDTO;
import com.tms.common.PageResult;
import com.tms.entity.Carrier;
import com.tms.entity.Driver;
import com.tms.exception.BusinessException;
import com.tms.repository.CarrierRepository;
import com.tms.repository.DriverRepository;
import com.tms.service.impl.DriverServiceImpl;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DriverServiceTest {

    @Mock
    private DriverRepository driverRepository;

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private DriverServiceImpl driverService;

    private Driver driver;
    private DriverDTO driverDTO;
    private Carrier carrier;

    @BeforeEach
    void setUp() {
        driver = new Driver();
        driver.setId(1L);
        driver.setDriverName("张三");
        driver.setIdCard("110101199001011234");
        driver.setPhone("13800138000");
        driver.setLicenseType("A1");
        driver.setLicenseNo("110101199001011234");
        driver.setHireDate(LocalDate.now());
        driver.setCarrierId(1L);
        driver.setVehicleId(null);
        driver.setStatus(1);

        driverDTO = new DriverDTO();
        driverDTO.setId(1L);
        driverDTO.setDriverName("张三");
        driverDTO.setIdCard("110101199001011234");
        driverDTO.setPhone("13800138000");
        driverDTO.setLicenseType("A1");
        driverDTO.setLicenseNo("110101199001011234");
        driverDTO.setHireDate(LocalDate.now());
        driverDTO.setCarrierId(1L);
        driverDTO.setVehicleId(null);
        driverDTO.setStatus(1);

        carrier = new Carrier();
        carrier.setId(1L);
        carrier.setCarrierCode("CA001");
        carrier.setCarrierName("测试承运商");
        carrier.setStatus(1);
    }

    @Test
    void testGetDriverById_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));

        DriverDTO result = driverService.getDriverById(1L);

        assertNotNull(result);
        assertEquals(driver.getId(), result.getId());
        assertEquals(driver.getDriverName(), result.getDriverName());
        verify(driverRepository).findById(1L);
    }

    @Test
    void testGetDriverById_NotFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.getDriverById(1L);
        });

        assertEquals("司机不存在", exception.getMessage());
        verify(driverRepository).findById(1L);
    }

    @Test
    void testGetDriverByIdCard_Success() {
        when(driverRepository.findByIdCard("110101199001011234")).thenReturn(Optional.of(driver));
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));

        DriverDTO result = driverService.getDriverByIdCard("110101199001011234");

        assertNotNull(result);
        assertEquals(driver.getIdCard(), result.getIdCard());
        verify(driverRepository).findByIdCard("110101199001011234");
    }

    @Test
    void testGetDriverByIdCard_NotFound() {
        when(driverRepository.findByIdCard("110101199001011234")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.getDriverByIdCard("110101199001011234");
        });

        assertEquals("司机不存在", exception.getMessage());
    }

    @Test
    void testCreateDriver_Success() {
        when(driverRepository.existsByIdCard("110101199001011234")).thenReturn(false);
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        DriverDTO result = driverService.createDriver(driverDTO);

        assertNotNull(result);
        assertEquals(driver.getDriverName(), result.getDriverName());
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    void testCreateDriver_IdCardExists() {
        when(driverRepository.existsByIdCard("110101199001011234")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.createDriver(driverDTO);
        });

        assertEquals("身份证号已存在", exception.getMessage());
        verify(driverRepository, never()).save(any());
    }

    @Test
    void testCreateDriver_CarrierNotFound() {
        when(driverRepository.existsByIdCard("110101199001011234")).thenReturn(false);
        when(carrierRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.createDriver(driverDTO);
        });

        assertEquals("承运商不存在", exception.getMessage());
    }

    @Test
    void testCreateDriver_CarrierDisabled() {
        when(driverRepository.existsByIdCard("110101199001011234")).thenReturn(false);
        carrier.setStatus(0);
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.createDriver(driverDTO);
        });

        assertEquals("承运商已停用", exception.getMessage());
    }

    @Test
    void testCreateDriver_NoCarrier() {
        driverDTO.setCarrierId(null);
        when(driverRepository.existsByIdCard("110101199001011234")).thenReturn(false);
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        DriverDTO result = driverService.createDriver(driverDTO);

        assertNotNull(result);
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    void testUpdateDriver_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));
        when(driverRepository.save(any(Driver.class))).thenReturn(driver);

        DriverDTO result = driverService.updateDriver(1L, driverDTO);

        assertNotNull(result);
        verify(driverRepository).findById(1L);
        verify(driverRepository).save(any(Driver.class));
    }

    @Test
    void testUpdateDriver_NotFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.updateDriver(1L, driverDTO);
        });

        assertEquals("司机不存在", exception.getMessage());
    }

    @Test
    void testUpdateDriver_IdCardExists() {
        Driver existingDriver = new Driver();
        existingDriver.setId(1L);
        existingDriver.setIdCard("110101199001011111");

        when(driverRepository.findById(1L)).thenReturn(Optional.of(existingDriver));
        when(driverRepository.existsByIdCard("110101199001011234")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.updateDriver(1L, driverDTO);
        });

        assertEquals("身份证号已存在", exception.getMessage());
    }

    @Test
    void testDeleteDriver_Success() {
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        driverService.deleteDriver(1L);

        verify(driverRepository).findById(1L);
        verify(driverRepository).deleteById(1L);
    }

    @Test
    void testDeleteDriver_NotFound() {
        when(driverRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.deleteDriver(1L);
        });

        assertEquals("司机不存在", exception.getMessage());
    }

    @Test
    void testDeleteDriver_InTransit() {
        driver.setStatus(2);
        when(driverRepository.findById(1L)).thenReturn(Optional.of(driver));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            driverService.deleteDriver(1L);
        });

        assertEquals("司机在途，不能删除", exception.getMessage());
    }

    @Test
    void testGetDriverList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Driver> page = new PageImpl<>(Arrays.asList(driver));
        when(driverRepository.findByDriverNameContainingAndStatus(any(), any(), eq(pageable))).thenReturn(page);
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));

        PageResult<DriverDTO> result = driverService.getDriverList("张三", 1, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
    }

    @Test
    void testGetAvailableDrivers() {
        when(driverRepository.findByStatus(1)).thenReturn(Arrays.asList(driver));
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));

        List<DriverDTO> result = driverService.getAvailableDrivers();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(driver.getDriverName(), result.get(0).getDriverName());
    }
}
