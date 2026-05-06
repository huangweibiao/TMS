package com.tms.entity;

import jakarta.persistence.*;

/**
 * 角色实体类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Entity
@Table(name = "tms_role")
public class Role extends BaseEntity {

    /**
     * 角色编码
     */
    @Column(name = "role_code", nullable = false, unique = true, length = 50)
    private String roleCode;

    /**
     * 角色名称
     */
    @Column(name = "role_name", nullable = false, length = 50)
    private String roleName;

    /**
     * 角色描述
     */
    @Column(name = "description", length = 200)
    private String description;

    /**
     * 状态：1-启用，0-禁用
     */
    @Column(name = "status", nullable = false)
    private Integer status = 1;

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
