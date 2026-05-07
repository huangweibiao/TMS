package com.tms.controller;

import com.tms.common.PageResult;
import com.tms.common.Result;
import com.tms.dto.UserDTO;
import com.tms.entity.User;
import com.tms.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户Controller
 *
 * @author TMS Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 创建用户
     *
     * @param dto 用户DTO
     * @return 创建结果
     */
    @PostMapping
    public Result<UserDTO> createUser(@Valid @RequestBody UserDTO dto) {
        User user = userService.createUser(dto);
        return Result.success(convertToDTO(user));
    }

    /**
     * 更新用户
     *
     * @param id  用户ID
     * @param dto 用户DTO
     * @return 更新结果
     */
    @PutMapping("/{id}")
    public Result<UserDTO> updateUser(@PathVariable Long id,
                                      @Valid @RequestBody UserDTO dto) {
        User user = userService.updateUser(id, dto);
        return Result.success(convertToDTO(user));
    }

    /**
     * 删除用户
     *
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public Result<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "用户不存在"));
    }

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    @GetMapping("/by-username/{username}")
    public Result<UserDTO> getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username)
                .map(this::convertToDTO)
                .map(Result::success)
                .orElse(Result.error(404, "用户不存在"));
    }

    /**
     * 分页查询用户列表
     *
     * @param username 用户名（模糊查询）
     * @param status   状态
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<UserDTO>> getUserList(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<User> page = userService.findUsers(username, status, pageable);

        List<UserDTO> list = page.getContent()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return Result.success(new PageResult<>(pageNum, pageSize, page.getTotalElements(), list));
    }

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    @GetMapping("/all")
    public Result<List<UserDTO>> getAllUsers() {
        List<UserDTO> list = userService.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return Result.success(list);
    }

    /**
     * 启用用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @PostMapping("/{id}/enable")
    public Result<UserDTO> enableUser(@PathVariable Long id) {
        User user = userService.enableUser(id);
        return Result.success(convertToDTO(user));
    }

    /**
     * 禁用用户
     *
     * @param id 用户ID
     * @return 操作结果
     */
    @PostMapping("/{id}/disable")
    public Result<UserDTO> disableUser(@PathVariable Long id) {
        User user = userService.disableUser(id);
        return Result.success(convertToDTO(user));
    }

    /**
     * 重置密码
     *
     * @param id     用户ID
     * @param params 包含password的请求参数
     * @return 操作结果
     */
    @PostMapping("/{id}/reset-password")
    public Result<UserDTO> resetPassword(@PathVariable Long id,
                                         @RequestBody Map<String, String> params) {
        String password = params.get("password");
        User user = userService.resetPassword(id, password);
        return Result.success(convertToDTO(user));
    }

    /**
     * 修改密码
     *
     * @param params         包含oldPassword和newPassword的请求参数
     * @param authentication 当前用户认证信息
     * @return 操作结果
     */
    @PostMapping("/change-password")
    public Result<Map<String, Boolean>> changePassword(@RequestBody Map<String, String> params,
                                                       Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        String oldPassword = params.get("oldPassword");
        String newPassword = params.get("newPassword");

        boolean success = userService.changePassword(user.getId(), oldPassword, newPassword);
        Map<String, Boolean> result = new HashMap<>();
        result.put("success", success);
        return Result.success(result);
    }

    /**
     * 更新用户角色
     *
     * @param id      用户ID
     * @param roleIds 角色ID列表
     * @return 操作结果
     */
    @PostMapping("/{id}/roles")
    public Result<Void> updateUserRoles(@PathVariable Long id,
                                        @RequestBody List<Long> roleIds) {
        userService.updateUserRoles(id, roleIds);
        return Result.success();
    }

    /**
     * 获取用户角色
     *
     * @param id 用户ID
     * @return 角色ID列表
     */
    @GetMapping("/{id}/roles")
    public Result<List<Long>> getUserRoles(@PathVariable Long id) {
        List<Long> roleIds = userService.getUserRoleIds(id);
        return Result.success(roleIds);
    }

    /**
     * 将实体转换为DTO
     *
     * @param entity 用户实体
     * @return 用户DTO
     */
    private UserDTO convertToDTO(User entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setRealName(entity.getRealName());
        dto.setPhone(entity.getPhone());
        dto.setEmail(entity.getEmail());
        dto.setStatus(entity.getStatus());
        dto.setLastLoginTime(entity.getLastLoginTime());
        dto.setLastLoginIp(entity.getLastLoginIp());
        dto.setCreateTime(entity.getCreateTime());
        dto.setUpdateTime(entity.getUpdateTime());
        // 获取用户角色
        dto.setRoleIds(userService.getUserRoleIds(entity.getId()));
        return dto;
    }
}

