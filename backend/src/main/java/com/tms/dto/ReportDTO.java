package com.tms.dto;

import java.math.BigDecimal;

/**
 * 报表统计DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class ReportDTO {

    /**
     * 统计日期
     */
    private String date;

    /**
     * 运单数量
     */
    private Long waybillCount;

    /**
     * 运输里程
     */
    private BigDecimal totalDistance;

    /**
     * 运费金额
     */
    private BigDecimal freightAmount;

    /**
     * 成本金额
     */
    private BigDecimal costAmount;

    /**
     * 利润
     */
    private BigDecimal profit;

    /**
     * 异常数量
     */
    private Long exceptionCount;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getWaybillCount() {
        return waybillCount;
    }

    public void setWaybillCount(Long waybillCount) {
        this.waybillCount = waybillCount;
    }

    public BigDecimal getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(BigDecimal totalDistance) {
        this.totalDistance = totalDistance;
    }

    public BigDecimal getFreightAmount() {
        return freightAmount;
    }

    public void setFreightAmount(BigDecimal freightAmount) {
        this.freightAmount = freightAmount;
    }

    public BigDecimal getCostAmount() {
        return costAmount;
    }

    public void setCostAmount(BigDecimal costAmount) {
        this.costAmount = costAmount;
    }

    public BigDecimal getProfit() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit = profit;
    }

    public Long getExceptionCount() {
        return exceptionCount;
    }

    public void setExceptionCount(Long exceptionCount) {
        this.exceptionCount = exceptionCount;
    }
}
