package com.tms.service;

import com.tms.dto.AddressDTO;
import com.tms.entity.Address;
import com.tms.exception.BusinessException;
import com.tms.repository.AddressRepository;
import com.tms.service.impl.AddressServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressServiceImpl addressService;

    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        address = new Address();
        address.setId(1L);
        address.setAddressCode("ADR202401010001");
        address.setAddressName("测试地址");
        address.setAddressType(1);
        address.setCustomerId(1L);
        address.setContactName("张三");
        address.setContactPhone("13800138000");
        address.setProvince("北京市");
        address.setCity("北京市");
        address.setDistrict("朝阳区");
        address.setDetailAddress("测试街道1号");
        address.setFullAddress("北京市北京市朝阳区测试街道1号");
        address.setLongitude(new BigDecimal("116.4074"));
        address.setLatitude(new BigDecimal("39.9042"));
        address.setIsDefault(0);
        address.setStatus(1);

        addressDTO = new AddressDTO();
        addressDTO.setId(1L);
        addressDTO.setAddressName("测试地址");
        addressDTO.setAddressType(1);
        addressDTO.setCustomerId(1L);
        addressDTO.setContactName("张三");
        addressDTO.setContactPhone("13800138000");
        addressDTO.setProvince("北京市");
        addressDTO.setCity("北京市");
        addressDTO.setDistrict("朝阳区");
        addressDTO.setDetailAddress("测试街道1号");
        addressDTO.setLongitude(new BigDecimal("116.4074"));
        addressDTO.setLatitude(new BigDecimal("39.9042"));
        addressDTO.setIsDefault(0);
    }

    @Test
    void testFindById_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        Optional<Address> result = addressService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(address.getId(), result.get().getId());
        verify(addressRepository).findById(1L);
    }

    @Test
    void testFindByCustomerId() {
        when(addressRepository.findByCustomerId(1L)).thenReturn(Arrays.asList(address));

        List<Address> result = addressService.findByCustomerId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByCustomerIdAndType() {
        when(addressRepository.findByCustomerIdAndAddressType(1L, 1)).thenReturn(Arrays.asList(address));

        List<Address> result = addressService.findByCustomerIdAndType(1L, 1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateAddress_Success() {
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.createAddress(addressDTO);

        assertNotNull(result);
        assertEquals(address.getAddressName(), result.getAddressName());
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void testCreateAddress_DefaultAddress() {
        addressDTO.setIsDefault(1);
        when(addressRepository.findByCustomerIdAndAddressType(1L, 1)).thenReturn(Arrays.asList());
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.createAddress(addressDTO);

        assertNotNull(result);
    }

    @Test
    void testUpdateAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.updateAddress(1L, addressDTO);

        assertNotNull(result);
        verify(addressRepository).findById(1L);
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void testUpdateAddress_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            addressService.updateAddress(1L, addressDTO);
        });

        assertEquals("地址不存在", exception.getMessage());
    }

    @Test
    void testDeleteAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));

        addressService.deleteAddress(1L);

        verify(addressRepository).findById(1L);
        verify(addressRepository).deleteById(1L);
    }

    @Test
    void testDeleteAddress_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            addressService.deleteAddress(1L);
        });

        assertEquals("地址不存在", exception.getMessage());
    }

    @Test
    void testFindAddresses() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Address> page = new PageImpl<>(Arrays.asList(address), pageable, 1);
        when(addressRepository.findAll(any(org.springframework.data.jpa.domain.Specification.class), eq(pageable))).thenReturn(page);

        Page<Address> result = addressService.findAddresses(1L, 1, 1, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testSetDefaultAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.findByCustomerIdAndAddressType(1L, 1)).thenReturn(Arrays.asList());
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.setDefaultAddress(1L);

        assertNotNull(result);
        assertEquals(1, result.getIsDefault());
    }

    @Test
    void testSetDefaultAddress_NotFound() {
        when(addressRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            addressService.setDefaultAddress(1L);
        });

        assertEquals("地址不存在", exception.getMessage());
    }

    @Test
    void testCancelDefaultAddress_Success() {
        address.setIsDefault(1);
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.cancelDefaultAddress(1L);

        assertNotNull(result);
        assertEquals(0, result.getIsDefault());
    }

    @Test
    void testEnableAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.enableAddress(1L);

        assertNotNull(result);
        assertEquals(1, result.getStatus());
    }

    @Test
    void testDisableAddress_Success() {
        when(addressRepository.findById(1L)).thenReturn(Optional.of(address));
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.disableAddress(1L);

        assertNotNull(result);
        assertEquals(0, result.getStatus());
    }

    @Test
    void testGenerateAddressCode() {
        String code = addressService.generateAddressCode();

        assertNotNull(code);
        assertTrue(code.startsWith("ADR"));
    }
}
