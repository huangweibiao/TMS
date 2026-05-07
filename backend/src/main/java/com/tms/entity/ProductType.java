package com.tms.entity;

import jakarta.persistence.*;

/**
 * 产品/货物类型实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_product_type")
public class ProductType extends BaseEntity {

    /**
     * 类型编码
     */
    @Column(name = "type_code", nullable = false, unique = true, length = 32)
    private String typeCode;

    /**
     * 类型名称
     */
    @Column(name = "type_name", nullable = false, length = 50)
    private String typeName;

    /**
     * 父类型ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 层级
     */
    @Column(name = "level")
    private Integer level = 1;

    /**
     * 是否需要温控：1-是, 0-否
     */
    @Column(name = "need_temperature")
    private Integer needTemperature = 0;

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
     * 默认单位重量(kg)
     */
    @Column(name = "default_unit_weight", precision = 10, scale = 3)
    private java.math.BigDecimal defaultUnitWeight;

    /**
     * 默认单位体积(m³)
     */
    @Column(name = "default_unit_volume", precision = 10, scale = 3)
    private java.math.BigDecimal defaultUnitVolume;

    /**
     * 排序号
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;

    /**
     * 状态：1-启用, 0-停用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    /**
     * 备注
     */
    @Column(name = "remark", length = 200)
    private String remark;

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getNeedTemperature() {
        return needTemperature;
    }

    public void setNeedTemperature(Integer needTemperature) {
        this.needTemperature = needTemperature;
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

    public java.math.BigDecimal getDefaultUnitWeight() {
        return defaultUnitWeight;
    }

    public void setDefaultUnitWeight(java.math.BigDecimal defaultUnitWeight) {
        this.defaultUnitWeight = defaultUnitWeight;
    }

    public java.math.BigDecimal getDefaultUnitVolume() {
        return defaultUnitVolume;
    }

    public void setDefaultUnitVolume(java.math.BigDecimal defaultUnitVolume) {
        this.defaultUnitVolume = defaultUnitVolume;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
