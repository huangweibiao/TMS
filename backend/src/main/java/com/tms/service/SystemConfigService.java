package com.tms.service;

import com.tms.entity.SystemConfig;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface SystemConfigService {

    /**
     * 创建系统配置
     *
     * @param systemConfig 系统配置
     * @return 创建后的配置
     */
    SystemConfig createConfig(SystemConfig systemConfig);

    /**
     * 更新系统配置
     *
     * @param id           配置ID
     * @param systemConfig 系统配置
     * @return 更新后的配置
     */
    SystemConfig updateConfig(Long id, SystemConfig systemConfig);

    /**
     * 删除系统配置
     *
     * @param id 配置ID
     */
    void deleteConfig(Long id);

    /**
     * 根据ID查询配置
     *
     * @param id 配置ID
     * @return 系统配置
     */
    Optional<SystemConfig> findById(Long id);

    /**
     * 根据配置键查询
     *
     * @param configKey 配置键
     * @return 系统配置
     */
    Optional<SystemConfig> findByConfigKey(String configKey);

    /**
     * 根据配置键获取配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    String getConfigValue(String configKey);

    /**
     * 根据配置键获取配置值（带默认值）
     *
     * @param configKey    配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * 查询所有配置
     *
     * @return 配置列表
     */
    List<SystemConfig> findAll();

    /**
     * 根据分组查询配置
     *
     * @param configGroup 配置分组
     * @return 配置列表
     */
    List<SystemConfig> findByConfigGroup(String configGroup);

    /**
     * 刷新配置缓存
     */
    void refreshConfigCache();
}
