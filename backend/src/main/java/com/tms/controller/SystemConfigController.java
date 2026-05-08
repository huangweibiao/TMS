package com.tms.controller;

import com.tms.common.Result;
import com.tms.entity.SystemConfig;
import com.tms.service.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统配置Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/v1/system-configs")
public class SystemConfigController {

    private final SystemConfigService systemConfigService;

    @Autowired
    public SystemConfigController(SystemConfigService systemConfigService) {
        this.systemConfigService = systemConfigService;
    }

    /**
     * 创建配置
     */
    @PostMapping
    public Result<SystemConfig> create(@RequestBody SystemConfig systemConfig) {
        return Result.success(systemConfigService.createConfig(systemConfig));
    }

    /**
     * 更新配置
     */
    @PutMapping("/{id}")
    public Result<SystemConfig> update(@PathVariable Long id, @RequestBody SystemConfig systemConfig) {
        return Result.success(systemConfigService.updateConfig(id, systemConfig));
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        systemConfigService.deleteConfig(id);
        return Result.success();
    }

    /**
     * 根据ID查询
     */
    @GetMapping("/{id}")
    public Result<SystemConfig> getById(@PathVariable Long id) {
        return systemConfigService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "配置不存在"));
    }

    /**
     * 根据配置键查询
     */
    @GetMapping("/key/{configKey}")
    public Result<SystemConfig> getByKey(@PathVariable String configKey) {
        return systemConfigService.findByConfigKey(configKey)
                .map(Result::success)
                .orElse(Result.error(404, "配置不存在"));
    }

    /**
     * 查询所有配置
     */
    @GetMapping
    public Result<List<SystemConfig>> list() {
        return Result.success(systemConfigService.findAll());
    }

    /**
     * 根据分组查询
     */
    @GetMapping("/group/{configGroup}")
    public Result<List<SystemConfig>> getByGroup(@PathVariable String configGroup) {
        return Result.success(systemConfigService.findByConfigGroup(configGroup));
    }

    /**
     * 刷新配置缓存
     */
    @PostMapping("/refresh-cache")
    public Result<Void> refreshCache() {
        systemConfigService.refreshConfigCache();
        return Result.success();
    }
}
