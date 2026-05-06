package com.tms.repository;

import com.tms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 角色Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface RoleRepository extends JpaRepositoryRepository<Role, Long> {

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色对象
     */
    Optional Optional<Role> findByRoleCode(String roleCode);

    /**
     * 检查角色编码是否存在
     *
     * @param roleCode 角色编码
     * @return 是否存在
     */
    boolean existsByRoleCode(String roleCode);
}
