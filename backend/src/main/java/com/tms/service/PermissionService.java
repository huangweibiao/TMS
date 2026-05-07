package com.tms.service;

import com.tms.dto.PermissionDTO;
import com.tms.entity.Permission;

import java.util.List;
import java.util.Optional;

/**
 * 权限Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface PermissionService {

    /**
     * 创建权限
     *
     * @param dto 权限DTO
     * @return 创建后的权限
     */
    Permission createPermission(PermissionDTO dto);

    /**
     * 更新权限
     *
     * @param id  权限ID
     * @param dto 权限DTO
     * @return 更新后的权限
     */
    Permission updatePermission(Long id, PermissionDTO dto);

    /**
     * 删除权限
     *
     * @param id 权限ID
     */
    void deletePermission(Long id);

    /**
     * 根据ID查询权限
     *
     * @param id 权限ID
     * @return 权限对象
     */
    Optional Optional<Permission> findById(Long id);

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限对象
     */
    Optional Optional<Permission> findByPermissionCode(String permissionCode);

    /**
     * 查询所有权限列表
     *
     * @return 权限列表
     */
    List List<Permission> findAll();

    /**
     * 根据父权限ID查询权限列表
     *
     * @param parentId 父权限ID
     * @return 权限列表
     */
    List List<Permission> findByParentId(Long parentId);

    /**
     * 根据权限类型查询权限列表
     *
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List List<Permission> findByPermissionType(Integer permissionType);

    /**
     * 根据状态查询权限列表
     *
     * @param status 状态
     * @return 权限列表
     */
    List List<Permission> findByStatus(Integer status);

    /**
     * 启用权限
     *
     * @param id 权限ID
     * @return 更新后的权限
     */
    Permission enablePermission(Long id);

    /**
     * 禁用权限
     *
     * @param id 权限ID
     * @return 更新后的权限
     */
    Permission disablePermission(Long id);
}
