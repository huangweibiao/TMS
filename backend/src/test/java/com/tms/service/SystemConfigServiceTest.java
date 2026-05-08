package com.tms.service;

import com.tms.entity.SystemConfig;
import com.tms.exception.BusinessException;
import com.tms.repository.SystemConfigRepository;
import com.tms.service.impl.SystemConfigServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SystemConfigServiceTest {

    @Mock
    private SystemConfigRepository systemConfigRepository;

    @InjectMocks
    private SystemConfigServiceImpl systemConfigService;

    private SystemConfig systemConfig;

    @BeforeEach
    void setUp() {
        systemConfig = new SystemConfig();
        systemConfig.setId(1L);
        systemConfig.setConfigKey("site.name");
        systemConfig.setConfigValue("TMS系统");
        systemConfig.setDescription("站点名称");
        systemConfig.setConfigGroup("basic");
    }

    @Test
    void createConfig_Success() {
        when(systemConfigRepository.existsByConfigKey("site.name")).thenReturn(false);
        when(systemConfigRepository.save(any(SystemConfig.class))).thenReturn(systemConfig);

        SystemConfig result = systemConfigService.createConfig(systemConfig);

        assertNotNull(result);
        assertEquals("TMS系统", result.getConfigValue());
        verify(systemConfigRepository).save(any(SystemConfig.class));
    }

    @Test
    void createConfig_DuplicateKey_ThrowsException() {
        when(systemConfigRepository.existsByConfigKey("site.name")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class,
                () -> systemConfigService.createConfig(systemConfig));
        assertEquals("配置键已存在", exception.getMessage());
    }

    @Test
    void updateConfig_Success() {
        SystemConfig updateData = new SystemConfig();
        updateData.setConfigValue("新名称");
        updateData.setDescription("新描述");

        when(systemConfigRepository.findById(1L)).thenReturn(Optional.of(systemConfig));
        when(systemConfigRepository.save(any(SystemConfig.class))).thenReturn(systemConfig);

        SystemConfig result = systemConfigService.updateConfig(1L, updateData);

        assertNotNull(result);
        verify(systemConfigRepository).save(any(SystemConfig.class));
    }

    @Test
    void updateConfig_NotFound_ThrowsException() {
        when(systemConfigRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> systemConfigService.updateConfig(1L, systemConfig));
        assertEquals("系统配置不存在", exception.getMessage());
    }

    @Test
    void deleteConfig_Success() {
        when(systemConfigRepository.findById(1L)).thenReturn(Optional.of(systemConfig));

        systemConfigService.deleteConfig(1L);

        verify(systemConfigRepository).deleteById(1L);
    }

    @Test
    void findById_Success() {
        when(systemConfigRepository.findById(1L)).thenReturn(Optional.of(systemConfig));

        Optional<SystemConfig> result = systemConfigService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("site.name", result.get().getConfigKey());
    }

    @Test
    void findByConfigKey_Success() {
        when(systemConfigRepository.findByConfigKey("site.name")).thenReturn(Optional.of(systemConfig));

        Optional<SystemConfig> result = systemConfigService.findByConfigKey("site.name");

        assertTrue(result.isPresent());
        assertEquals("TMS系统", result.get().getConfigValue());
    }

    @Test
    void getConfigValue_Success() {
        when(systemConfigRepository.findByConfigKey("site.name")).thenReturn(Optional.of(systemConfig));

        String result = systemConfigService.getConfigValue("site.name");

        assertEquals("TMS系统", result);
    }

    @Test
    void getConfigValue_NotFound_ReturnsDefault() {
        when(systemConfigRepository.findByConfigKey("missing.key")).thenReturn(Optional.empty());

        String result = systemConfigService.getConfigValue("missing.key", "default");

        assertEquals("default", result);
    }

    @Test
    void findByConfigGroup_Success() {
        when(systemConfigRepository.findByConfigGroup("basic")).thenReturn(Arrays.asList(systemConfig));

        List<SystemConfig> result = systemConfigService.findByConfigGroup("basic");

        assertEquals(1, result.size());
    }

    @Test
    void findAll_Success() {
        when(systemConfigRepository.findAll()).thenReturn(Arrays.asList(systemConfig));

        List<SystemConfig> result = systemConfigService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void refreshConfigCache_Success() {
        // refreshConfigCache just clears cache, no repository interaction
        assertDoesNotThrow(() -> systemConfigService.refreshConfigCache());
    }
}
