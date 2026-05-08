package com.tms.init;

import com.tms.entity.*;
import com.tms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 数据初始化类
 * 系统启动时自动初始化基础数据
 *
 * @author TMS Team
 * @version 1.0.0
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PermissionRepository permissionRepository,
                           UserRoleRepository userRoleRepository,
                           RolePermissionRepository rolePermissionRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.userRoleRepository = userRoleRepository;
        this.rolePermissionRepository = rolePermissionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        // 初始化权限
        initPermissions();

        // 初始化角色
        initRoles();

        // 初始化管理员用户
        initAdminUser();

        // 初始化角色权限关联
        initRolePermissions();

        System.out.println("数据初始化完成");
    }

    /**
     * 初始化权限数据
     */
    private void initPermissions() {
        if (permissionRepository.count() > 0) {
            return;
        }

        List<Permission> permissions = Arrays.asList(
                // 系统管理
                createPermission("system:user:view", "用户查看", 3, null, "/system/user", 1),
                createPermission("system:user:create", "用户创建", 3, null, null, 2),
                createPermission("system:user:update", "用户修改", 3, null, null, 3),
                createPermission("system:user:delete", "用户删除", 3, null, null, 4),
                createPermission("system:role:view", "角色查看", 3, null, "/system/role", 5),
                createPermission("system:role:create", "角色创建", 3, null, null, 6),
                createPermission("system:role:update", "角色修改", 3, null, null, 7),
                createPermission("system:role:delete", "角色删除", 3, null, null, 8),

                // 基础数据
                createPermission("base:customer:view", "客户查看", 3, null, "/base/customer", 9),
                createPermission("base:customer:create", "客户创建", 3, null, null, 10),
                createPermission("base:customer:update", "客户修改", 3, null, null, 11),
                createPermission("base:customer:delete", "客户删除", 3, null, null, 12),
                createPermission("base:vehicle:view", "车辆查看", 3, null, "/base/vehicle", 13),
                createPermission("base:vehicle:create", "车辆创建", 3, null, null, 14),
                createPermission("base:vehicle:update", "车辆修改", 3, null, null, 15),
                createPermission("base:vehicle:delete", "车辆删除", 3, null, null, 16),
                createPermission("base:driver:view", "司机查看", 3, null, "/base/driver", 17),
                createPermission("base:driver:create", "司机创建", 3, null, null, 18),
                createPermission("base:driver:update", "司机修改", 3, null, null, 19),
                createPermission("base:driver:delete", "司机删除", 3, null, null, 20),

                // 订单管理
                createPermission("order:waybill:view", "运单查看", 3, null, "/order/waybill", 21),
                createPermission("order:waybill:create", "运单创建", 3, null, null, 22),
                createPermission("order:waybill:update", "运单修改", 3, null, null, 23),
                createPermission("order:waybill:delete", "运单删除", 3, null, null, 24),

                // 调度管理
                createPermission("dispatch:dispatch:view", "调度查看", 3, null, "/dispatch/dispatch", 25),
                createPermission("dispatch:dispatch:create", "调度创建", 3, null, null, 26),
                createPermission("dispatch:dispatch:update", "调度修改", 3, null, null, 27),

                // 在途监控
                createPermission("tracking:track:view", "轨迹查看", 3, null, "/tracking/track", 28),
                createPermission("tracking:event:view", "事件查看", 3, null, "/tracking/event", 29),
                createPermission("tracking:event:handle", "事件处理", 3, null, null, 30),

                // 财务结算
                createPermission("finance:cost:view", "费用查看", 3, null, "/finance/cost", 31),
                createPermission("finance:settlement:view", "结算查看", 3, null, "/finance/settlement", 32),
                createPermission("finance:settlement:create", "结算创建", 3, null, null, 33),
                createPermission("finance:settlement:confirm", "结算确认", 3, null, null, 34)
        );

        permissionRepository.saveAll(permissions);
        System.out.println("权限数据初始化完成");
    }

    /**
     * 创建权限对象
     */
    private Permission createPermission(String code, String name, Integer type, Long parentId, String path, Integer sortOrder) {
        Permission permission = new Permission();
        permission.setPermissionCode(code);
        permission.setPermissionName(name);
        permission.setPermissionType(type);
        permission.setParentId(parentId);
        permission.setPath(path);
        permission.setSortOrder(sortOrder);
        permission.setStatus(1);
        return permission;
    }

    /**
     * 初始化角色数据
     */
    private void initRoles() {
        if (roleRepository.count() > 0) {
            return;
        }

        List<Role> roles = Arrays.asList(
                createRole("ROLE_ADMIN", "系统管理员", "拥有所有权限"),
                createRole("ROLE_DISPATCHER", "调度员", "负责运单调度和派车"),
                createRole("ROLE_CUSTOMER_SERVICE", "客服", "负责运单创建和跟踪"),
                createRole("ROLE_FINANCE", "财务", "负责费用结算和对账"),
                createRole("ROLE_DRIVER", "司机", "可查看分配的运单")
        );

        roleRepository.saveAll(roles);
        System.out.println("角色数据初始化完成");
    }

    /**
     * 创建角色对象
     */
    private Role createRole(String code, String name, String description) {
        Role role = new Role();
        role.setRoleCode(code);
        role.setRoleName(name);
        role.setDescription(description);
        role.setStatus(1);
        return role;
    }

    /**
     * 初始化管理员用户
     */
    private void initAdminUser() {
        if (userRepository.count() > 0) {
            return;
        }

        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRealName("系统管理员");
        admin.setPhone("13800138000");
        admin.setEmail("admin@tms.com");
        admin.setStatus(1);

        User savedUser = userRepository.save(admin);

        // 关联管理员角色
        Role adminRole = roleRepository.findByRoleCode("ROLE_ADMIN").orElse(null);
        if (adminRole != null) {
            UserRole userRole = new UserRole();
            userRole.setUserId(savedUser.getId());
            userRole.setRoleId(adminRole.getId());
            userRoleRepository.save(userRole);
        }

        System.out.println("管理员用户初始化完成，用户名: admin，密码: admin123");
    }

    /**
     * 初始化角色权限关联
     */
    private void initRolePermissions() {
        if (rolePermissionRepository.count() > 0) {
            return;
        }

        Role adminRole = roleRepository.findByRoleCode("ROLE_ADMIN").orElse(null);
        if (adminRole == null) {
            return;
        }

        // 为管理员角色分配所有权限
        List<Permission> permissions = permissionRepository.findAll();
        for (Permission permission : permissions) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(adminRole.getId());
            rolePermission.setPermissionId(permission.getId());
            rolePermissionRepository.save(rolePermission);
        }

        System.out.println("角色权限关联初始化完成");
    }
}
