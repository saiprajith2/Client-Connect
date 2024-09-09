package com.sails.client_connect.repository;

import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.RoleName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RoleRepositoryTest {

    @MockBean
    private RoleRepository roleRepository;

    /**
     * to test the method find by name
     */
    @Test
    void testFindByName() {
        // Arrange
        RoleName roleName = RoleName.USER;
        Role role = new Role(1L, roleName, new HashSet<>());
        // Create a Role instance with an id of 1L, a role name of USER, and an empty set of authorities.

        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        // role created is returned when method findByName is called

        // Act
        Optional<Role> result = roleRepository.findByName(roleName);
        // findByName is called on the mocked roleRepository

        // Assert
        assertEquals(role, result.orElse(null));
        // check that the result returned from findByName matches the expected role object
    }

}
