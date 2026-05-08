package com.tms.service;

import com.tms.dto.CostCalculateRequest;
import com.tms.dto.CostDetailDTO;
import com.tms.entity.Waybill;
import com.tms.repository.CostDetailRepository;
import com.tms.repository.WaybillRepository;
import com.tms.service.impl.CostDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TieredPricingTest {

    @Mock
    private CostDetailRepository costDetailRepository;

    @Mock
    private WaybillRepository waybillRepository;

    @InjectMocks
    private CostDetailServiceImpl costDetailService;

    private Waybill waybill;

    @BeforeEach
    void setUp() {
        waybill = new Waybill();
        waybill.setId(1L);
        waybill.setWaybillNo("WB202401010001");
        waybill.setTotalWeight(new BigDecimal("5000")); // 5吨
        waybill.setTotalVolume(new BigDecimal("15"));   // 15立方米
    }

    @Test
    void calculateCost_WithTieredPricing_ByWeight() {
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        // 配置阶梯价格：
        // 0-1000kg: 2元/kg
        // 1000-3000kg: 1.8元/kg
        // 3000kg以上: 1.5元/kg
        CostCalculateRequest.TieredPricing tieredPricing = new CostCalculateRequest.TieredPricing();
        tieredPricing.setTierType(1); // 按重量
        tieredPricing.setTiers(Arrays.asList(
                new CostCalculateRequest.TieredPricing.Tier(
                        new BigDecimal("0"), new BigDecimal("1000"), new BigDecimal("2.0")),
                new CostCalculateRequest.TieredPricing.Tier(
                        new BigDecimal("1000"), new BigDecimal("3000"), new BigDecimal("1.8")),
                new CostCalculateRequest.TieredPricing.Tier(
                        new BigDecimal("3000"), null, new BigDecimal("1.5"))
        ));

        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(1);
        request.setUnitPrice(new BigDecimal("2.0"));
        request.setTieredPricing(tieredPricing);

        List<CostDetailDTO> result = costDetailService.calculateCost(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        // 计算预期费用：
        // 第一档：1000kg * 2.0 = 2000
        // 第二档：2000kg * 1.8 = 3600
        // 第三档：2000kg * 1.5 = 3000
        // 总计：8600
        BigDecimal expectedAmount = new BigDecimal("8600");
        assertEquals(0, result.get(0).getAmount().compareTo(expectedAmount));
    }

    @Test
    void calculateCost_WithTieredPricing_ByVolume() {
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        // 配置阶梯价格：
        // 0-5m³: 100元/m³
        // 5-10m³: 90元/m³
        // 10m³以上: 80元/m³
        CostCalculateRequest.TieredPricing tieredPricing = new CostCalculateRequest.TieredPricing();
        tieredPricing.setTierType(2); // 按体积
        tieredPricing.setTiers(Arrays.asList(
                new CostCalculateRequest.TieredPricing.Tier(
                        new BigDecimal("0"), new BigDecimal("5"), new BigDecimal("100")),
                new CostCalculateRequest.TieredPricing.Tier(
                        new BigDecimal("5"), new BigDecimal("10"), new BigDecimal("90")),
                new CostCalculateRequest.TieredPricing.Tier(
                        new BigDecimal("10"), null, new BigDecimal("80"))
        ));

        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(2);
        request.setUnitPrice(new BigDecimal("100"));
        request.setTieredPricing(tieredPricing);

        List<CostDetailDTO> result = costDetailService.calculateCost(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        // 计算预期费用：
        // 第一档：5m³ * 100 = 500
        // 第二档：5m³ * 90 = 450
        // 第三档：5m³ * 80 = 400
        // 总计：1350
        BigDecimal expectedAmount = new BigDecimal("1350");
        assertEquals(0, result.get(0).getAmount().compareTo(expectedAmount));
    }

    @Test
    void calculateCost_WithTieredPricing_SmallValue() {
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        waybill.setTotalWeight(new BigDecimal("500")); // 0.5吨

        // 配置阶梯价格
        CostCalculateRequest.TieredPricing tieredPricing = new CostCalculateRequest.TieredPricing();
        tieredPricing.setTierType(1);
        tieredPricing.setTiers(Arrays.asList(
                new CostCalculateRequest.TieredPricing.Tier(
                        new BigDecimal("0"), new BigDecimal("1000"), new BigDecimal("2.0")),
                new CostCalculateRequest.TieredPricing.Tier(
                        new BigDecimal("1000"), null, new BigDecimal("1.5"))
        ));

        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(1);
        request.setUnitPrice(new BigDecimal("2.0"));
        request.setTieredPricing(tieredPricing);

        List<CostDetailDTO> result = costDetailService.calculateCost(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        // 只使用第一档：500kg * 2.0 = 1000
        BigDecimal expectedAmount = new BigDecimal("1000");
        assertEquals(0, result.get(0).getAmount().compareTo(expectedAmount));
    }

    @Test
    void calculateCost_WithoutTieredPricing_FallbackToStandard() {
        when(waybillRepository.findById(1L)).thenReturn(Optional.of(waybill));

        // 不使用阶梯价格，使用标准计费
        CostCalculateRequest request = new CostCalculateRequest();
        request.setWaybillId(1L);
        request.setCalculateType(1); // 按重量
        request.setUnitPrice(new BigDecimal("2.0"));
        // 不设置 tieredPricing

        List<CostDetailDTO> result = costDetailService.calculateCost(request);

        assertNotNull(result);
        assertFalse(result.isEmpty());

        // 标准计费：5000kg * 2.0 = 10000
        BigDecimal expectedAmount = new BigDecimal("10000");
        assertEquals(0, result.get(0).getAmount().compareTo(expectedAmount));
    }

    @Test
    void tierContains_ValueInRange() {
        CostCalculateRequest.TieredPricing.Tier tier = new CostCalculateRequest.TieredPricing.Tier(
                new BigDecimal("100"), new BigDecimal("500"), new BigDecimal("2.0"));

        assertTrue(tier.contains(new BigDecimal("100")));  // 边界值
        assertTrue(tier.contains(new BigDecimal("250")));  // 中间值
        assertTrue(tier.contains(new BigDecimal("499")));  // 边界值
        assertFalse(tier.contains(new BigDecimal("500"))); // 不包含上限
        assertFalse(tier.contains(new BigDecimal("50")));  // 低于下限
        assertFalse(tier.contains(new BigDecimal("600"))); // 高于上限
    }

    @Test
    void tierContains_WithNullMax() {
        CostCalculateRequest.TieredPricing.Tier tier = new CostCalculateRequest.TieredPricing.Tier(
                new BigDecimal("1000"), null, new BigDecimal("1.5"));

        assertTrue(tier.contains(new BigDecimal("1000")));
        assertTrue(tier.contains(new BigDecimal("5000")));
        assertTrue(tier.contains(new BigDecimal("100000")));
        assertFalse(tier.contains(new BigDecimal("999")));
    }

    @Test
    void tierContains_WithNullMin() {
        CostCalculateRequest.TieredPricing.Tier tier = new CostCalculateRequest.TieredPricing.Tier(
                null, new BigDecimal("100"), new BigDecimal("2.0"));

        assertTrue(tier.contains(new BigDecimal("0")));
        assertTrue(tier.contains(new BigDecimal("50")));
        assertTrue(tier.contains(new BigDecimal("99")));
        assertFalse(tier.contains(new BigDecimal("100")));
    }
}
