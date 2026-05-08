package com.tms.service;

import com.tms.dto.RoleDTO;
import com.tms.entity.Role;

import java.util.List;
import java.util.Optional;

/**
 * 角色Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface RoleService {

    /**
     * 创建角色
     *
     * @param dto 角色DTO
     * @return 创建后的角色
     */
    Role createRole(RoleDTO dto);

    /**
     * 更新角色
     *
     * @param id  角色ID
     * @param dto 角色DTO
     * @return 更新后的角色
     */
    Role updateRole(Long id, RoleDTO dto);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void deleteRole(Long id);

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色对象
     */
    Optional<Role> findById(Long id);

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色对象
     */
    Optional<Role> findByRoleCode(String roleCode);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<Role> findAll();

    /**
     * 根据状态查询角色列表
     *
     * @param status 状态
     * @return 角色列表
     */
    List<Role> findByStatus(Integer status);

    /**
     * 启用角色
     *
     * @param id 角色ID
     * @return 更新后的角色
     */
    Role enableRole(Long id);

    /**
     * 禁用角色
     *
     * @param id 角色ID
     * @return 更新后的角色
     */
    Role disableRole(Long id);

    /**
     * 更新角色权限
     *
     * @param id            角色ID
     * @param permissionIds 权限ID列表
     */
    void updateRolePermissions(Long id, List<Long> permissionIds);

    /**
     * 获取角色权限ID列表
     *
     * @param id 角色ID
     * @return 权限ID列表
     */
    List<Long> getRolePermissionIds(Long id);

    /**
     * 获取用户的角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> getRolesByUserId(Long userId);
}
