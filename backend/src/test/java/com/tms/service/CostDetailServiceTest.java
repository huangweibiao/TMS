package com.tms.service;

import com.tms.common.PageResult;
import com.tms.dto.CostCalculateRequest;
import com.tms.dto.CostDetailDTO;
import com.tms.entity.CostDetail;
import com.tms.entity.Waybill;
import com.tms.exception.BusinessException;
import com.tms.repository.CostDetailRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.impl.CostDetailServiceImpl;
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
public class CostDetailServiceTest {

    @Mock
    private CostDetailRepository costDetailRepository;

    @Mock
    private WaybillRepository waybillRepository;

    @InjectMocks
    private CostDetailServiceImpl costDetailService;

    private Waybill waybill;
    private CostDetail costDetail;
    private CostDetailDTO costDetailDTO;

    @BeforeEach
    void setUp() {
        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");
        waybill.setTotalWeight(new BigDecimal("10.00"));
        waybill.setTotalVolume(new BigDecimal("5.00"));

        costDetail = new CostDetail();
        costDetail.setId(1L);
        costDetail.setWaybillId(1L);
        costDetail.setCostType(1);
        costDetail.setAmount(new BigDecimal("100.00"));
        costDetail.setCurrency("CNY");
        costDetail.setDirection(1);
        costDetail.setSettlementStatus(1);

        costDetailDTO = new CostDetailDTO();
        costDetailDTO.setId(1L);
        costDetailDTO.setWaybillId(1L);
        costDetailDTO.setWaybillNo("WB202401010001");
        costDetailDTO.setCostType(1);
        costDetailDTO.setCostTypeName("运费");
        costDetailDTO.setAmount(new BigDecimal("100.00"));
        costDetailDTO.setCurrency("CNY");
        costDetailDTO.setDirection(1);
        costDetailDTO.setDirectionName("应收");
        costDetailDTO.setSettlementStatus(1);
    }

    @Test
    void testGetCostDetailById_Success() {
        when(costDetailRepository.findById(1L)).thenReturn(Optional.of(costDetail));
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        CostDetailDTO result = costDetailService.getCostDetailById(1L);

        assertNotNull(result);
        assertEquals(costDetail.getId(), result.getId());
        verify(costDetailRepository).findById(1L);
    }

    @Test
    void testGetCostDetailById_NotFound() {
        when(costDetailRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            costDetailService.getCostDetailById(1L);
        });

        assertEquals("费用明细不存在", exception.getMessage());
    }

    @Test
    void testGetCostDetailsByWaybillId() {
        when(costDetailRepository.findByWaybillId(1L)).thenReturn(Arrays.asList(costDetail));
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        List<CostDetailDTO> result = costDetailService.getCostDetailsByWaybillId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateCostDetail_Success() {
        when(costDetailRepository.save(any(CostDetail.class))).thenReturn(costDetail);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        CostDetailDTO result = costDetailService.createCostDetail(costDetailDTO);

        assertNotNull(result);
        verify(costDetailRepository).save(any(CostDetail.class));
    }

    @Test
    void testUpdateCostDetail_Success() {
        when(costDetailRepository.findById(1L)).thenReturn(Optional.of(costDetail));
        when(costDetailRepository.save(any(CostDetail.class))).thenReturn(costDetail);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        CostDetailDTO result = costDetailService.updateCostDetail(1L, costDetailDTO);

        assertNotNull(result);
        verify(costDetailRepository).findById(1L);
        verify(costDetailRepository).save(any(CostDetail.class));
    }

    @Test
    void testUpdateCostDetail_NotFound() {
        when(costDetailRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            costDetailService.updateCostDetail(1L, costDetailDTO);
        });

        assertEquals("费用明细不存在", exception.getMessage());
    }

    @Test
    void testDeleteCostDetail_Success() {
        when(costDetailRepository.existsById(1L)).thenReturn(true);

        costDetailService.deleteCostDetail(1L);

        verify(costDetailRepository).existsById(1L);
        verify(costDetailRepository).deleteById(1L);
    }

    @Test
    void testDeleteCostDetail_NotFound() {
        when(costDetailRepository.existsById(1L)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            costDetailService.deleteCostDetail(1L);
        });

        assertEquals("费用明细不存在", exception.getMessage());
    }

    @Test
    void testCalculateCost_ByWeight() {
        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(1);
        request.setUnitPrice(new BigDecimal("10.00"));

        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(costDetailRepository.save(any(CostDetail.class))).thenReturn(costDetail);

        List<CostDetailDTO> result = costDetailService.calculateCost(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(0, result.get(0).getAmount().compareTo(new BigDecimal("100.00")));
    }

    @Test
    void testCalculateCost_ByVolume() {
        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(2);
        request.setUnitPrice(new BigDecimal("20.00"));

        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(costDetailRepository.save(any(CostDetail.class))).thenReturn(costDetail);

        List<CostDetailDTO> result = costDetailService.calculateCost(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testCalculateCost_ByDistance() {
        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(3);
        request.setDistance(new BigDecimal("100.00"));
        request.setUnitPrice(new BigDecimal("2.00"));

        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(costDetailRepository.save(any(CostDetail.class))).thenReturn(costDetail);

        List<CostDetailDTO> result = costDetailService.calculateCost(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testCalculateCost_WaybillNotFound() {
        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(1);

        when(waybillRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            costDetailService.calculateCost(request);
        });

        assertEquals("运单不存在", exception.getMessage());
    }

    @Test
    void testCalculateCost_WithAdditionalCost() {
        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(1);
        request.setUnitPrice(new BigDecimal("10.00"));
        
        CostCalculateRequest.AdditionalCost additional = new CostCalculateRequest.AdditionalCost();
        additional.setLoadingFee(new BigDecimal("50.00"));
        additional.setInsuranceFee(new BigDecimal("20.00"));
        request.setAdditionalCost(additional);

        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));
        when(costDetailRepository.save(any(CostDetail.class))).thenReturn(costDetail);

        List<CostDetailDTO> result = costDetailService.calculateCost(request);

        assertNotNull(result);
        assertTrue(result.size() >= 3);
    }

    @Test
    void testGetCostDetailList() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CostDetail> page = new PageImpl<>(Arrays.asList(costDetail), pageable, 1);
        
        when(costDetailRepository.findAll(pageable)).thenReturn(page);
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        PageResult<CostDetailDTO> result = costDetailService.getCostDetailList(null, null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetCostDetailList_ByWaybillNo() {
        when(waybillRepository.findByWaybillNo("WB202401010001")).thenReturn(Optional.of(waybill));
        when(costDetailRepository.findByWaybillId(1L)).thenReturn(Arrays.asList(costDetail));

        PageResult<CostDetailDTO> result = costDetailService.getCostDetailList("WB202401010001", null, 1, 10);

        assertNotNull(result);
        assertEquals(1, result.getList().size());
    }

    @Test
    void testGetCostDetailList_WaybillNotFound() {
        when(waybillRepository.findByWaybillNo("WB999")).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            costDetailService.getCostDetailList("WB999", null, 1, 10);
        });

        assertEquals("运单不存在", exception.getMessage());
    }
}
