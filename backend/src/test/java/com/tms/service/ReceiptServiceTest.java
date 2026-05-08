package com.tms.service;

import com.tms.dto.ReceiptDTO;
import com.tms.entity.Receipt;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.ReceiptRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.impl.ReceiptServiceImpl;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private WaybillRepository waybillRepository;

    @InjectMocks
    private ReceiptServiceImpl receiptService;

    private Receipt receipt;
    private ReceiptDTO receiptDTO;
    private Waybill waybill;

    @BeforeEach
    void setUp() {
        receipt = new Receipt();
        receipt.setId(1L);
        receipt.setReceiptNo("RT202401010001");
        receipt.setWaybillId(1L);
        receipt.setReceiptType(1);
        receipt.setSignerName("张三");
        receipt.setSignerPhone("13800138000");
        receipt.setStatus(1);

        receiptDTO = new ReceiptDTO();
        receiptDTO.setId(1L);
        receiptDTO.setWaybillId(1L);
        receiptDTO.setReceiptType(1);
        receiptDTO.setSignerName("张三");
        receiptDTO.setSignerPhone("13800138000");

        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");
    }

    @Test
    void testFindById_Success() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));

        Optional<Receipt> result = receiptService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(receipt.getId(), result.get().getId());
        verify(receiptRepository).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Receipt> result = receiptService.findById(1L);

        assertFalse(result.isPresent());
        verify(receiptRepository).findById(1L);
    }

    @Test
    void testCreateReceipt_Success() {
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(receiptRepository.findByWaybillId(1L)).thenReturn(Optional.empty());
        when(receiptRepository.findMaxReceiptNoByPrefix(any())).thenReturn(null);
        when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);

        Receipt result = receiptService.createReceipt(receiptDTO);

        assertNotNull(result);
        assertEquals(receipt.getWaybillId(), result.getWaybillId());
        verify(waybillRepository).findById(1L);
        verify(receiptRepository).save(any(Receipt.class));
    }

    @Test
    void testCreateReceipt_WaybillNotFound() {
        when(waybillRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            receiptService.createReceipt(receiptDTO);
        });

        assertEquals("运单不存在", exception.getMessage());
        verify(receiptRepository, never()).save(any());
    }

    @Test
    void testCreateReceipt_ReceiptAlreadyExists() {
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(receiptRepository.findByWaybillId(1L)).thenReturn(Optional.of(receipt));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            receiptService.createReceipt(receiptDTO);
        });

        assertEquals("该运单已存在回单", exception.getMessage());
        verify(receiptRepository, never()).save(any());
    }

    @Test
    void testUpdateReceipt_Success() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));
        when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);

        Receipt result = receiptService.updateReceipt(1L, receiptDTO);

        assertNotNull(result);
        verify(receiptRepository).findById(1L);
        verify(receiptRepository).save(any(Receipt.class));
    }

    @Test
    void testUpdateReceipt_NotFound() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            receiptService.updateReceipt(1L, receiptDTO);
        });

        assertEquals("回单不存在", exception.getMessage());
    }

    @Test
    void testUpdateReceipt_InvalidStatus() {
        receipt.setStatus(2);
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            receiptService.updateReceipt(1L, receiptDTO);
        });

        assertEquals("只有未回传状态的回单可以修改", exception.getMessage());
    }

    @Test
    void testDeleteReceipt_Success() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));

        receiptService.deleteReceipt(1L);

        verify(receiptRepository).findById(1L);
        verify(receiptRepository).deleteById(1L);
    }

    @Test
    void testDeleteReceipt_NotFound() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            receiptService.deleteReceipt(1L);
        });

        assertEquals("回单不存在", exception.getMessage());
    }

    @Test
    void testDeleteReceipt_InvalidStatus() {
        receipt.setStatus(2);
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            receiptService.deleteReceipt(1L);
        });

        assertEquals("只有未回传状态的回单可以删除", exception.getMessage());
    }

    @Test
    void testFindReceipts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Receipt> page = new PageImpl<>(Arrays.asList(receipt));
        when(receiptRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(page);

        Page<Receipt> result = receiptService.findReceipts("RT", 1, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void testReturnReceipt_Success() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));
        when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);

        Receipt result = receiptService.returnReceipt(1L);

        assertNotNull(result);
        assertEquals(2, result.getStatus());
        verify(receiptRepository).save(any(Receipt.class));
    }

    @Test
    void testReturnReceipt_InvalidStatus() {
        receipt.setStatus(2);
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            receiptService.returnReceipt(1L);
        });

        assertEquals("只有未回传状态的回单可以回传", exception.getMessage());
    }

    @Test
    void testAuditReceipt_Success() {
        receipt.setStatus(2);
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));
        when(receiptRepository.save(any(Receipt.class))).thenReturn(receipt);

        Receipt result = receiptService.auditReceipt(1L, "admin");

        assertNotNull(result);
        assertEquals(3, result.getStatus());
        assertEquals("admin", result.getAuditBy());
        verify(receiptRepository).save(any(Receipt.class));
    }

    @Test
    void testAuditReceipt_InvalidStatus() {
        when(receiptRepository.findById(1L)).thenReturn(Optional.of(receipt));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            receiptService.auditReceipt(1L, "admin");
        });

        assertEquals("只有已回传状态的回单可以审核", exception.getMessage());
    }

    @Test
    void testFindByReceiptNo() {
        when(receiptRepository.findByReceiptNo("RT202401010001")).thenReturn(Optional.of(receipt));

        Optional<Receipt> result = receiptService.findByReceiptNo("RT202401010001");

        assertTrue(result.isPresent());
        assertEquals(receipt.getReceiptNo(), result.get().getReceiptNo());
    }

    @Test
    void testFindByWaybillId() {
        when(receiptRepository.findByWaybillId(1L)).thenReturn(Optional.of(receipt));

        Optional<Receipt> result = receiptService.findByWaybillId(1L);

        assertTrue(result.isPresent());
        assertEquals(receipt.getWaybillId(), result.get().getWaybillId());
    }
}
