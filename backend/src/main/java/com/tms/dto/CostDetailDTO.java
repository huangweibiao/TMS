package com.tms.dto;

import java.math.BigDecimal;

/**
 * 费用明细DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class CostDetailDTO {

    private Long id;

    /**
     * 运单ID
     */
    private Long waybillId;

    /**
     * 运单号
     */
    private String waybillNo;

    /**
     * 费用类型：1-运费, 2-装卸费, 3-保险费, 4-等待费, 5-高速费, 6-油费, 7-罚款
     */
    private Integer costType;

    /**
     * 费用类型名称
     */
    private String costTypeName;

    /**
     * 金额(元)
     */
    private BigDecimal amount;

    /**
     * 币种
     */
    private String currency;

    /**
     * 方向：1-应收(客户), 2-应付(承运商)
     */
    private Integer direction;

    /**
     * 方向名称
     */
    private String directionName;

    /**
     * 结算状态：1-未结算, 2-已结算, 3-部分结算
     */
    private Integer settlementStatus;

    /**
     * 发票号
     */
    private String invoiceNo;

    /**
     * 结算单ID
     */
    private Long settlementId;

    /**
     * 备注
     */
    private String remark;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
    }

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public Integer getCostType() {
        return costType;
    }

    public void setCostType(Integer costType) {
        this.costType = costType;
    }

    public String getCostTypeName() {
        return costTypeName;
    }

    public void setCostTypeName(String costTypeName) {
        this.costTypeName = costTypeName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }

    public String getDirectionName() {
        return directionName;
    }

    public void setDirectionName(String directionName) {
        this.directionName = directionName;
    }

    public Integer getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(Integer settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public Long getSettlementId() {
        return settlementId;
    }

    public void setSettlementId(Long settlementId) {
        this.settlementId = settlementId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
