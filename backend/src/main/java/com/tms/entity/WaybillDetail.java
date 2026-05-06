package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * 运单明细实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_waybill_detail")
public class WaybillDetail extends BaseEntity {

    /**
     * 运单ID
     */
    @Column(name = "waybill_id", nullable = false)
    private Long waybillId;

    /**
     * SKU编码
     */
    @Column(name = "sku_code", length = 50)
    private String skuCode;

    /**
     * 商品名称
     */
    @Column(name = "sku_name", length = 200)
    private String skuName;

    /**
     * 数量
     */
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    /**
     * 单位
     */
    @Column(name = "unit", length = 10)
    private String unit = "个";

    /**
     * 单重(kg)
     */
    @Column(name = "unit_weight", precision = 10, scale = 3)
    private BigDecimal unitWeight;

    /**
     * 单体积(m³)
     */
    @Column(name = "unit_volume", precision = 10, scale = 3)
    private BigDecimal unitVolume;

    /**
     * 单价(元)
     */
    @Column(name = "unit_price", precision = 12, scale = 2)
    private BigDecimal unitPrice;

    /**
     * 总价(元)
     */
    @Column(name = "total_price", precision = 14, scale = 2)
    private BigDecimal totalPrice;

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

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getUnitWeight() {
        return unitWeight;
    }

    public void setUnitWeight(BigDecimal unitWeight) {
        this.unitWeight = unitWeight;
    }

    public BigDecimal getUnitVolume() {
        return unitVolume;
    }

    public void setUnitVolume(BigDecimal unitVolume) {
        this.unitVolume = unitVolume;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
