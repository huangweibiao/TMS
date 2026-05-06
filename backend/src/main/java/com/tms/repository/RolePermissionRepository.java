package com.tms.repository;

import com.tms.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色权限关联Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface RolePermissionRepository extends JpaRepositoryRepository<RolePermission, Long> {

    /**
     * 根据角色ID查询权限关联列表
     *
     * @param roleId 角色ID
     * @return 关联列表
     */
    List List<RolePermission> findByRoleId(Long roleId);

    /**
     * 删除角色的所有权限关联
     *
     * @param roleId 角色ID
     */
    void deleteByRoleId(Long roleId);
}
