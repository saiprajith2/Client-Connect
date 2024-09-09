package com.sails.client_connect.service;

import com.sails.client_connect.dto.UserAuth;
import com.sails.client_connect.dto.UserDTO;
import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.RoleName;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.repository.RoleRepository;
import com.sails.client_connect.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private UserService userService;


    /**
     * Tests that saveUser() correctly saves a user and sends an email.
     */
    @Test
    void saveUserSendEmail() throws MessagingException {
        // Arrange
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername("testuser");
        userAuth.setEmail("test@example.com");
        userAuth.setPassword("password");
        userAuth.setRoleNames(Set.of(RoleName.USER));

        Role role = new Role();
        role.setName(RoleName.USER);

        User user = new User();
        user.setUsername(userAuth.getUsername());
        user.setEmail(userAuth.getEmail());
        user.setPassword("encodedpassword");
        user.setRoles(Set.of(role));

        when(roleRepository.findByName(any(RoleName.class))).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        userService.saveUser(userAuth);

        // Assert
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendDynamicEmail(anyString(), anyString(), anyString(), anyString());
    }

    /**
     * Tests that saveUser() throws an IllegalStateException when an admin already exists.
     */
    @Test
    void saveUserThrowExceptionWhenAdminExists() {
        // Arrange
        UserAuth userAuth = new UserAuth();
        userAuth.setUsername("testadmin");
        userAuth.setEmail("admin@example.com");
        userAuth.setPassword("password");
        userAuth.setRoleNames(Set.of(RoleName.ADMIN));

        when(userRepository.findByRoles_Name(RoleName.ADMIN)).thenReturn(Optional.of(new User()));

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> userService.saveUser(userAuth));
    }

    /**
     * Tests that findAllUsers() returns a list of UserDTOs.
     */
    @Test
    void findAllUsersReturnListOfUserDTOs() {
        // Arrange
        User user = new User();
        user.setUser_id(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        Role role = new Role();
        role.setName(RoleName.USER);
        user.setRoles(Set.of(role));

        when(userRepository.findAllWithRoles()).thenReturn(List.of(user));

        // Act
        List<UserDTO> userDTOs = userService.findAllUsers();

        // Assert
        assertEquals(1, userDTOs.size());
        assertEquals("testuser", userDTOs.get(0).getUsername());
        assertEquals("test@example.com", userDTOs.get(0).getEmail());
        assertTrue(userDTOs.get(0).getRoleNames().contains(RoleName.USER));
    }

    /**
     * Tests that findByUsername() returns a User for a valid username.
     */
    @Test
    void findByUsername() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        User foundUser = userService.findByUsername("testuser");

        // Assert
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    /**
     * Tests that findByUsername() throws an exception when the user is not found.
     */
    @Test
    void findByUsernameUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.findByUsername("nonexistentuser"));
    }

    /**
     * Tests that updatePassword() updates the user's password.
     */
    @Test
    void updatePasswordSuccess() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newpassword")).thenReturn("encodednewpassword");

        // Act
        userService.updatePassword("testuser", "newpassword");

        // Assert
        verify(userRepository, times(1)).save(user);
        assertEquals("encodednewpassword", user.getPassword());
    }

    /**
     * Tests that updatePassword() throws an exception when the user is not found.
     */
    @Test
    void updatePassword() {
        // Arrange
        when(userRepository.findByUsername("nonexistentuser")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> userService.updatePassword("nonexistentuser", "newpassword"));
    }
}
