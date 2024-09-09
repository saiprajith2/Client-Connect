package com.sails.client_connect.controller;

import com.sails.client_connect.config.CustomUserDetails;
import com.sails.client_connect.dto.UserDTO;
import com.sails.client_connect.service.JwtService;
import com.sails.client_connect.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private UserController userController;

    /**
     * To Test where all users are returned
     */
    @Test
    void getAllUsers() {
        // Arrange
        UserDTO userDTO = new UserDTO(1L, "testuser", "test@example.com", Set.of());
        when(userService.findAllUsers()).thenReturn(List.of(userDTO));

        // Act
        List<UserDTO> users = userController.getAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }

    /**
     * Test to check for update password
     */

    @Test
    void updatePassword() {
        // Arrange
        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);
        when(customUserDetails.getUsername()).thenReturn("testuser");

        // Act
        ResponseEntity<String> response = userController.updatePassword("testuser", "newpassword", customUserDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated successfully.", response.getBody());
        verify(userService, times(1)).updatePassword("testuser", "newpassword");
    }


}
