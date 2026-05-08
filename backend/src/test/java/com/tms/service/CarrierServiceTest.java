package com.tms.service;

import com.tms.dto.CarrierDTO;
import com.tms.common.PageResult;
import com.tms.entity.Carrier;
import com.tms.exception.BusinessException;
import com.tms.repository.CarrierRepository;
import com.tms.service.impl.CarrierServiceImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarrierServiceTest {

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private CarrierServiceImpl carrierService;

    private Carrier carrier;
    private CarrierDTO carrierDTO;

    @BeforeEach
    void setUp() {
        carrier = new Carrier();
        carrier.setId(1L);
        carrier.setCarrierCode("CA001");
        carrier.setCarrierName("测试承运商");
        carrier.setCarrierType(1);
        carrier.setLicenseNo("LIC001");
        carrier.setTaxNo("TAX001");
        carrier.setContactPerson("张三");
        carrier.setContactPhone("13800138000");
        carrier.setCooperationStart(LocalDate.now());
        carrier.setCooperationEnd(LocalDate.now().plusYears(1));
        carrier.setRating(new BigDecimal("5.00"));
        carrier.setStatus(1);

        carrierDTO = new CarrierDTO();
        carrierDTO.setId(1L);
        carrierDTO.setCarrierCode("CA001");
        carrierDTO.setCarrierName("测试承运商");
        carrierDTO.setCarrierType(1);
        carrierDTO.setLicenseNo("LIC001");
        carrierDTO.setTaxNo("TAX001");
        carrierDTO.setContactPerson("张三");
        carrierDTO.setContactPhone("13800138000");
        carrierDTO.setCooperationStart(LocalDate.now());
        carrierDTO.setCooperationEnd(LocalDate.now().plusYears(1));
        carrierDTO.setRating(new BigDecimal("5.00"));
        carrierDTO.setStatus(1);
    }

    @Test
    void testGetCarrierById_Success() {
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));

        CarrierDTO result = carrierService.getCarrierById(1L);

        assertNotNull(result);
        assertEquals(carrier.getId(), result.getId());
        assertEquals(carrier.getCarrierCode(), result.getCarrierCode());
        verify(carrierRepository).findById(1L);
    }

    @Test
    void testGetCarrierById_NotFound() {
        when(carrierRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            carrierService.getCarrierById(1L);
        });

        assertEquals("承运商不存在", exception.getMessage());
        verify(carrierRepository).findById(1L);
    }

    @Test
    void testCreateCarrier_Success() {
        when(carrierRepository.save(any(Carrier.class))).thenReturn(carrier);

        CarrierDTO result = carrierService.createCarrier(carrierDTO);

        assertNotNull(result);
        assertEquals(carrier.getCarrierCode(), result.getCarrierCode());
        verify(carrierRepository).existsByCarrierCode("CA001");
        verify(carrierRepository).save(any(Carrier.class));
    }

    @Test
    void testCreateCarrier_CodeAlreadyExists() {
        when(carrierRepository.existsByCarrierCode("CA001")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            carrierService.createCarrier(carrierDTO);
        });

        assertEquals("承运商编码已存在", exception.getMessage());
        verify(carrierRepository, never()).save(any());
    }

    @Test
    void testUpdateCarrier_Success() {
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));
        when(carrierRepository.save(any(Carrier.class))).thenReturn(carrier);

        CarrierDTO result = carrierService.updateCarrier(1L, carrierDTO);

        assertNotNull(result);
        assertEquals(carrier.getCarrierCode(), result.getCarrierCode());
        verify(carrierRepository).findById(1L);
        verify(carrierRepository).save(any(Carrier.class));
    }

    @Test
    void testUpdateCarrier_NotFound() {
        when(carrierRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            carrierService.updateCarrier(1L, carrierDTO);
        });

        assertEquals("承运商不存在", exception.getMessage());
    }

    @Test
    void testUpdateCarrier_CodeAlreadyExists() {
        Carrier existingCarrier = new Carrier();
        existingCarrier.setId(2L);
        existingCarrier.setCarrierCode("CA002");

        carrierDTO.setCarrierCode("CA002");
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));
        when(carrierRepository.existsByCarrierCode("CA002")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            carrierService.updateCarrier(1L, carrierDTO);
        });

        assertEquals("承运商编码已存在", exception.getMessage());
    }

    @Test
    void testDeleteCarrier_Success() {
        when(carrierRepository.existsById(1L)).thenReturn(true);

        carrierService.deleteCarrier(1L);

        verify(carrierRepository).existsById(1L);
        verify(carrierRepository).deleteById(1L);
    }

    @Test
    void testDeleteCarrier_NotFound() {
        when(carrierRepository.existsById(1L)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            carrierService.deleteCarrier(1L);
        });

        assertEquals("承运商不存在", exception.getMessage());
        verify(carrierRepository, never()).deleteById(any());
    }

    @Test
    void testGetCarrierList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Carrier> page = new PageImpl<>(Arrays.asList(carrier));
        when(carrierRepository.findByCarrierNameContainingAndStatus(any(), any(), eq(pageable))).thenReturn(page);

        PageResult<CarrierDTO> result = carrierService.getCarrierList("测试", 1, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
        assertEquals(1, result.getPageNum());
        assertEquals(10, result.getPageSize());
    }

    @Test
    void testGetCarrierList_DefaultStatus() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Carrier> page = new PageImpl<>(Arrays.asList(carrier));
        when(carrierRepository.findByCarrierNameContainingAndStatus(any(), any(), eq(pageable))).thenReturn(page);

        PageResult<CarrierDTO> result = carrierService.getCarrierList("测试", null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
    }
}
