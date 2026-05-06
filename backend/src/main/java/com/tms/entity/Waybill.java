package com.tms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 运单主表实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_waybill")
public class Waybill extends BaseEntity {

    /**
     * 运单号
     */
    @Column(name = "waybill_no", nullable = false, unique = true, length = 32)
    private String waybillNo;

    /**
     * 客户订单号
     */
    @Column(name = "order_no", length = 32)
    private String orderNo;

    /**
     * 客户ID
     */
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    /**
     * 承运商ID
     */
    @Column(name = "carrier_id")
    private Long carrierId;

    /**
     * 货物类型
     */
    @Column(name = "product_type", length = 50)
    private String productType;

    /**
     * 总重量(kg)
     */
    @Column(name = "total_weight", precision = 12, scale = 3)
    private BigDecimal totalWeight;

    /**
     * 总体积(m³)
     */
    @Column(name = "total_volume", precision = 12, scale = 3)
    private BigDecimal totalVolume;

    /**
     * 总件数
     */
    @Column(name = "total_quantity")
    private Integer totalQuantity;

    /**
     * 货物价值(元)
     */
    @Column(name = "goods_value", precision = 14, scale = 2)
    private BigDecimal goodsValue;

    /**
     * 是否危险品：1-是, 0-否
     */
    @Column(name = "is_hazardous")
    private Integer isHazardous = 0;

    /**
     * 是否易碎：1-是, 0-否
     */
    @Column(name = "is_fragile")
    private Integer isFragile = 0;

    /**
     * 是否需要温控：1-是, 0-否
     */
    @Column(name = "need_temperature")
    private Integer needTemperature = 0;

    /**
     * 最低温度(℃)
     */
    @Column(name = "min_temp", precision = 5, scale = 1)
    private BigDecimal minTemp;

    /**
     * 最高温度(℃)
     */
    @Column(name = "max_temp", precision = 5, scale = 1)
    private BigDecimal maxTemp;

    /**
     * 发货方名称
     */
    @Column(name = "shipper_name", nullable = false, length = 100)
    private String shipperName;

    /**
     * 发货方电话
     */
    @Column(name = "shipper_phone", nullable = false, length = 20)
    private String shipperPhone;

    /**
     * 发货方地址
     */
    @Column(name = "shipper_address", nullable = false, length = 200)
    private String shipperAddress;

    /**
     * 收货方名称
     */
    @Column(name = "consignee_name", nullable = false, length = 100)
    private String consigneeName;

    /**
     * 收货方电话
     */
    @Column(name = "consignee_phone", nullable = false, length = 20)
    private String consigneePhone;

    /**
     * 收货方地址
     */
    @Column(name = "consignee_address", nullable = false, length = 200)
    private String consigneeAddress;

    /**
     * 期望提货时间
     */
    @Column(name = "expect_pickup_time")
    private LocalDateTime expectPickupTime;

    /**
     * 期望送达时间
     */
    @Column(name = "expect_delivery_time")
    private LocalDateTime expectDeliveryTime;

    /**
     * 实际提货时间
     */
    @Column(name = "actual_pickup_time")
    private LocalDateTime actualPickupTime;

    /**
     * 实际送达时间
     */
    @Column(name = "actual_delivery_time")
    private LocalDateTime actualDeliveryTime;

    /**
     * 状态：1-待调度, 2-已调度, 3-提货中, 4-运输中, 5-签收, 6-异常, 7-取消
     */
    @Column(name = "waybill_status", nullable = false)
    private Integer waybillStatus = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "create_by", length = 50)
    private String createBy;

    public String getWaybillNo() {
        return waybillNo;
    }

    public void setWaybillNo(String waybillNo) {
        this.waybillNo = waybillNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(BigDecimal totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public BigDecimal getGoodsValue() {
        return goodsValue;
    }

    public void setGoodsValue(BigDecimal goodsValue) {
        this.goodsValue = goodsValue;
    }

    public Integer getIsHazardous() {
        return isHazardous;
    }

    public void setIsHazardous(Integer isHazardous) {
        this.isHazardous = isHazardous;
    }

    public Integer getIsFragile() {
        return isFragile;
    }

    public void setIsFragile(Integer isFragile) {
        this.isFragile = isFragile;
    }

    public Integer getNeedTemperature() {
        return needTemperature;
    }

    public void setNeedTemperature(Integer needTemperature) {
        this.needTemperature = needTemperature;
    }

    public BigDecimal getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(BigDecimal minTemp) {
        this.minTemp = minTemp;
    }

    public BigDecimal getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(BigDecimal maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getShipperName() {
        return shipperName;
    }

    public void setShipperName(String shipperName) {
        this.shipperName = shipperName;
    }

    public String getShipperPhone() {
        return shipperPhone;
    }

    public void setShipperPhone(String shipperPhone) {
        this.shipperPhone = shipperPhone;
    }

    public String getShipperAddress() {
        return shipperAddress;
    }

    public void setShipperAddress(String shipperAddress) {
        this.shipperAddress = shipperAddress;
    }

    public String getConsigneeName() {
        return consigneeName;
    }

    public void setConsigneeName(String consigneeName) {
        this.consigneeName = consigneeName;
    }

    public String getConsigneePhone() {
        return consigneePhone;
    }

    public void setConsigneePhone(String consigneePhone) {
        this.consigneePhone = consigneePhone;
    }

    public String getConsigneeAddress() {
        return consigneeAddress;
    }

    public void setConsigneeAddress(String consigneeAddress) {
        this.consigneeAddress = consigneeAddress;
    }

    public LocalDateTime getExpectPickupTime() {
        return expectPickupTime;
    }

    public void setExpectPickupTime(LocalDateTime expectPickupTime) {
        this.expectPickupTime = expectPickupTime;
    }

    public LocalDateTime getExpectDeliveryTime() {
        return expectDeliveryTime;
    }

    public void setExpectDeliveryTime(LocalDateTime expectDeliveryTime) {
        this.expectDeliveryTime = expectDeliveryTime;
    }

    public LocalDateTime getActualPickupTime() {
        return actualPickupTime;
    }

    public void setActualPickupTime(LocalDateTime actualPickupTime) {
        this.actualPickupTime = actualPickupTime;
    }

    public LocalDateTime getActualDeliveryTime() {
        return actualDeliveryTime;
    }

    public void setActualDeliveryTime(LocalDateTime actualDeliveryTime) {
        this.actualDeliveryTime = actualDeliveryTime;
    }

    public Integer getWaybillStatus() {
        return waybillStatus;
    }

    public void setWaybillStatus(Integer waybillStatus) {
        this.waybillStatus = waybillStatus;
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
