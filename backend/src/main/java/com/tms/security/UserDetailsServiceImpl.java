package com.tms.security;

import com.tms.entity.Role;
import com.tms.entity.User;
import com.tms.entity.UserRole;
import com.tms.repository.PermissionRepository;
import com.tms.repository.RoleRepository;
import com.tms.repository.UserRepository;
import com.tms.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Spring Security用户详情服务实现类
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository,
                                   UserRoleRepository userRoleRepository,
                                   RoleRepository roleRepository,
                                   PermissionRepository permissionRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));

        // 获取用户角色
        List<Long> roleIds = userRoleRepository.findByUserId(user.getId())
                .stream()
                .map(UserRole::getRoleId)
                .toList();

        // 获取角色编码作为权限
        List<String> permissions = roleRepository.findAllById(roleIds)
                .stream()
                .map(Role::getRoleCode)
                .toList();

        return new UserDetailsImpl(user, permissions);
    }
}
