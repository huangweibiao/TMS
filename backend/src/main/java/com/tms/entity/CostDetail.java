package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 费用明细实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_cost_detail")
public class CostDetail extends BaseEntity {

    /**
     * 运单ID
     */
    @Column(name = "waybill_id", nullable = false)
    private Long waybillId;

    /**
     * 费用类型：1-运费, 2-装卸费, 3-保险费, 4-等待费, 5-高速费, 6-油费, 7-罚款
     */
    @Column(name = "cost_type", nullable = false)
    private Integer costType;

    /**
     * 金额(元)
     */
    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    /**
     * 币种
     */
    @Column(name = "currency", length = 3)
    private String currency = "CNY";

    /**
     * 方向：1-应收(客户), 2-应付(承运商)
     */
    @Column(name = "direction", nullable = false)
    private Integer direction;

    /**
     * 结算状态：1-未结算, 2-已结算, 3-部分结算
     */
    @Column(name = "settlement_status")
    private Integer settlementStatus = 1;

    /**
     * 发票号
     */
    @Column(name = "invoice_no", length = 50)
    private String invoiceNo;

    /**
     * 结算单ID
     */
    @Column(name = "settlement_id")
    private Long settlementId;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    public Long getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(Long waybillId) {
        this.waybillId = waybillId;
    }

    public Integer getCostType() {
        return costType;
    }

    public void setCostType(Integer costType) {
        this.costType = costType;
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
