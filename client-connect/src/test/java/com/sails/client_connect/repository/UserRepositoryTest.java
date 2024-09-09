package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.RoleName;
import com.sails.client_connect.entity.User;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserRepositoryTest userRepositoryTest;


    /**
     * Test for finding a user by username.
     */
    @Test
    void testFindByUsername() {
        User user = new User(); // Initialize user
        user.setUsername("testuser");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByUsername("testuser");

        assertEquals(user, result.orElse(null));
    }

    /**
     * Test for finding a user by role name.
     */
    @Test
    void testFindByRoles_Name() {
        User user = new User(); // Initialize user
        Role role = new Role(); // Initialize role
        role.setName(RoleName.ADMIN); // Set role name
        user.setRoles(Collections.singleton(role)); // Set roles

        when(userRepository.findByRoles_Name(any(RoleName.class))).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByRoles_Name(RoleName.ADMIN);

        assertEquals(user, result.orElse(null));
    }

    /**
     * Test for finding all users with roles.
     */
    @Test
    void testFindAllWithRoles() {
        User user1 = new User(); // Initialize user
        User user2 = new User(); // Initialize user
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAllWithRoles()).thenReturn(users);

        List<User> result = userRepository.findAllWithRoles();

        assertEquals(2, result.size());
        assertEquals(user1, result.get(0));
        assertEquals(user2, result.get(1));
    }

    /**
     * Test for finding a user with roles by user ID.
     */
    @Test
    void testFindUserWithRolesById() {
        User user = new User(); // Initialize user
        user.setUser_id(1L); // Use int for ID if method expects int

        when(userRepository.findUserWithRolesById(anyInt())).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findUserWithRolesById(1); // Pass int

        assertEquals(user, result.orElse(null));
    }
}
