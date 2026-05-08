package com.tms.controller;

import com.tms.common.Result;
import com.tms.dto.PermissionDTO;
import com.tms.entity.Permission;
import com.tms.service.PermissionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 权限Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    private final PermissionService permissionService;

    @Autowired
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 创建权限
     *
     * @param dto 权限DTO
     * @return 创建结果
     */
    @PostMapping
    public Result<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO dto) {
        Permission permission = permissionService.createPermission(dto);
        return Result.success(convertToDTO(permission));
    }

    /**
     * 更新权限
     *
     * @param id  权限ID
     * @param dto 权限DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<PermissionDTO> updatePermission(@PathVariable Long id,
                                                  @Valid @RequestBody PermissionDTO dto) {
        Permission permission = permissionService.updatePermission(id, dto);
        return Result.success(convertToDTO(permission));
    }

    /**
     * 删除权限
     *
     * @param id 权限ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return Result.success();
    }

    /**
     * 根据ID查询权限
     *
     * @param id 权限ID
     * @return 权限信息
     */
    @GetMapping("/{id}")
    public Result<PermissionDTO> getPermissionById(@PathVariable Long id) {
        return permissionService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "权限不存在"));
    }

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限信息
     */
    @GetMapping("/by-code/{permissionCode}")
    public Result<PermissionDTO> getPermissionByCode(@PathVariable String permissionCode) {
        return permissionService.findByPermissionCode(permissionCode)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "权限不存在"));
    }

    /**
     * 查询所有权限列表
     *
     * @return 权限列表
     */
    @GetMapping("/list")
    public Result<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> list = permissionService.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 根据父权限ID查询权限列表
     *
     * @param parentId 父权限ID
     * @return 权限列表
     */
    @GetMapping("/by-parent/{parentId}")
    public Result<List<PermissionDTO>> getPermissionsByParent(@PathVariable Long parentId) {
        List<PermissionDTO> list = permissionService.findByParentId(parentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 根据权限类型查询权限列表
     *
     * @param permissionType 权限类型
     * @return 权限列表
     */
    @GetMapping("/by-type/{permissionType}")
    public Result<List<PermissionDTO>> getPermissionsByType(@PathVariable Integer permissionType) {
        List<PermissionDTO> list = permissionService.findByPermissionType(permissionType)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 启用权限
     *
     * @param id 权限ID
     * @return 操作结果
     */
    @PostMapping("/{id}/enable")
    public Result<PermissionDTO> enablePermission(@PathVariable Long id) {
        Permission permission = permissionService.enablePermission(id);
        return Result.success(convertToDTO(permission));
    }

    /**
     * 禁用权限
     *
     * @param id 权限ID
     * @return 操作结果
     */
    @PostMapping("/{id}/disable")
    public Result<PermissionDTO> disablePermission(@PathVariable Long id) {
        Permission permission = permissionService.disablePermission(id);
        return Result.success(convertToDTO(permission));
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 权限实体
     * @return 权限DTO
     */
    private PermissionDTO convertToDTO(Permission entity) {
        PermissionDTO dto = new PermissionDTO();
        dto.setId(entity.getId());
        dto.setPermissionCode(entity.getPermissionCode());
        dto.setPermissionName(entity.getPermissionName());
        dto.setPermissionType(entity.getPermissionType());
        dto.setParentId(entity.getParentId());
        dto.setPath(entity.getPath());
        dto.setSortOrder(entity.getSortOrder());
        dto.setStatus(entity.getStatus());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        return dto;
    }
}
