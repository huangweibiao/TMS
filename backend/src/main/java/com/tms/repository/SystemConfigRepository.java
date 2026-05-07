package com.tms.repository;

import com.tms.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置Repository接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {

    /**
     * 根据配置键查询
     *
     * @param configKey 配置键
     * @return 系统配置
     */
    Optional<SystemConfig> findByConfigKey(String configKey);

    /**
     * 根据分组查询
     *
     * @param configGroup 配置分组
     * @return 系统配置列表
     */
    List<SystemConfig> findByConfigGroup(String configGroup);

    /**
     * 根据状态查询
     *
     * @param status 状态
     * @return 系统配置列表
     */
    List<SystemConfig> findByStatus(Integer status);

    /**
     * 根据分组和状态查询
     *
     * @param configGroup 配置分组
     * @param status      状态
     * @return 系统配置列表
     */
    List<SystemConfig> findByConfigGroupAndStatus(String configGroup, Integer status);

    /**
     * 检查配置键是否存在
     *
     * @param configKey 配置键
     * @return 是否存在
     */
    boolean existsByConfigKey(String configKey);
}
