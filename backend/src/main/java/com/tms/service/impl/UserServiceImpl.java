package com.tms.service.impl;

import com.tms.dto.UserDTO;
import com.tms.entity.User;
import com.tms.entity.UserRole;
import com.tms.exception.BusinessException;
import com.tms.repository.RoleRepository;
import com.tms.repository.UserRepository;
import com.tms.repository.UserRoleRepository;
import com.tms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用户Service实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           UserRoleRepository userRoleRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    @CachePut(value = "user", key = "#result.id")
    public User createUser(UserDTO dto) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername());
        // 默认密码为用户名
        String password = StringUtils.hasText(dto.getPassword()) ? dto.getPassword() : dto.getUsername();
        user.setPassword(passwordEncoder.encode(password));
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setStatus(1); // 启用

        User savedUser = userRepository.save(user);

        // 保存用户角色关联
        if (dto.getRoleIds() != null && !dto.getRoleIds().isEmpty()) {
            updateUserRoles(savedUser.getId(), dto.getRoleIds());
        }

        return savedUser;
    }

    @Override
    @Transactional
    @CachePut(value = "user", key = "#id")
    public User updateUser(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        // 检查用户名是否被其他用户使用
        if (!user.getUsername().equals(dto.getUsername()) && userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessException("用户名已存在");
        }

        user.setUsername(dto.getUsername());
        user.setRealName(dto.getRealName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

        User savedUser = userRepository.save(user);

        // 更新用户角色关联
        if (dto.getRoleIds() != null) {
            updateUserRoles(savedUser.getId(), dto.getRoleIds());
        }

        return savedUser;
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#id")
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        // 删除用户角色关联
        userRoleRepository.deleteByUserId(id);

        // 删除用户
        userRepository.deleteById(id);
    }

    @Override
    @Cacheable(value = "user", key = "#id")
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Cacheable(value = "user", key = "#username")
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Page<User> findUsers(String username, Integer status, Pageable pageable) {
        Specification<User> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(username)) {
                predicates.add(cb.like(root.get("username"), "%" + username + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return userRepository.findAll(spec, pageable);
    }

    @Override
    @Cacheable(value = "user", key = "'all'")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    @CachePut(value = "user", key = "#id")
    public User enableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setStatus(1);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    @CachePut(value = "user", key = "#id")
    public User disableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setStatus(0);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    @CachePut(value = "user", key = "#id")
    public User resetPassword(Long id, String password) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public boolean changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            return false;
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return true;
    }

    @Override
    @Transactional
    public void updateUserRoles(Long id, List<Long> roleIds) {
        // 验证用户是否存在
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        // 验证角色是否存在
        for (Long roleId : roleIds) {
            roleRepository.findById(roleId)
                    .orElseThrow(() -> new BusinessException("角色不存在: " + roleId));
        }

        // 删除原有角色关联
        userRoleRepository.deleteByUserId(id);

        // 创建新的角色关联
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(id);
            userRole.setRoleId(roleId);
            userRoleRepository.save(userRole);
        }
    }

    @Override
    public List<Long> getUserRoleIds(Long id) {
        return userRoleRepository.findByUserId(id).stream()
                .map(UserRole::getRoleId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CachePut(value = "user", key = "#id")
    public void recordLoginInfo(Long id, String ip, LocalDateTime time) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("用户不存在"));

        user.setLastLoginTime(time);
        user.setLastLoginIp(ip);
        userRepository.save(user);
    }
}
