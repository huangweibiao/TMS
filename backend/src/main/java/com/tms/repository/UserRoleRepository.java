package com.tms.repository;

import com.tms.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 用户角色关联Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    /**
     * 根据用户ID查询角色关联列表
     *
     * @param userId 用户ID
     * @return 关联列表
     */
    List<UserRole> findByUserId(Long userId);

    /**
     * 根据角色ID查询用户关联列表
     *
     * @param roleId 角色ID
     * @return 关联列表
     */
    List<UserRole> findByRoleId(Long roleId);

    /**
     * 删除用户的所有角色关联
     *
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);
}
