package com.tms.service.impl;

import com.tms.entity.SystemConfig;
import com.tms.exception.BusinessException;
import com.tms.repository.SystemConfigRepository;
import com.tms.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 系统配置Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Autowired
    public SystemConfigServiceImpl(SystemConfigRepository systemConfigRepository) {
        this.systemConfigRepository = systemConfigRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "systemConfig", key = "#result.configKey")
    public SystemConfig createConfig(SystemConfig systemConfig) {
        // 检查配置键是否已存在
        if (systemConfigRepository.existsByConfigKey(systemConfig.getConfigKey())) {
            throw new BusinessException("配置键已存在");
        }

        systemConfig.setStatus(1);
        return systemConfigRepository.save(systemConfig);
    }

    @Override
    @Transactional
    @CachePut(value = "systemConfig", key = "#result.configKey")
    public SystemConfig updateConfig(Long id, SystemConfig systemConfig) {
        SystemConfig existing = systemConfigRepository.findById(id)
                .orElseThrow(() -> new BusinessException("系统配置不存在"));

        // 检查是否可编辑
        if (existing.getIsEditable() != null && existing.getIsEditable() == 0) {
            throw new BusinessException("该配置项不允许编辑");
        }

        // 检查配置键是否被其他配置使用
        if (!existing.getConfigKey().equals(systemConfig.getConfigKey())
                && systemConfigRepository.existsByConfigKey(systemConfig.getConfigKey())) {
            throw new BusinessException("配置键已存在");
        }

        existing.setConfigKey(systemConfig.getConfigKey());
        existing.setConfigValue(systemConfig.getConfigValue());
        existing.setConfigName(systemConfig.getConfigName());
        existing.setDescription(systemConfig.getDescription());
        existing.setConfigType(systemConfig.getConfigType());
        existing.setConfigGroup(systemConfig.getConfigGroup());
        existing.setSortOrder(systemConfig.getSortOrder());

        return systemConfigRepository.save(existing);
    }

    @Override
    @Transactional
    @CacheEvict(value = "systemConfig", key = "#id")
    public void deleteConfig(Long id) {
        SystemConfig systemConfig = systemConfigRepository.findById(id)
                .orElseThrow(() -> new BusinessException("系统配置不存在"));

        // 检查是否可编辑（不可编辑的也不能删除）
        if (systemConfig.getIsEditable() != null && systemConfig.getIsEditable() == 0) {
            throw new BusinessException("该配置项不允许删除");
        }

        systemConfigRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "systemConfig", key = "#id")
    public Optional<SystemConfig> findById(Long id) {
        return systemConfigRepository.findById(id);
    }

    @Override
    @Cacheable(value = "systemConfig", key = "#configKey")
    public Optional<SystemConfig> findByConfigKey(String configKey) {
        return systemConfigRepository.findByConfigKey(configKey);
    }

    @Override
    public String getConfigValue(String configKey) {
        return findByConfigKey(configKey)
                .map(SystemConfig::getConfigValue)
                .orElse(null);
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        String value = getConfigValue(configKey);
        return value != null ? value : defaultValue;
    }

    @Override
    @Cacheable(value = "systemConfig", key = "'all'")
    public List<SystemConfig> findAll() {
        return systemConfigRepository.findAll();
    }

    @Override
    public List<SystemConfig> findByConfigGroup(String configGroup) {
        return systemConfigRepository.findByConfigGroup(configGroup);
    }

    @Override
    @CacheEvict(value = "systemConfig", allEntries = true)
    public void refreshConfigCache() {
        // 清空缓存后，下次查询会自动重新加载
    }
}
