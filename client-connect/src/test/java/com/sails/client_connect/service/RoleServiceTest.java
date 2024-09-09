package com.sails.client_connect.service;

import com.sails.client_connect.dto.RoleDTO;
import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.User;
import com.sails.client_connect.mapper.RoleMapper;
import com.sails.client_connect.mapper.UserMapper;
import com.sails.client_connect.repository.RoleRepository;
import com.sails.client_connect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.relation.RoleNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private RoleService roleService;

    //To Test CreateRole creates new role
    @Test
    void testCreateRole() {
        // Arrange
        //creating mock roleDTO Object
        RoleDTO roleDTO = new RoleDTO();
        Role role = new Role();
        //when toEntity method is called return role
        when(roleMapper.toEntity(roleDTO)).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(roleMapper.toDto(role)).thenReturn(roleDTO);

        // Act:Calling the service method to create a role
        RoleDTO result = roleService.createRole(roleDTO);

        // Assert
        assertNotNull(result);
        // Asserting that the result is not null, meaning the service method successfully returned a DTO.
        verify(roleRepository).save(role);
        verify(roleMapper).toDto(role);
    }

    //To Test the method DeleteRole deletes role by id
    @Test
    void testDeleteRole() throws RoleNotFoundException {
        // Arrange
        Long roleId = 1L;
        Role role = new Role();
        role.setUsers(Set.of(new User()));
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act
        roleService.deleteRole(roleId);

        // Assert
        verify(roleRepository).findById(roleId);
        verify(roleRepository).deleteById(roleId);
    }

    //To Test GetRoleById return the roleName by roleId
    @Test
    void testGetRoleById() throws RoleNotFoundException {
        // Arrange
        Long roleId = 1L;
        Role role = new Role();
        RoleDTO roleDTO = new RoleDTO();
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDTO);
        // Act
        RoleDTO result = roleService.getRoleById(roleId);
        // Assert
        assertNotNull(result);
        verify(roleRepository).findById(roleId);
        verify(roleMapper).toDto(role);
    }

    // To test the GetAllRoles method returns all the roles
    @Test
    void testGetAllRoles() {
        // Arrange
        Role role = new Role();
        RoleDTO roleDTO = new RoleDTO();
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(roleMapper.toDto(role)).thenReturn(roleDTO);
        // Act
        List<RoleDTO> result = roleService.getAllRoles();
        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        verify(roleRepository).findAll();
    }


}
