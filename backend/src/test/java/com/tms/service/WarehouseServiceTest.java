package com.tms.service;

import com.tms.dto.WarehouseDTO;
import com.tms.common.PageResult;
import com.tms.entity.Warehouse;
import com.tms.exception.BusinessException;
import com.tms.repository.WarehouseRepository;
import com.tms.service.impl.WarehouseServiceImpl;
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
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WarehouseServiceTest {

    @Mock
    private WarehouseRepository warehouseRepository;

    @InjectMocks
    private WarehouseServiceImpl warehouseService;

    private Warehouse warehouse;
    private WarehouseDTO warehouseDTO;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setWarehouseCode("WH001");
        warehouse.setWarehouseName("测试仓库");
        warehouse.setWarehouseType(1);
        warehouse.setProvince("北京市");
        warehouse.setCity("北京市");
        warehouse.setDistrict("朝阳区");
        warehouse.setAddress("测试地址");
        warehouse.setLongitude(new BigDecimal("116.4074"));
        warehouse.setLatitude(new BigDecimal("39.9042"));
        warehouse.setContactPerson("张三");
        warehouse.setContactPhone("13800138000");
        warehouse.setStatus(1);

        warehouseDTO = new WarehouseDTO();
        warehouseDTO.setId(1L);
        warehouseDTO.setWarehouseCode("WH001");
        warehouseDTO.setWarehouseName("测试仓库");
        warehouseDTO.setWarehouseType(1);
        warehouseDTO.setProvince("北京市");
        warehouseDTO.setCity("北京市");
        warehouseDTO.setDistrict("朝阳区");
        warehouseDTO.setAddress("测试地址");
        warehouseDTO.setLongitude(new BigDecimal("116.4074"));
        warehouseDTO.setLatitude(new BigDecimal("39.9042"));
        warehouseDTO.setContactPerson("张三");
        warehouseDTO.setContactPhone("13800138000");
        warehouseDTO.setStatus(1);
    }

    @Test
    void testGetWarehouseById_Success() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));

        WarehouseDTO result = warehouseService.getWarehouseById(1L);

        assertNotNull(result);
        assertEquals(warehouse.getId(), result.getId());
        assertEquals(warehouse.getWarehouseCode(), result.getWarehouseCode());
        verify(warehouseRepository).findById(1L);
    }

    @Test
    void testGetWarehouseById_NotFound() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            warehouseService.getWarehouseById(1L);
        });

        assertEquals("仓库不存在", exception.getMessage());
        verify(warehouseRepository).findById(1L);
    }

    @Test
    void testCreateWarehouse_Success() {
        when(warehouseRepository.existsByWarehouseCode("WH001")).thenReturn(false);
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        WarehouseDTO result = warehouseService.createWarehouse(warehouseDTO);

        assertNotNull(result);
        assertEquals(warehouse.getWarehouseCode(), result.getWarehouseCode());
        verify(warehouseRepository).existsByWarehouseCode("WH001");
        verify(warehouseRepository).save(any(Warehouse.class));
    }

    @Test
    void testCreateWarehouse_CodeAlreadyExists() {
        when(warehouseRepository.existsByWarehouseCode("WH001")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            warehouseService.createWarehouse(warehouseDTO);
        });

        assertEquals("仓库编码已存在", exception.getMessage());
        verify(warehouseRepository, never()).save(any());
    }

    @Test
    void testUpdateWarehouse_Success() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(warehouse));
        when(warehouseRepository.save(any(Warehouse.class))).thenReturn(warehouse);

        WarehouseDTO result = warehouseService.updateWarehouse(1L, warehouseDTO);

        assertNotNull(result);
        verify(warehouseRepository).findById(1L);
        verify(warehouseRepository).save(any(Warehouse.class));
    }

    @Test
    void testUpdateWarehouse_NotFound() {
        when(warehouseRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            warehouseService.updateWarehouse(1L, warehouseDTO);
        });

        assertEquals("仓库不存在", exception.getMessage());
    }

    @Test
    void testUpdateWarehouse_CodeAlreadyExists() {
        Warehouse existingWarehouse = new Warehouse();
        existingWarehouse.setId(1L);
        existingWarehouse.setWarehouseCode("WH002");

        warehouseDTO.setWarehouseCode("WH003");

        when(warehouseRepository.findById(1L)).thenReturn(Optional.of(existingWarehouse));
        when(warehouseRepository.existsByWarehouseCode("WH003")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            warehouseService.updateWarehouse(1L, warehouseDTO);
        });

        assertEquals("仓库编码已存在", exception.getMessage());
    }

    @Test
    void testDeleteWarehouse_Success() {
        when(warehouseRepository.existsById(1L)).thenReturn(true);

        warehouseService.deleteWarehouse(1L);

        verify(warehouseRepository).existsById(1L);
        verify(warehouseRepository).deleteById(1L);
    }

    @Test
    void testDeleteWarehouse_NotFound() {
        when(warehouseRepository.existsById(1L)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            warehouseService.deleteWarehouse(1L);
        });

        assertEquals("仓库不存在", exception.getMessage());
        verify(warehouseRepository, never()).deleteById(any());
    }

    @Test
    void testGetWarehouseList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Warehouse> page = new PageImpl<>(Arrays.asList(warehouse));
        when(warehouseRepository.findByWarehouseNameContainingAndStatus(any(), any(), eq(pageable))).thenReturn(page);

        PageResult<WarehouseDTO> result = warehouseService.getWarehouseList("测试", 1, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
    }

    @Test
    void testGetWarehouseList_DefaultStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Warehouse> page = new PageImpl<>(Arrays.asList(warehouse));
        when(warehouseRepository.findByWarehouseNameContainingAndStatus(any(), any(), eq(pageable))).thenReturn(page);

        PageResult<WarehouseDTO> result = warehouseService.getWarehouseList("测试", null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
    }
}
