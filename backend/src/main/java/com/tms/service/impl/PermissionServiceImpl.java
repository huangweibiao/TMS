package com.tms.service.impl;

import com.tms.dto.PermissionDTO;
import com.tms.entity.Permission;
import com.tms.exception.BusinessException;
import com.tms.repository.PermissionRepository;
import com.tms.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 权限Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    @Autowired
    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    @Transactional
    @CachePut(value = "permission", key = "#result.id")
    public Permission createPermission(PermissionDTO dto) {
        // 检查权限编码是否已存在
        Optional Optional<Permission> existingPermission = permissionRepository.findByPermissionCode(dto.getPermissionCode());
        if (existingPermission.isPresent()) {
            throw new BusinessException("权限编码已存在");
        }

        // 验证父权限是否存在
        if (dto.getParentId() != null) {
            permissionRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new BusinessException("父权限不存在"));
        }

        Permission permission = new Permission();
        permission.setPermissionCode(dto.getPermissionCode());
        permission.setPermissionName(dto.getPermissionName());
        permission.setPermissionType(dto.getPermissionType());
        permission.setParentId(dto.getParentId());
        permission.setPath(dto.getPath());
        permission.setSortOrder(dto.getSortOrder());
        permission.setStatus(1); // 启用

        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    @CachePut(value = "permission", key = "#id")
    public Permission updatePermission(Long id, PermissionDTO dto) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("权限不存在"));

        // 检查权限编码是否被其他权限使用
        Optional Optional<Permission> existingPermission = permissionRepository.findByPermissionCode(dto.getPermissionCode());
        if (existingPermission.isPresent() && !existingPermission.get().getId().equals(id)) {
            throw new BusinessException("权限编码已存在");
        }

        // 验证父权限是否存在，且不能将自己设为父权限
        if (dto.getParentId() != null) {
            if (dto.getParentId().equals(id)) {
                throw new BusinessException("不能将自己设为父权限");
            }
            permissionRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new BusinessException("父权限不存在"));
        }

        permission.setPermissionCode(dto.getPermissionCode());
        permission.setPermissionName(dto.getPermissionName());
        permission.setPermissionType(dto.getPermissionType());
        permission.setParentId(dto.getParentId());
        permission.setPath(dto.getPath());
        permission.setSortOrder(dto.getSortOrder());

        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    @CacheEvict(value = "permission", key = "#id")
    public void deletePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("权限不存在"));

        // 检查是否有子权限
        List List<Permission> children = permissionRepository.findByParentIdOrderBySortOrderAsc(id);
        if (!children.isEmpty()) {
            throw new BusinessException("该权限下存在子权限，不能删除");
        }

        permissionRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "permission", key = "#id")
    public Optional Optional<Permission> findById(Long id) {
        return permissionRepository.findById(id);
    }

    @Override
    @Cacheable(value = "permission", key = "#permissionCode")
    public Optional Optional<Permission> findByPermissionCode(String permissionCode) {
        return permissionRepository.findByPermissionCode(permissionCode);
    }

    @Override
    @Cacheable(value = "permission", key = "'all'")
    public List List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public List List<Permission> findByParentId(Long parentId) {
        return permissionRepository.findByParentIdOrderBySortOrderAsc(parentId);
    }

    @Override
    public List List<Permission> findByPermissionType(Integer permissionType) {
        return permissionRepository.findByPermissionType(permissionType);
    }

    @Override
    public List List<Permission> findByStatus(Integer status) {
        return permissionRepository.findByStatus(status);
    }

    @Override
    @Transactional
    @CachePut(value = "permission", key = "#id")
    public Permission enablePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("权限不存在"));

        permission.setStatus(1);
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    @CachePut(value = "permission", key = "#id")
    public Permission disablePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new BusinessException("权限不存在"));

        permission.setStatus(0);
        return permissionRepository.save(permission);
    }
}
