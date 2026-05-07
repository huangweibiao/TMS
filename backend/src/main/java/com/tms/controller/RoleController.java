package com.tms.controller;

import com.tms.common.Result;
import com.tms.dto.RoleDTO;
import com.tms.entity.Role;
import com.tms.service.RoleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/role")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 创建角色
     *
     * @param dto 角色DTO
     * @return 创建结果
     */
    @PostMapping
    public Result<RoleDTO> createRole(@Valid @RequestBody RoleDTO dto) {
        Role role = roleService.createRole(dto);
        return Result.success(convertToDTO(role));
    }

    /**
     * 更新角色
     *
     * @param id  角色ID
     * @param dto 角色DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<RoleDTO> updateRole(@PathVariable Long id,
                                      @Valid @RequestBody RoleDTO dto) {
        Role role = roleService.updateRole(id, dto);
        return Result.success(convertToDTO(role));
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return Result.success();
    }

    /**
     * 根据ID查询角色
     *
     * @param id 角色ID
     * @return 角色信息
     */
    @GetMapping("/{id}")
    public Result<RoleDTO> getRoleById(@PathVariable Long id) {
        return roleService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "角色不存在"));
    }

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色信息
     */
    @GetMapping("/by-code/{roleCode}")
    public Result<RoleDTO> getRoleByCode(@PathVariable String roleCode) {
        return roleService.findByRoleCode(roleCode)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "角色不存在"));
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> list = roleService.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 根据状态查询角色列表
     *
     * @param status 状态
     * @return 角色列表
     */
    @GetMapping("/by-status/{status}")
    public Result<List<RoleDTO>> getRolesByStatus(@PathVariable Integer status) {
        List<RoleDTO> list = roleService.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 启用角色
     *
     * @param id 角色ID
     * @return 操作结果
     */
    @PostMapping("/{id}/enable")
    public Result<RoleDTO> enableRole(@PathVariable Long id) {
        Role role = roleService.enableRole(id);
        return Result.success(convertToDTO(role));
    }

    /**
     * 禁用角色
     *
     * @param id 角色ID
     * @return 操作结果
     */
    @PostMapping("/{id}/disable")
    public Result<RoleDTO> disableRole(@PathVariable Long id) {
        Role role = roleService.disableRole(id);
        return Result.success(convertToDTO(role));
    }

    /**
     * 更新角色权限
     *
     * @param id            角色ID
     * @param permissionIds 权限ID列表
     * @return 操作结果
     */
    @PostMapping("/{id}/permissions")
    public Result<Void> updateRolePermissions(@PathVariable Long id,
                                              @RequestBody List<Long> permissionIds) {
        roleService.updateRolePermissions(id, permissionIds);
        return Result.success();
    }

    /**
     * 获取角色权限
     *
     * @param id 角色ID
     * @return 权限ID列表
     */
    @GetMapping("/{id}/permissions")
    public Result<List<Long>> getRolePermissions(@PathVariable Long id) {
        List<Long> permissionIds = roleService.getRolePermissionIds(id);
        return Result.success(permissionIds);
    }

    /**
     * 获取用户角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    @GetMapping("/by-user/{userId}")
    public Result<List<RoleDTO>> getRolesByUser(@PathVariable Long userId) {
        List<RoleDTO> list = roleService.getRolesByUserId(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 角色实体
     * @return 角色DTO
     */
    private RoleDTO convertToDTO(Role entity) {
        RoleDTO dto = new RoleDTO();
        dto.setId(entity.getId());
        dto.setRoleCode(entity.getRoleCode());
        dto.setRoleName(entity.getRoleName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        // 获取角色权限
        dto.setPermissionIds(roleService.getRolePermissionIds(entity.getId()));
        return dto;
    }
}

