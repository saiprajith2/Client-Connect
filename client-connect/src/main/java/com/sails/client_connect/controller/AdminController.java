package com.sails.client_connect.controller;

import com.sails.client_connect.dto.*;
import com.sails.client_connect.service.CustomerService;
import com.sails.client_connect.service.RoleService;
import com.sails.client_connect.service.TaskService;
import com.sails.client_connect.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final CustomerService customerService;
    private final TaskService taskService;
    private final RoleService roleService;

    /**
     * @param userAuth Admin adds the user into system with dummy password
     * @return Response message User Created is sent
     */
    @PostMapping("/adduser")
    public ResponseEntity<String> addUser(@Valid @RequestBody UserAuth userAuth) {
        try {
            userService.saveUser(userAuth);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * @param roleDTO Admin can add roles in the system
     * @return New role is added in database
     */
    @PostMapping("/role/addRole")
    public ResponseEntity<RoleDTO> addRole(@Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO createdRole = roleService.createRole(roleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    /**
     * @param userId
     * @param roleUpdateDTOS Admin can update the role of a specific user
     * @return updated user details
     */

    @PutMapping("/roles/{userId}")
    public ResponseEntity<UserRoleUpdateDTO> updateUserRoles(@PathVariable int userId, @RequestBody Set<RoleUpdateDTO> roleUpdateDTOS) {
        UserRoleUpdateDTO updatedUser = roleService.updateUserRoles(userId, roleUpdateDTOS);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    /**
     * @param id Admin can delete a specific role from database
     * @return If role is not present in database
     * @throws RoleNotFoundException
     */
    @DeleteMapping("/role/delete/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) throws RoleNotFoundException {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * @param id Admin can fetch a specific role by its id
     * @return Role that is fetched
     * If role is not present in database
     * @throws RoleNotFoundException
     */
    @GetMapping("/role/{id}")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) throws RoleNotFoundException {
        RoleDTO roleDTO = roleService.getRoleById(id);
        return ResponseEntity.ok(roleDTO);
    }

    /**
     * Admin can fetch all the roles in database
     *
     * @return List of Roles
     */
    @GetMapping("/roles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    /**
     * Admin can view all the users from database
     *
     * @return List of users
     */
    @GetMapping("/users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * Admin can view all the customers from database
     *
     * @return List of customers
     */
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerUpdateDTO>> getAllCustomers() {
        List<CustomerUpdateDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    /**
     * Admin can view all the tasks assigned to users
     *
     * @return List of Tasks
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<TaskDTO>> getAllTasksToAdminView() {
        List<TaskDTO> tasks = taskService.getAllTasksToAdminView();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Admin can view details of customers fo financing
     *
     * @return List of customers
     */
    @GetMapping("/customers/names")
    public ResponseEntity<List<CustomersFinancingDTO>> getCustomersNames() {
        List<CustomersFinancingDTO> customers = customerService.getCustomersNames();
        return ResponseEntity.ok(customers);
    }
}
