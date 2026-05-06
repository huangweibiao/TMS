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
public interface PermissionRepository extends JpaRepositoryRepository<Permission, Long> {

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限对象
     */
    Optional Optional<Permission> findByPermissionCode(String permissionCode);

    /**
     * 根据父权限ID查询权限列表
     *
     * @param parentId 父权限ID
     * @return 权限列表
     */
    List List<Permission> findByParentIdOrderBySortOrderAsc(Long parentId);
}
