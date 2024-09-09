package com.sails.client_connect.controller;

import com.sails.client_connect.dto.*;
import com.sails.client_connect.service.CustomerService;
import com.sails.client_connect.service.RoleService;
import com.sails.client_connect.service.TaskService;
import com.sails.client_connect.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.management.relation.RoleNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the AdminController class.
 * Uses Mockito to mock dependencies and verify interactions.
 */
@SpringBootTest
class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private CustomerService customerService;

    @Mock
    private TaskService taskService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AdminController adminController;

    /**
     * Tests the addRole method of AdminController.
     */
    @Test
    void testaddRole() {
        RoleDTO roleDTO = new RoleDTO(); // Initialize with necessary fields
        when(roleService.createRole(roleDTO)).thenReturn(roleDTO);

        ResponseEntity<RoleDTO> response = adminController.addRole(roleDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(roleDTO, response.getBody());
        verify(roleService, times(1)).createRole(roleDTO);
    }

    /**
     * Tests the updateUserRoles method of AdminController.
     */
    @Test
    void updateUserRoles() {
        int userId = 1;
        Set<RoleUpdateDTO> roleUpdateDtos = Set.of(new RoleUpdateDTO()); // Initialize as needed
        UserRoleUpdateDTO updatedUser = new UserRoleUpdateDTO(); // Initialize as needed
        when(roleService.updateUserRoles(userId, roleUpdateDtos)).thenReturn(updatedUser);

        ResponseEntity<UserRoleUpdateDTO> response = adminController.updateUserRoles(userId, roleUpdateDtos);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedUser, response.getBody());
        verify(roleService, times(1)).updateUserRoles(userId, roleUpdateDtos);
    }

    /**
     * Tests the deleteRole method of AdminController.
     *
     * @throws RoleNotFoundException if the role is not found
     */
    @Test
    void deleteRoleReturnNoContent() throws RoleNotFoundException {
        Long roleId = 1L;
        doNothing().when(roleService).deleteRole(roleId);

        ResponseEntity<Void> response = adminController.deleteRole(roleId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(roleService, times(1)).deleteRole(roleId);
    }

    /**
     * Tests the getRoleById method of AdminController.
     *
     * @throws RoleNotFoundException if the role is not found
     */
    @Test
    void getRoleByIdReturnRole() throws RoleNotFoundException {
        Long roleId = 1L;
        RoleDTO roleDTO = new RoleDTO(); // Initialize with necessary fields
        when(roleService.getRoleById(roleId)).thenReturn(roleDTO);

        ResponseEntity<RoleDTO> response = adminController.getRoleById(roleId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roleDTO, response.getBody());
        verify(roleService, times(1)).getRoleById(roleId);
    }

    /**
     * Tests the getAllRoles method of AdminController.
     */
    @Test
    void getAllRolesReturnRoles() {
        List<RoleDTO> roles = Arrays.asList(new RoleDTO(), new RoleDTO()); // Initialize as needed
        when(roleService.getAllRoles()).thenReturn(roles);

        ResponseEntity<List<RoleDTO>> response = adminController.getAllRoles();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(roles, response.getBody());
        verify(roleService, times(1)).getAllRoles();
    }

    /**
     * Tests the getAllUsers method of AdminController.
     */
    @Test
    void getAllUsersReturnUsers() {
        List<UserDTO> users = Arrays.asList(new UserDTO(), new UserDTO()); // Initialize as needed
        when(userService.findAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = adminController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
        verify(userService, times(1)).findAllUsers();
    }

    /**
     * Tests the getAllCustomers method of AdminController.
     */
    @Test
    void getAllCustomersReturnCustomers() {
        List<CustomerUpdateDTO> customers = Arrays.asList(new CustomerUpdateDTO(), new CustomerUpdateDTO()); // Initialize as needed
        when(customerService.getAllCustomers()).thenReturn(customers);

        ResponseEntity<List<CustomerUpdateDTO>> response = adminController.getAllCustomers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customers, response.getBody());
        verify(customerService, times(1)).getAllCustomers();
    }

    /**
     * Tests the getAllTasksToAdminView method of AdminController.
     */
    @Test
    void getAllTasksToAdminViewReturnTasks() {
        List<TaskDTO> tasks = Arrays.asList(new TaskDTO(), new TaskDTO()); // Initialize as needed
        when(taskService.getAllTasksToAdminView()).thenReturn(tasks);

        ResponseEntity<List<TaskDTO>> response = adminController.getAllTasksToAdminView();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(tasks, response.getBody());
        verify(taskService, times(1)).getAllTasksToAdminView();
    }

    /**
     * Tests the getCustomersNames method of AdminController.
     */
    @Test
    void getCustomersNamesReturnCustomersNames() {
        List<CustomersFinancingDTO> customers = Arrays.asList(new CustomersFinancingDTO(), new CustomersFinancingDTO()); // Initialize as needed
        when(customerService.getCustomersNames()).thenReturn(customers);

        ResponseEntity<List<CustomersFinancingDTO>> response = adminController.getCustomersNames();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(customers, response.getBody());
        verify(customerService, times(1)).getCustomersNames();
    }
}
