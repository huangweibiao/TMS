package com.tms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 运单DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class WaybillDTO {

    private Long id;

    /**
     * 运单号
     */
    private String waybillNo;

    /**
     * 客户订单号
     */
    private String orderNo;

    /**
     * 客户ID
     */
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    /**
     * 客户名称（用于展示）
     */
    private String customerName;

    /**
     * 承运商ID
     */
    private Long carrierId;

    /**
     * 货物类型
     */
    private String productType;

    /**
     * 总重量(kg)
     */
    private BigDecimal totalWeight;

    /**
     * 总体积(m³)
     */
    private BigDecimal totalVolume;

    /**
     * 总件数
     */
    private Integer totalQuantity;

    /**
     * 货物价值(元)
     */
    private BigDecimal goodsValue;

    /**
     * 是否危险品：1-是, 0-否
     */
    private Integer isHazardous = 0;

    /**
     * 是否易碎：1-是, 0-否
     */
    private Integer isFragile = 0;

    /**
     * 是否需要温控：1-是, 0-否
     */
    private Integer needTemperature = 0;

    /**
     * 最低温度(℃)
     */
    private BigDecimal minTemp;

    /**
     * 最高温度(℃)
     */
    private BigDecimal maxTemp;

    /**
     * 发货方名称
     */
    @NotBlank(message = "发货方名称不能为空")
    private String shipperName;

    /**
     * 发货方电话
     */
    @NotBlank(message = "发货方电话不能为空")
    private String shipperPhone;

    /**
     * 发货方地址
     */
    @NotBlank(message = "发货方地址不能为空")
    private String shipperAddress;

    /**
     * 收货方名称
     */
    @NotBlank(message = "收货方名称不能为空")
    private String consigneeName;

    /**
     * 收货方电话
     */
    @NotBlank(message = "收货方电话不能为空")
    private String consigneePhone;

    /**
     * 收货方地址
     */
    @NotBlank(message = "收货方地址不能为空")
    private String consigneeAddress;

    /**
     * 期望提货时间
     */
    private LocalDateTime expectPickupTime;

    /**
     * 期望送达时间
     */
    private LocalDateTime expectDeliveryTime;

    /**
     * 实际提货时间
     */
    private LocalDateTime actualPickupTime;

    /**
     * 实际送达时间
     */
    private LocalDateTime actualDeliveryTime;

    /**
     * 状态：1-待调度, 2-已调度, 3-提货中, 4-运输中, 5-签收, 6-异常, 7-取消
     */
    private Integer waybillStatus = 1;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 运单明细列表
     */
    private List List<WaybillDetailDTO> details;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public List List<WaybillDetailDTO> getDetails() {
        return details;
    }

    public void setDetails(List(List<WaybillDetailDTO> details) {
        this.details = details;
    }
}
