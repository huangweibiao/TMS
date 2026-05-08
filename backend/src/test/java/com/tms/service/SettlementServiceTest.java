package com.tms.service;

import com.tms.dto.SettlementDTO;
import com.tms.entity.Carrier;
import com.tms.entity.Customer;
import com.tms.entity.Settlement;
import com.tms.exception.BusinessException;
import com.tms.repository.CarrierRepository;
import com.tms.repository.CustomerRepository;
import com.tms.repository.SettlementRepository;
import com.tms.service.impl.SettlementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;import org.springframework.data.domain.PageImpl;
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
public class SettlementServiceTest {

    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CarrierRepository carrierRepository;

    @InjectMocks
    private SettlementServiceImpl settlementService;

    private Settlement settlement;
    private SettlementDTO settlementDTO;
    private Customer customer;
    private Carrier carrier;

    @BeforeEach
    void setUp() {
        settlement = new Settlement();
        settlement.setId(1L);
        settlement.setSettlementNo("ST202401010001");
        settlement.setPartyType(1);
        settlement.setPartyId(1L);
        settlement.setSettlementStart(LocalDate.now());
        settlement.setSettlementEnd(LocalDate.now().plusDays(30));
        settlement.setTotalAmount(new BigDecimal("1000.00"));
        settlement.setPaidAmount(BigDecimal.ZERO);
        settlement.setUnpaidAmount(new BigDecimal("1000.00"));
        settlement.setStatus(1);

        settlementDTO = new SettlementDTO();
        settlementDTO.setId(1L);
        settlementDTO.setPartyType(1);
        settlementDTO.setPartyId(1L);
        settlementDTO.setSettlementStart(LocalDate.now());
        settlementDTO.setSettlementEnd(LocalDate.now().plusDays(30));
        settlementDTO.setTotalAmount(new BigDecimal("1000.00"));

        customer = new Customer();
        customer.setId(1L);
        customer.setCustomerCode("C001");
        customer.setCustomerName("测试客户");

        carrier = new Carrier();
        carrier.setId(1L);
        carrier.setCarrierCode("CA001");
        carrier.setCarrierName("测试承运商");
    }

    @Test
    void testFindById_Success() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));

        Optional<Settlement> result = settlementService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(settlement.getId(), result.get().getId());
        verify(settlementRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Settlement> result = settlementService.findById(1L);

        assertFalse(result.isPresent());
        verify(settlementRepository).findById(1L);
    }

    @Test
    void testCreateSettlement_Success_Customer() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(settlementRepository.findMaxSettlementNoByPrefix(any())).thenReturn(null);
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);

        Settlement result = settlementService.createSettlement(settlementDTO);

        assertNotNull(result);
        assertEquals(1, result.getPartyType());
        verify(customerRepository).findById(1L);
        verify(settlementRepository).save(any(Settlement.class));
    }

    @Test
    void testCreateSettlement_Success_Carrier() {
        settlementDTO.setPartyType(2);
        when(carrierRepository.findById(1L)).thenReturn(Optional.of(carrier));
        when(settlementRepository.findMaxSettlementNoByPrefix(any())).thenReturn(null);
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);

        Settlement result = settlementService.createSettlement(settlementDTO);

        assertNotNull(result);
        assertEquals(1, result.getPartyType());
        verify(carrierRepository).findById(1L);
        verify(settlementRepository).save(any(Settlement.class));
    }

    @Test
    void testCreateSettlement_CustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.createSettlement(settlementDTO);
        });

        assertEquals("客户不存在", exception.getMessage());
        verify(settlementRepository, never()).save(any());
    }

    @Test
    void testCreateSettlement_InvalidPartyType() {
        settlementDTO.setPartyType(3);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.createSettlement(settlementDTO);
        });

        assertEquals("无效的结算方类型", exception.getMessage());
    }

    @Test
    void testCreateSettlement_NullPartyType() {
        settlementDTO.setPartyType(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.createSettlement(settlementDTO);
        });

        assertEquals("结算方类型和ID不能为空", exception.getMessage());
    }

    @Test
    void testUpdateSettlement_Success() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);

        Settlement result = settlementService.updateSettlement(1L, settlementDTO);

        assertNotNull(result);
        verify(settlementRepository).findById(1L);
        verify(settlementRepository).save(any(Settlement.class));
    }

    @Test
    void testUpdateSettlement_NotFound() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.updateSettlement(1L, settlementDTO);
        });

        assertEquals("结算单不存在", exception.getMessage());
    }

    @Test
    void testUpdateSettlement_InvalidStatus() {
        settlement.setStatus(2);
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.updateSettlement(1L, settlementDTO);
        });

        assertEquals("只有待确认状态的结算单可以修改", exception.getMessage());
    }

    @Test
    void testDeleteSettlement_Success() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));

        settlementService.deleteSettlement(1L);

        verify(settlementRepository).findById(1L);
        verify(settlementRepository).deleteById(1L);
    }

    @Test
    void testDeleteSettlement_NotFound() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.deleteSettlement(1L);
        });

        assertEquals("结算单不存在", exception.getMessage());
    }

    @Test
    void testDeleteSettlement_InvalidStatus() {
        settlement.setStatus(2);
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.deleteSettlement(1L);
        });

        assertEquals("只有待确认或已取消状态的结算单可以删除", exception.getMessage());
    }

    @Test
    void testFindSettlements() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Settlement> page = new PageImpl<>(Arrays.asList(settlement));
        when(settlementRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Settlement> result = settlementService.findSettlements(1, 1L, 1, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testConfirmSettlement_Success() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);

        Settlement result = settlementService.confirmSettlement(1L);

        assertNotNull(result);
        assertEquals(2, result.getStatus());
        verify(settlementRepository).save(any(Settlement.class));
    }

    @Test
    void testConfirmSettlement_InvalidStatus() {
        settlement.setStatus(2);
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.confirmSettlement(1L);
        });

        assertEquals("只有待确认状态的结算单可以确认", exception.getMessage());
    }

    @Test
    void testMakePayment_Success() {
        settlement.setStatus(2);
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);

        Settlement result = settlementService.makePayment(1L, new BigDecimal("500.00"));

        assertNotNull(result);
        assertEquals(3, result.getStatus());
        assertEquals(new BigDecimal("500.00"), result.getPaidAmount());
        assertEquals(new BigDecimal("500.00"), result.getUnpaidAmount());
    }

    @Test
    void testMakePayment_FullPayment() {
        settlement.setStatus(2);
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);

        Settlement result = settlementService.makePayment(1L, new BigDecimal("1000.00"));

        assertNotNull(result);
        assertEquals(4, result.getStatus());
        assertEquals(0, result.getUnpaidAmount().compareTo(BigDecimal.ZERO));
    }

    @Test
    void testMakePayment_InvalidAmount() {
        settlement.setStatus(2);
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.makePayment(1L, BigDecimal.ZERO);
        });

        assertEquals("付款金额必须大于0", exception.getMessage());
    }

    @Test
    void testMakePayment_ExceedAmount() {
        settlement.setStatus(2);
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.makePayment(1L, new BigDecimal("2000.00"));
        });

        assertEquals("付款金额不能超过未付金额", exception.getMessage());
    }

    @Test
    void testCancelSettlement_Success() {
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));
        when(settlementRepository.save(any(Settlement.class))).thenReturn(settlement);

        Settlement result = settlementService.cancelSettlement(1L, "测试取消");

        assertNotNull(result);
        assertEquals(5, result.getStatus());
    }

    @Test
    void testCancelSettlement_InvalidStatus() {
        settlement.setStatus(3);
        when(settlementRepository.findById(1L)).thenReturn(Optional.of(settlement));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            settlementService.cancelSettlement(1L, "测试取消");
        });

        assertEquals("当前状态的结算单不能取消", exception.getMessage());
    }

    @Test
    void testFindBySettlementNo() {
        when(settlementRepository.findBySettlementNo("ST202401010001")).thenReturn(Optional.of(settlement));

        Optional<Settlement> result = settlementService.findBySettlementNo("ST202401010001");

        assertTrue(result.isPresent());
        assertEquals(settlement.getSettlementNo(), result.get().getSettlementNo());
    }
}
