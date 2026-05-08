package com.tms.service.impl;

import com.tms.dto.RoleDTO;
import com.tms.entity.Role;
import com.tms.entity.RolePermission;
import com.tms.entity.UserRole;
import com.tms.exception.BusinessException;
import com.tms.repository.PermissionRepository;
import com.tms.repository.RolePermissionRepository;
import com.tms.repository.RoleRepository;
import com.tms.repository.UserRoleRepository;
import com.tms.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository,
                           RolePermissionRepository rolePermissionRepository,
                           PermissionRepository permissionRepository,
                           UserRoleRepository userRoleRepository) {
        this.roleRepository = roleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.permissionRepository = permissionRepository;
        this.userRoleRepository = userRoleRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "role", key = "#result.id")
    public Role createRole(RoleDTO dto) {
        // 检查角色编码是否已存在
        if (roleRepository.existsByRoleCode(dto.getRoleCode())) {
            throw new BusinessException("角色编码已存在");
        }

        Role role = new Role();
        role.setRoleCode(dto.getRoleCode());
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());
        role.setStatus(1); // 启用

        Role savedRole = roleRepository.save(role);

        // 保存角色权限关联
        if (dto.getPermissionIds() != null && !dto.getPermissionIds().isEmpty()) {
            updateRolePermissions(savedRole.getId(), dto.getPermissionIds());
        }

        return savedRole;
    }

    @Override
    @Transactional
    @CachePut(value = "role", key = "#id")
    public Role updateRole(Long id, RoleDTO dto) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        // 检查角色编码是否被其他角色使用
        if (!role.getRoleCode().equals(dto.getRoleCode()) && roleRepository.existsByRoleCode(dto.getRoleCode())) {
            throw new BusinessException("角色编码已存在");
        }

        role.setRoleCode(dto.getRoleCode());
        role.setRoleName(dto.getRoleName());
        role.setDescription(dto.getDescription());

        Role savedRole = roleRepository.save(role);

        // 更新角色权限关联
        if (dto.getPermissionIds() != null) {
            updateRolePermissions(savedRole.getId(), dto.getPermissionIds());
        }

        return savedRole;
    }

    @Override
    @Transactional
    @CacheEvict(value = "role", key = "#id")
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        // 检查是否有用户关联此角色
        List<UserRole> userRoles = userRoleRepository.findByRoleId(id);
        if (!userRoles.isEmpty()) {
            throw new BusinessException("该角色下存在用户，不能删除");
        }

        // 删除角色权限关联
        rolePermissionRepository.deleteByRoleId(id);

        // 删除角色
        roleRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "role", key = "#id")
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    @Override
    @Cacheable(value = "role", key = "#roleCode")
    public Optional<Role> findByRoleCode(String roleCode) {
        return roleRepository.findByRoleCode(roleCode);
    }

    @Override
    @Cacheable(value = "role", key = "'all'")
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public List<Role> findByStatus(Integer status) {
        return roleRepository.findByStatus(status);
    }

    @Override
    @Transactional
    @CachePut(value = "role", key = "#id")
    public Role enableRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        role.setStatus(1);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    @CachePut(value = "role", key = "#id")
    public Role disableRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        role.setStatus(0);
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public void updateRolePermissions(Long id, List<Long> permissionIds) {
        // 验证角色是否存在
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new BusinessException("角色不存在"));

        // 验证权限是否存在
        for (Long permissionId : permissionIds) {
            permissionRepository.findById(permissionId)
                    .orElseThrow(() -> new BusinessException("权限不存在: " + permissionId));
        }

        // 删除原有权限关联
        rolePermissionRepository.deleteByRoleId(id);

        // 创建新的权限关联
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(id);
            rolePermission.setPermissionId(permissionId);
            rolePermissionRepository.save(rolePermission);
        }
    }

    @Override
    public List<Long> getRolePermissionIds(Long id) {
        return rolePermissionRepository.findByRoleId(id).stream()
                .map(RolePermission::getPermissionId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> getRolesByUserId(Long userId) {
        List<Long> roleIds = userRoleRepository.findByUserId(userId).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());

        return roleRepository.findAllById(roleIds);
    }
}
