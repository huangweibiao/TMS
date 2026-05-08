package com.tms.service;

import com.tms.dto.RoleDTO;
import com.tms.entity.Role;
import com.tms.entity.RolePermission;
import com.tms.entity.UserRole;
import com.tms.exception.BusinessException;
import com.tms.repository.PermissionRepository;
import com.tms.repository.RolePermissionRepository;
import com.tms.repository.RoleRepository;
import com.tms.repository.UserRoleRepository;
import com.tms.service.impl.RoleServiceImpl;
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
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RolePermissionRepository rolePermissionRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;
    private RoleDTO roleDTO;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setRoleCode("ROLE_ADMIN");
        role.setRoleName("管理员");
        role.setDescription("系统管理员");
        role.setStatus(1);

        roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setRoleCode("ROLE_ADMIN");
        roleDTO.setRoleName("管理员");
        roleDTO.setDescription("系统管理员");
        roleDTO.setPermissionIds(Arrays.asList(1L, 2L));
    }

    @Test
    void testFindById_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(role.getId(), result.get().getId());
        verify(roleRepository).findById(1L);
    }

    @Test
    void testFindByRoleCode_Success() {
        when(roleRepository.findByRoleCode("ROLE_ADMIN")).thenReturn(Optional.of(role));

        Optional<Role> result = roleService.findByRoleCode("ROLE_ADMIN");

        assertTrue(result.isPresent());
        assertEquals(role.getRoleCode(), result.get().getRoleCode());
    }

    @Test
    void testFindAll() {
        when(roleRepository.findAll()).thenReturn(Arrays.asList(role));

        List<Role> result = roleService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testFindByStatus() {
        when(roleRepository.findByStatus(1)).thenReturn(Arrays.asList(role));

        List<Role> result = roleService.findByStatus(1);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateRole_Success() {
        when(roleRepository.existsByRoleCode("ROLE_ADMIN")).thenReturn(false);
        Role savedRole = new Role();
        savedRole.setId(1L);
        savedRole.setRoleCode("ROLE_ADMIN");
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(savedRole));
        when(permissionRepository.findById(any())).thenReturn(Optional.of(new com.tms.entity.Permission()));

        Role result = roleService.createRole(roleDTO);

        assertNotNull(result);
        assertEquals(role.getRoleCode(), result.getRoleCode());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void testCreateRole_CodeAlreadyExists() {
        when(roleRepository.existsByRoleCode("ROLE_ADMIN")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            roleService.createRole(roleDTO);
        });

        assertEquals("角色编码已存在", exception.getMessage());
        verify(roleRepository, never()).save(any());
    }

    @Test
    void testUpdateRole_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);
        when(permissionRepository.findById(any())).thenReturn(Optional.of(new com.tms.entity.Permission()));

        Role result = roleService.updateRole(1L, roleDTO);

        assertNotNull(result);
        verify(roleRepository, atLeast(1)).findById(1L);
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void testUpdateRole_NotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            roleService.updateRole(1L, roleDTO);
        });

        assertEquals("角色不存在", exception.getMessage());
    }

    @Test
    void testUpdateRole_CodeAlreadyExists() {
        Role existingRole = new Role();
        existingRole.setId(1L);
        existingRole.setRoleCode("ROLE_OLD");

        roleDTO.setRoleCode("ROLE_NEW");

        when(roleRepository.findById(1L)).thenReturn(Optional.of(existingRole));
        when(roleRepository.existsByRoleCode("ROLE_NEW")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            roleService.updateRole(1L, roleDTO);
        });

        assertEquals("角色编码已存在", exception.getMessage());
    }

    @Test
    void testDeleteRole_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRoleRepository.findByRoleId(1L)).thenReturn(Collections.emptyList());

        roleService.deleteRole(1L);

        verify(rolePermissionRepository).deleteByRoleId(1L);
        verify(roleRepository).deleteById(1L);
    }

    @Test
    void testDeleteRole_NotFound() {
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            roleService.deleteRole(1L);
        });

        assertEquals("角色不存在", exception.getMessage());
    }

    @Test
    void testDeleteRole_HasUsers() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRoleRepository.findByRoleId(1L)).thenReturn(Arrays.asList(new UserRole()));

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            roleService.deleteRole(1L);
        });

        assertEquals("该角色下存在用户，不能删除", exception.getMessage());
    }

    @Test
    void testEnableRole_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = roleService.enableRole(1L);

        assertNotNull(result);
        assertEquals(1, result.getStatus());
    }

    @Test
    void testDisableRole_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role result = roleService.disableRole(1L);

        assertNotNull(result);
        assertEquals(0, result.getStatus());
    }

    @Test
    void testUpdateRolePermissions_Success() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(permissionRepository.findById(any())).thenReturn(Optional.of(new com.tms.entity.Permission()));

        roleService.updateRolePermissions(1L, Arrays.asList(1L, 2L));

        verify(rolePermissionRepository).deleteByRoleId(1L);
        verify(rolePermissionRepository, times(2)).save(any(RolePermission.class));
    }

    @Test
    void testGetRolePermissionIds() {
        RolePermission rp1 = new RolePermission();
        rp1.setRoleId(1L);
        rp1.setPermissionId(1L);
        RolePermission rp2 = new RolePermission();
        rp2.setRoleId(1L);
        rp2.setPermissionId(2L);

        when(rolePermissionRepository.findByRoleId(1L)).thenReturn(Arrays.asList(rp1, rp2));

        List<Long> result = roleService.getRolePermissionIds(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
    }

    @Test
    void testGetRolesByUserId() {
        UserRole userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleId(1L);

        when(userRoleRepository.findByUserId(1L)).thenReturn(Arrays.asList(userRole));
        when(roleRepository.findAllById(Arrays.asList(1L))).thenReturn(Arrays.asList(role));

        List<Role> result = roleService.getRolesByUserId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
