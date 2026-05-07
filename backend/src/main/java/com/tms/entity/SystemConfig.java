package com.tms.entity;

import jakarta.persistence.*;

/**
 * 系统配置实体类
 * 用于存储系统参数配置
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_system_config")
public class SystemConfig extends BaseEntity {

    /**
     * 配置键
     */
    @Column(name = "config_key", nullable = false, unique = true, length = 50)
    private String configKey;

    /**
     * 配置值
     */
    @Column(name = "config_value", nullable = false, length = 500)
    private String configValue;

    /**
     * 配置名称
     */
    @Column(name = "config_name", length = 100)
    private String configName;

    /**
     * 配置描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 配置类型：1-字符串, 2-数字, 3-布尔值, 4-JSON
     */
    @Column(name = "config_type")
    private Integer configType = 1;

    /**
     * 是否可编辑：1-是, 0-否
     */
    @Column(name = "is_editable")
    private Integer isEditable = 1;

    /**
     * 分组
     */
    @Column(name = "config_group", length = 50)
    private String configGroup;

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

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getConfigType() {
        return configType;
    }

    public void setConfigType(Integer configType) {
        this.configType = configType;
    }

    public Integer getIsEditable() {
        return isEditable;
    }

    public void setIsEditable(Integer isEditable) {
        this.isEditable = isEditable;
    }

    public String getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup;
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
}
