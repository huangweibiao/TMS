package com.tms.service;

import com.tms.dto.UserDTO;
import com.tms.entity.Role;
import com.tms.entity.User;
import com.tms.entity.UserRole;
import com.tms.exception.BusinessException;
import com.tms.repository.RoleRepository;
import com.tms.repository.UserRepository;
import com.tms.repository.UserRoleRepository;
import com.tms.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserRoleRepository userRoleRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;
    private Role role;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("encoded_password");
        user.setRealName("管理员");
        user.setPhone("13800138000");
        user.setEmail("admin@tms.com");
        user.setStatus(1);

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("admin");
        userDTO.setPassword("123456");
        userDTO.setRealName("管理员");
        userDTO.setPhone("13800138000");
        userDTO.setEmail("admin@tms.com");
        userDTO.setRoleIds(Arrays.asList(1L));

        role = new Role();
        role.setId(1L);
        role.setRoleCode("ROLE_ADMIN");
        role.setRoleName("管理员");
    }

    @Test
    void testFindById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(user.getId(), result.get().getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void testFindByUsername_Success() {
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername("admin");

        assertTrue(result.isPresent());
        assertEquals(user.getUsername(), result.get().getUsername());
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        List<User> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("admin");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));

        User result = userService.createUser(userDTO);

        assertNotNull(result);
        assertEquals(user.getUsername(), result.getUsername());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUser_UsernameExists() {
        when(userRepository.existsByUsername("admin")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.createUser(userDTO);
        });

        assertEquals("用户名已存在", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    void testCreateUser_DefaultPassword() {
        userDTO.setPassword(null);
        
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(passwordEncoder.encode("admin")).thenReturn("encoded_password");
        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("admin");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userRepository.findById(1L)).thenReturn(Optional.of(savedUser));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        User result = userService.createUser(userDTO);

        assertNotNull(result);
        verify(passwordEncoder).encode("admin");
    }

    @Test
    void testUpdateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        User result = userService.updateUser(1L, userDTO);

        assertNotNull(result);
        verify(userRepository, atLeast(1)).findById(1L);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateUser(1L, userDTO);
        });

        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void testUpdateUser_UsernameExists() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setUsername("old_admin");

        userDTO.setUsername("new_admin");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByUsername("new_admin")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateUser(1L, userDTO);
        });

        assertEquals("用户名已存在", exception.getMessage());
    }

    @Test
    void testDeleteUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(userRoleRepository).deleteByUserId(1L);
        verify(userRepository).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.deleteUser(1L);
        });

        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void testEnableUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.enableUser(1L);

        assertNotNull(result);
        assertEquals(1, result.getStatus());
    }

    @Test
    void testDisableUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.disableUser(1L);

        assertNotNull(result);
        assertEquals(0, result.getStatus());
    }

    @Test
    void testResetPassword_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("new_password")).thenReturn("new_encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.resetPassword(1L, "new_password");

        assertNotNull(result);
        verify(passwordEncoder).encode("new_password");
    }

    @Test
    void testChangePassword_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old_password", "encoded_password")).thenReturn(true);
        when(passwordEncoder.encode("new_password")).thenReturn("new_encoded_password");

        boolean result = userService.changePassword(1L, "old_password", "new_password");

        assertTrue(result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testChangePassword_WrongOldPassword() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

        boolean result = userService.changePassword(1L, "wrong_password", "new_password");

        assertFalse(result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testUpdateUserRoles_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        userService.updateUserRoles(1L, Arrays.asList(1L));

        verify(userRoleRepository).deleteByUserId(1L);
        verify(userRoleRepository).save(any(UserRole.class));
    }

    @Test
    void testUpdateUserRoles_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateUserRoles(1L, Arrays.asList(1L));
        });

        assertEquals("用户不存在", exception.getMessage());
    }

    @Test
    void testUpdateUserRoles_RoleNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roleRepository.findById(1L)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            userService.updateUserRoles(1L, Arrays.asList(1L));
        });

        assertEquals("角色不存在: 1", exception.getMessage());
    }

    @Test
    void testGetUserRoleIds() {
        UserRole userRole = new UserRole();
        userRole.setUserId(1L);
        userRole.setRoleId(1L);

        when(userRoleRepository.findByUserId(1L)).thenReturn(Arrays.asList(userRole));

        List<Long> result = userService.getUserRoleIds(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0));
    }

    @Test
    void testRecordLoginInfo() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        LocalDateTime loginTime = LocalDateTime.now();
        userService.recordLoginInfo(1L, "127.0.0.1", loginTime);

        verify(userRepository).save(any(User.class));
    }
}
