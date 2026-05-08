package com.tms.service;

import com.tms.dto.WaybillDTO;
import com.tms.dto.WaybillDetailDTO;
import com.tms.entity.Customer;
import com.tms.entity.Waybill;
import com.tms.entity.WaybillDetail;
import com.tms.exception.BusinessException;
import com.tms.repository.CustomerRepository;
import com.tms.repository.WaybillDetailRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.impl.WaybillServiceImpl;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 运单服务单元测试
 *
 * @author TMS Team
 * @version 1.0.0
 */
@ExtendWith(MockitoExtension.class)
public class WaybillServiceTest {

    @Mock
    private WaybillRepository waybillRepository;

    @Mock
    private WaybillDetailRepository waybillDetailRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private WaybillServiceImpl waybillService;

    private Waybill waybill;
    private WaybillDTO waybillDTO;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerCode("C001");
        customer.setCustomerName("测试客户");

        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");
        waybill.setCustomerId(1L);
        waybill.setShipperName("发货人");
        waybill.setShipperPhone("13800138000");
        waybill.setShipperAddress("发货地址");
        waybill.setConsigneeName("收货人");
        waybill.setConsigneePhone("13900139000");
        waybill.setConsigneeAddress("收货地址");
        waybill.setWaybillStatus(1);
        waybill.setTotalWeight(new BigDecimal("10.00"));
        waybill.setTotalVolume(new BigDecimal("1.00"));
        waybill.setTotalQuantity(10);

        waybillDTO = new WaybillDTO();
        waybillDTO.setId(1L);
        waybillDTO.setWaybillNo("WB202401010001");
        waybillDTO.setCustomerId(1L);
        waybillDTO.setShipperName("发货人");
        waybillDTO.setShipperPhone("13800138000");
        waybillDTO.setShipperAddress("发货地址");
        waybillDTO.setConsigneeName("收货人");
        waybillDTO.setConsigneePhone("13900139000");
        waybillDTO.setConsigneeAddress("收货地址");
        waybillDTO.setWaybillStatus(1);
    }

    @Test
    void testGetWaybillById_Success() {
        // 准备
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(waybillDetailRepository.findByWaybillId(1L)).thenReturn(Collections.emptyList());

        // 执行
        WaybillDTO result = waybillService.getWaybillById(1L);

        // 验证
        assertNotNull(result);
        assertEquals("WB202401010001", result.getWaybillNo());
        assertEquals("测试客户", result.getCustomerName());
        verify(waybillRepository, times(1)).findById(1L);
    }

    @Test
    void testGetWaybillById_NotFound() {
        // 准备
        when(waybillRepository.findById(1L)).thenReturn(Optional.empty());

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            waybillService.getWaybillById(1L);
        });
    }

    @Test
    void testCreateWaybill_Success() {
        // 准备
        when(waybillRepository.findMaxWaybillNoByPrefix(any())).thenReturn(null);
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(waybillDetailRepository.findByWaybillId(1L)).thenReturn(Collections.emptyList());

        // 执行
        WaybillDTO result = waybillService.createWaybill(waybillDTO);

        // 验证
        assertNotNull(result);
        verify(waybillRepository, times(1)).save(any(Waybill.class));
    }

    @Test
    void testUpdateWaybill_Success() {
        // 准备
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(waybillDetailRepository.findByWaybillId(1L)).thenReturn(Collections.emptyList());

        // 执行
        WaybillDTO result = waybillService.updateWaybill(1L, waybillDTO);

        // 验证
        assertNotNull(result);
        verify(waybillRepository, times(1)).save(any(Waybill.class));
    }

    @Test
    void testUpdateWaybill_NotPending() {
        // 准备
        waybill.setWaybillStatus(2); // 已调度状态
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            waybillService.updateWaybill(1L, waybillDTO);
        });
    }

    @Test
    void testDeleteWaybill_Success() {
        // 准备
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        doNothing().when(waybillDetailRepository).deleteByWaybillId(1L);
        doNothing().when(waybillRepository).deleteById(1L);

        // 执行
        waybillService.deleteWaybill(1L);

        // 验证
        verify(waybillRepository, times(1)).deleteById(1L);
        verify(waybillDetailRepository, times(1)).deleteByWaybillId(1L);
    }

    @Test
    void testDeleteWaybill_NotPending() {
        // 准备
        waybill.setWaybillStatus(2); // 已调度状态
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            waybillService.deleteWaybill(1L);
        });
    }

    @Test
    void testGetWaybillList() {
        // 准备
        Pageable pageable = PageRequest.of(0, 10);
        Page<Waybill> page = new PageImpl<>(Arrays.asList(waybill), pageable, 1);
        when(waybillRepository.findAll(pageable)).thenReturn(page);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(waybillDetailRepository.findByWaybillId(1L)).thenReturn(Collections.emptyList());

        // 执行
        var result = waybillService.getWaybillList(null, null, 1, 10);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.getList().size());
    }

    @Test
    void testCancelWaybill_Success() {
        // 准备
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);

        // 执行
        waybillService.cancelWaybill(1L, "客户取消");

        // 验证
        verify(waybillRepository, times(1)).save(any(Waybill.class));
    }

    @Test
    void testCancelWaybill_InvalidStatus() {
        // 准备
        waybill.setWaybillStatus(3); // 提货中状态
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            waybillService.cancelWaybill(1L, "客户取消");
        });
    }

    @Test
    void testConfirmPickup_Success() {
        // 准备
        waybill.setWaybillStatus(2); // 已调度状态
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);

        // 执行
        waybillService.confirmPickup(1L);

        // 验证
        verify(waybillRepository, times(1)).save(any(Waybill.class));
    }

    @Test
    void testConfirmPickup_InvalidStatus() {
        // 准备
        waybill.setWaybillStatus(1); // 待调度状态
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        // 执行和验证
        assertThrows(BusinessException.class, () -> {
            waybillService.confirmPickup(1L);
        });
    }

    @Test
    void testConfirmDelivery_Success() {
        // 准备
        waybill.setWaybillStatus(4); // 运输中状态
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);

        // 执行
        waybillService.confirmDelivery(1L, "张三", "已签收");

        // 验证
        verify(waybillRepository, times(1)).save(any(Waybill.class));
    }

    @Test
    void testMarkException_Success() {
        // 准备
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);

        // 执行
        waybillService.markException(1L, "交通事故", "道路封闭");

        // 验证
        verify(waybillRepository, times(1)).save(any(Waybill.class));
    }

    @Test
    void testHandleException_Success() {
        // 准备
        waybill.setWaybillStatus(6); // 异常状态
        waybill.setRemark("异常原因: 交通事故, 原状态: 4");
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(waybillRepository.save(any(Waybill.class))).thenReturn(waybill);

        // 执行
        waybillService.handleException(1L, "改道通行", "已解决");

        // 验证
        verify(waybillRepository, times(1)).save(any(Waybill.class));
    }
}
