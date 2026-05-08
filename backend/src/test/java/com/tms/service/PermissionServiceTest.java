package com.tms.service;

import com.tms.dto.PermissionDTO;
import com.tms.entity.Permission;
import com.tms.exception.BusinessException;
import com.tms.repository.PermissionRepository;
import com.tms.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private Permission permission;
    private PermissionDTO permissionDTO;

    @BeforeEach
    void setUp() {
        permission = new Permission();
        permission.setId(1L);
        permission.setPermissionCode("user:create");
        permission.setPermissionName("创建用户");
        permission.setPermissionType(1);
        permission.setParentId(null);
        permission.setPath("/user/create");
        permission.setSortOrder(1);
        permission.setStatus(1);

        permissionDTO = new PermissionDTO();
        permissionDTO.setId(1L);
        permissionDTO.setPermissionCode("user:create");
        permissionDTO.setPermissionName("创建用户");
        permissionDTO.setPermissionType(1);
        permissionDTO.setParentId(null);
        permissionDTO.setPath("/user/create");
        permissionDTO.setSortOrder(1);
    }

    @Test
    void testFindById_Success() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));

        Optional<Permission> result = permissionService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(permission.getId(), result.get().getId());
        verify(permissionRepository).findById(1L);
    }

    @Test
    void testFindByPermissionCode_Success() {
        when(permissionRepository.findByPermissionCode("user:create")).thenReturn(Optional.of(permission));

        Optional<Permission> result = permissionService.findByPermissionCode("user:create");

        assertTrue(result.isPresent());
        assertEquals(permission.getPermissionCode(), result.get().getPermissionCode());
    }

    @Test
    void testFindAll() {
        when(permissionRepository.findAll()).thenReturn(Arrays.asList(permission));

        List<Permission> result = permissionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByParentId() {
        when(permissionRepository.findByParentIdOrderBySortOrderAsc(0L)).thenReturn(Arrays.asList(permission));

        List<Permission> result = permissionService.findByParentId(0L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByPermissionType() {
        when(permissionRepository.findByPermissionType(1)).thenReturn(Arrays.asList(permission));

        List<Permission> result = permissionService.findByPermissionType(1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByStatus() {
        when(permissionRepository.findByStatus(1)).thenReturn(Arrays.asList(permission));

        List<Permission> result = permissionService.findByStatus(1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreatePermission_Success() {
        when(permissionRepository.findByPermissionCode("user:create")).thenReturn(Optional.empty());
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        Permission result = permissionService.createPermission(permissionDTO);

        assertNotNull(result);
        assertEquals(permission.getPermissionCode(), result.getPermissionCode());
        verify(permissionRepository).save(any(Permission.class));
    }

    @Test
    void testCreatePermission_CodeAlreadyExists() {
        when(permissionRepository.findByPermissionCode("user:create")).thenReturn(Optional.of(permission));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            permissionService.createPermission(permissionDTO);
        });

        assertEquals("权限编码已存在", exception.getMessage());
        verify(permissionRepository, never()).save(any());
    }

    @Test
    void testCreatePermission_WithParent() {
        permissionDTO.setParentId(2L);
        Permission parent = new Permission();
        parent.setId(2L);

        when(permissionRepository.findByPermissionCode("user:create")).thenReturn(Optional.empty());
        when(permissionRepository.findById(2L)).thenReturn(Optional.of(parent));
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        Permission result = permissionService.createPermission(permissionDTO);

        assertNotNull(result);
        verify(permissionRepository).findById(2L);
    }

    @Test
    void testCreatePermission_ParentNotFound() {
        permissionDTO.setParentId(2L);

        when(permissionRepository.findByPermissionCode("user:create")).thenReturn(Optional.empty());
        when(permissionRepository.findById(2L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            permissionService.createPermission(permissionDTO);
        });

        assertEquals("父权限不存在", exception.getMessage());
    }

    @Test
    void testUpdatePermission_Success() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.findByPermissionCode("user:create")).thenReturn(Optional.of(permission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        Permission result = permissionService.updatePermission(1L, permissionDTO);

        assertNotNull(result);
        verify(permissionRepository).findById(1L);
        verify(permissionRepository).save(any(Permission.class));
    }

    @Test
    void testUpdatePermission_NotFound() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            permissionService.updatePermission(1L, permissionDTO);
        });

        assertEquals("权限不存在", exception.getMessage());
    }

    @Test
    void testUpdatePermission_CodeAlreadyExists() {
        Permission existingPermission = new Permission();
        existingPermission.setId(2L);
        existingPermission.setPermissionCode("user:update");

        permissionDTO.setPermissionCode("user:update");

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.findByPermissionCode("user:update")).thenReturn(Optional.of(existingPermission));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            permissionService.updatePermission(1L, permissionDTO);
        });

        assertEquals("权限编码已存在", exception.getMessage());
    }

    @Test
    void testUpdatePermission_SelfAsParent() {
        permissionDTO.setParentId(1L);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.findByPermissionCode("user:create")).thenReturn(Optional.of(permission));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            permissionService.updatePermission(1L, permissionDTO);
        });

        assertEquals("不能将自己设为父权限", exception.getMessage());
    }

    @Test
    void testDeletePermission_Success() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.findByParentIdOrderBySortOrderAsc(1L)).thenReturn(Collections.emptyList());

        permissionService.deletePermission(1L);

        verify(permissionRepository).deleteById(1L);
    }

    @Test
    void testDeletePermission_NotFound() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            permissionService.deletePermission(1L);
        });

        assertEquals("权限不存在", exception.getMessage());
    }

    @Test
    void testDeletePermission_HasChildren() {
        Permission child = new Permission();
        child.setId(2L);
        child.setParentId(1L);

        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.findByParentIdOrderBySortOrderAsc(1L)).thenReturn(Arrays.asList(child));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            permissionService.deletePermission(1L);
        });

        assertEquals("该权限下存在子权限，不能删除", exception.getMessage());
    }

    @Test
    void testEnablePermission_Success() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        Permission result = permissionService.enablePermission(1L);

        assertNotNull(result);
        assertEquals(1, result.getStatus());
    }

    @Test
    void testDisablePermission_Success() {
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        Permission result = permissionService.disablePermission(1L);

        assertNotNull(result);
        assertEquals(0, result.getStatus());
    }
}
