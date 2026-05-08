package com.tms.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 费用计算请求DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class CostCalculateRequest {

    /**
     * 运单ID
     */
    @NotNull(message = "运单ID不能为空")
    private Long waybillId;

    /**
     * 计费方式：1-按重量, 2-按体积, 3-按里程
     */
    @NotNull(message = "计费方式不能为空")
    private Integer calculateType;

    /**
     * 单价
     */
    @NotNull(message = "单价不能为空")
    private BigDecimal unitPrice;

    /**
     * 里程(km)，按里程计费时需要
     */
    private BigDecimal distance;

    /**
     * 附加费用
     */
    private AdditionalCost additionalCost;

    /**
     * 阶梯价格配置（用于阶梯计费）
     */
    private TieredPricing tieredPricing;

    /**
     * 备注
     */
    private String remark;

    // Getters and Setters
    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
    }

    public Integer getCalculateType() {
        return calculateType;
    }

    public void setCalculateType(Integer calculateType) {
        this.calculateType = calculateType;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public AdditionalCost getAdditionalCost() {
        return additionalCost;
    }

    public void setAdditionalCost(AdditionalCost additionalCost) {
        this.additionalCost = additionalCost;
    }

    public TieredPricing getTieredPricing() {
        return tieredPricing;
    }

    public void setTieredPricing(TieredPricing tieredPricing) {
        this.tieredPricing = tieredPricing;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 附加费用
     */
    public static class AdditionalCost {
        /**
         * 装卸费
         */
        private BigDecimal loadingFee = BigDecimal.ZERO;

        /**
         * 保险费
         */
        private BigDecimal insuranceFee = BigDecimal.ZERO;

        /**
         * 等待费
         */
        private BigDecimal waitingFee = BigDecimal.ZERO;

        /**
         * 高速费
         */
        private BigDecimal tollFee = BigDecimal.ZERO;

        /**
         * 其他费用
         */
        private BigDecimal otherFee = BigDecimal.ZERO;

        public BigDecimal getLoadingFee() {
            return loadingFee;
        }

        public void setLoadingFee(BigDecimal loadingFee) {
            this.loadingFee = loadingFee;
        }

        public BigDecimal getInsuranceFee() {
            return insuranceFee;
        }

        public void setInsuranceFee(BigDecimal insuranceFee) {
            this.insuranceFee = insuranceFee;
        }

        public BigDecimal getWaitingFee() {
            return waitingFee;
        }

        public void setWaitingFee(BigDecimal waitingFee) {
            this.waitingFee = waitingFee;
        }

        public BigDecimal getTollFee() {
            return tollFee;
        }

        public void setTollFee(BigDecimal tollFee) {
            this.tollFee = tollFee;
        }

        public BigDecimal getOtherFee() {
            return otherFee;
        }

        public void setOtherFee(BigDecimal otherFee) {
            this.otherFee = otherFee;
        }

        /**
         * 计算附加费用总和
         */
        public BigDecimal getTotal() {
            return loadingFee.add(insuranceFee).add(waitingFee).add(tollFee).add(otherFee);
        }
    }

    /**
     * 阶梯价格配置
     */
    public static class TieredPricing {

        /**
         * 阶梯类型：1-按重量, 2-按体积, 3-按里程
         */
        private Integer tierType;

        /**
         * 阶梯区间列表
         */
        private List<Tier> tiers;

        public Integer getTierType() {
            return tierType;
        }

        public void setTierType(Integer tierType) {
            this.tierType = tierType;
        }

        public List<Tier> getTiers() {
            return tiers;
        }

        public void setTiers(List<Tier> tiers) {
            this.tiers = tiers;
        }

        /**
         * 阶梯区间
         */
        public static class Tier {
            /**
             * 区间起始值（包含）
             */
            private BigDecimal minValue;

            /**
             * 区间结束值（不包含，null表示无穷大）
             */
            private BigDecimal maxValue;

            /**
             * 该区间单价
             */
            private BigDecimal unitPrice;

            public Tier() {
            }

            public Tier(BigDecimal minValue, BigDecimal maxValue, BigDecimal unitPrice) {
                this.minValue = minValue;
                this.maxValue = maxValue;
                this.unitPrice = unitPrice;
            }

            public BigDecimal getMinValue() {
                return minValue;
            }

            public void setMinValue(BigDecimal minValue) {
                this.minValue = minValue;
            }

            public BigDecimal getMaxValue() {
                return maxValue;
            }

            public void setMaxValue(BigDecimal maxValue) {
                this.maxValue = maxValue;
            }

            public BigDecimal getUnitPrice() {
                return unitPrice;
            }

            public void setUnitPrice(BigDecimal unitPrice) {
                this.unitPrice = unitPrice;
            }

            /**
             * 检查值是否在该区间内
             */
            public boolean contains(BigDecimal value) {
                if (value == null) return false;
                boolean aboveMin = minValue == null || value.compareTo(minValue) >= 0;
                boolean belowMax = maxValue == null || value.compareTo(maxValue) < 0;
                return aboveMin && belowMax;
            }
        }
    }
}
