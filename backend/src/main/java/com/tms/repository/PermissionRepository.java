package com.tms.repository;

import com.tms.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 权限Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限对象
     */
    Optional<Permission> findByPermissionCode(String permissionCode);

    /**
     * 根据父权限ID查询权限列表
     *
     * @param parentId 父权限ID
     * @return 权限列表
     */
    List<Permission> findByParentIdOrderBySortOrderAsc(Long parentId);

    /**
     * 根据权限类型查询权限列表
     *
     * @param permissionType 权限类型
     * @return 权限列表
     */
    List<Permission> findByPermissionType(Integer permissionType);

    /**
     * 根据状态查询权限列表
     *
     * @param status 状态
     * @return 权限列表
     */
    List<Permission> findByStatus(Integer status);
}
