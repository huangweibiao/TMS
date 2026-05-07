package com.tms.service;

import com.tms.dto.UserDTO;
import com.tms.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * 用户Service接口
 *
 * @author TMS Team
 * @version 1.0.0
 */
public interface UserService {

    /**
     * 创建用户
     *
     * @param dto 用户DTO
     * @return 创建后的用户
     */
    User createUser(UserDTO dto);

    /**
     * 更新用户
     *
     * @param id  用户ID
     * @param dto 用户DTO
     * @return 更新后的用户
     */
    User updateUser(Long id, UserDTO dto);

    /**
     * 删除用户
     *
     * @param id 用户ID
     */
    void deleteUser(Long id);

    /**
     * 根据ID查询用户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    Optional<User> findById(Long id);

    /**
     * 根据用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象
     */
    Optional<User> findByUsername(String username);

    /**
     * 分页查询用户列表
     *
     * @param username 用户名（模糊查询）
     * @param status   状态
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<User> findUsers(String username, Integer status, Pageable pageable);

    /**
     * 查询所有用户
     *
     * @return 用户列表
     */
    List<User> findAll();

    /**
     * 启用用户
     *
     * @param id 用户ID
     * @return 更新后的用户
     */
    User enableUser(Long id);

    /**
     * 禁用用户
     *
     * @param id 用户ID
     * @return 更新后的用户
     */
    User disableUser(Long id);

    /**
     * 重置密码
     *
     * @param id       用户ID
     * @param password 新密码
     * @return 更新后的用户
     */
    User resetPassword(Long id, String password);

    /**
     * 修改密码
     *
     * @param id          用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return 是否成功
     */
    boolean changePassword(Long id, String oldPassword, String newPassword);

    /**
     * 更新用户角色
     *
     * @param id      用户ID
     * @param roleIds 角色ID列表
     */
    void updateUserRoles(Long id, List<Long> roleIds);

    /**
     * 获取用户角色ID列表
     *
     * @param id 用户ID
     * @return 角色ID列表
     */
    List<Long> getUserRoleIds(Long id);

    /**
     * 记录登录信息
     *
     * @param id     用户ID
     * @param ip     登录IP
     * @param time   登录时间
     */
    void recordLoginInfo(Long id, String ip, java.time.LocalDateTime time);
}
