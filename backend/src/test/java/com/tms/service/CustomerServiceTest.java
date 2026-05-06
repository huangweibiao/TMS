package com.tms.service;

import com.tms.dto.CustomerDTO;
import com.tms.entity.Customer;
import com.tms.exception.BusinessException;
import com.tms.repository.CustomerRepository;
import com.tms.service.impl.CustomerServiceImpl;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 客户服务单元测试
 *
 * @author TMS Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerCode("C001");
        customer.setCustomerName("测试客户");
        customer.setContactPerson("张三");
        customer.setContactPhone("13800138000");
        customer.setStatus(1);

        customerDTO = new CustomerDTO();
        customerDTO.setId(1L);
        customerDTO.setCustomerCode("C001");
        customerDTO.setCustomerName("测试客户");
        customerDTO.setContactPerson("张三");
        customerDTO.setContactPhone("13800138000");
        customerDTO.setStatus(1);
    }

    @Test
    void testGetCustomerById_Success() {
        // 准备
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // 执行
        CustomerDTO result = customerService.getCustomerById(1L);

        // 验证
        assertNotNull(result);
        assertEquals("C001", result.getCustomerCode());
        assertEquals("测试客户", result.getCustomerName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCustomerById_NotFound() {
        // 准备
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            customerService.getCustomerById(1L);
        });
    }

    @Test
    void testCreateCustomer_Success() {
        // 准备
        when(customerRepository.existsByCustomerCode("C001")).thenReturn(false);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        // 执行
        CustomerDTO result = customerService.createCustomer(customerDTO);

        // 验证
        assertNotNull(result);
        assertEquals("C001", result.getCustomerCode());
        verify(customerRepository, times(1)).existsByCustomerCode("C001");
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateCustomer_CodeExists() {
        // 准备
        when(customerRepository.existsByCustomerCode("C001")).thenReturn(true);

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            customerService.createCustomer(customerDTO);
        });
        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void testDeleteCustomer_Success() {
        // 准备
        when(customerRepository.existsById(1L)).thenReturn(true);
        doNothing().when(customerRepository).deleteById(1L);

        // 执行
        customerService.deleteCustomer(1L);

        // 验证
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        // 准备
        when(customerRepository.existsById(1L)).thenReturn(false);

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            customerService.deleteCustomer(1L);
        });
        verify(customerRepository, never()).deleteById(any());
    }
}
