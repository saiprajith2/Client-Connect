package com.sails.client_connect.mapper;

import com.sails.client_connect.dto.RoleUpdateDTO;
import com.sails.client_connect.dto.UserDTO;
import com.sails.client_connect.dto.UserRoleUpdateDTO;
import com.sails.client_connect.entity.Role;
import com.sails.client_connect.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO toDto(User user);

    User toEntity(UserDTO userDto);

    @Mapping(source = "roles", target = "roleNames")
    UserRoleUpdateDTO toUpdateDto(User user);

    default Set<RoleUpdateDTO> mapRolesToRoleNames(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream()
                .map(this::mapRoleToRoleUpdateDto)
                .collect(Collectors.toSet());
    }

    default RoleUpdateDTO mapRoleToRoleUpdateDto(Role role) {
        if (role == null) {
            return null;
        }
        return new RoleUpdateDTO(role.getRole_id(), role.getName());
    }
}
