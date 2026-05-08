package com.tms.dto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 结算单DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class SettlementDTO {

    private Long id;

    /**
     * 结算单号
     */
    private String settlementNo;

    /**
     * 结算方类型：1-客户, 2-承运商
     */
    @NotNull(message = "结算方类型不能为空")
    private Integer partyType;

    /**
     * 结算方ID
     */
    @NotNull(message = "结算方ID不能为空")
    private Long partyId;

    /**
     * 结算方名称
     */
    private String partyName;

    /**
     * 结算开始日期
     */
    @NotNull(message = "结算开始日期不能为空")
    private LocalDate settlementStart;

    /**
     * 结算结束日期
     */
    @NotNull(message = "结算结束日期不能为空")
    private LocalDate settlementEnd;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 已付金额
     */
    private BigDecimal paidAmount;

    /**
     * 未付金额
     */
    private BigDecimal unpaidAmount;

    /**
     * 状态：1-待确认, 2-已确认, 3-部分付款, 4-已结清, 5-已取消
     */
    private Integer status = 1;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 付款时间
     */
    private LocalDateTime paymentTime;

    /**
     * 开票状态：0-未开票, 1-已开票, 2-红冲
     */
    private Integer invoiceStatus = 0;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSettlementNo() {
        return settlementNo;
    }

    public void setSettlementNo(String settlementNo) {
        this.settlementNo = settlementNo;
    }

    public Integer getPartyType() {
        return partyType;
    }

    public void setPartyType(Integer partyType) {
        this.partyType = partyType;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public LocalDate getSettlementStart() {
        return settlementStart;
    }

    public void setSettlementStart(LocalDate settlementStart) {
        this.settlementStart = settlementStart;
    }

    public LocalDate getSettlementEnd() {
        return settlementEnd;
    }

    public void setSettlementEnd(LocalDate settlementEnd) {
        this.settlementEnd = settlementEnd;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public BigDecimal getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(BigDecimal unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(LocalDateTime confirmTime) {
        this.confirmTime = confirmTime;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public Integer getInvoiceStatus() {
        return invoiceStatus;
    }

    public void setInvoiceStatus(Integer invoiceStatus) {
        this.invoiceStatus = invoiceStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;

    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public java.time.LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.time.LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
