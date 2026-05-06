package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 结算单实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_settlement")
public class Settlement extends BaseEntity {

    /**
     * 结算单号
     */
    @Column(name = "settlement_no", nullable = false, unique = true, length = 32)
    private String settlementNo;

    /**
     * 结算方类型：1-客户, 2-承运商
     */
    @Column(name = "party_type", nullable = false)
    private Integer partyType;

    /**
     * 结算方ID（客户/承运商ID）
     */
    @Column(name = "party_id", nullable = false)
    private Long partyId;

    /**
     * 结算开始日期
     */
    @Column(name = "settlement_start", nullable = false)
    private LocalDate settlementStart;

    /**
     * 结算结束日期
     */
    @Column(name = "settlement_end", nullable = false)
    private LocalDate settlementEnd;

    /**
     * 总金额
     */
    @Column(name = "total_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 已付金额
     */
    @Column(name = "paid_amount", precision = 14, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    /**
     * 未付金额
     */
    @Column(name = "unpaid_amount", precision = 14, scale = 2)
    private BigDecimal unpaidAmount = BigDecimal.ZERO;

    /**
     * 状态：1-待确认, 2-已确认, 3-部分付款, 4-已结清, 5-已取消
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 确认时间
     */
    @Column(name = "confirm_time")
    private LocalDateTime confirmTime;

    /**
     * 付款时间
     */
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    /**
     * 开票状态：0-未开票, 1-已开票, 2-红冲
     */
    @Column(name = "invoice_status")
    private Integer invoiceStatus = 0;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "create_by", length = 50)
    private String createBy;

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
}
