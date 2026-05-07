package com.tms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 产品类型DTO
 *
 * @author TMS Team
 * @version 1.0.0
 */
public class ProductTypeDTO {

    private Long id;

    /**
     * 类型编码
     */
    private String typeCode;

    /**
     * 类型名称
     */
    @NotBlank(message = "类型名称不能为空")
    private String typeName;

    /**
     * 父类型ID
     */
    private Long parentId;

    /**
     * 父类型名称（用于展示）
     */
    private String parentName;

    /**
     * 层级
     */
    private Integer level = 1;

    /**
     * 是否需要温控：1-是, 0-否
     */
    private Integer needTemperature = 0;

    /**
     * 是否危险品：1-是, 0-否
     */
    private Integer isHazardous = 0;

    /**
     * 是否易碎：1-是, 0-否
     */
    private Integer isFragile = 0;

    /**
     * 默认单位重量(kg)
     */
    private java.math.BigDecimal defaultUnitWeight;

    /**
     * 默认单位体积(m³)
     */
    private java.math.BigDecimal defaultUnitVolume;

    /**
     * 排序号
     */
    private Integer sortOrder = 0;

    /**
     * 状态：1-启用, 0-停用
     */
    private Integer status = 1;

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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
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
