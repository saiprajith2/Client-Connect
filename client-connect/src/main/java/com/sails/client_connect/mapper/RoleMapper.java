package com.sails.client_connect.mapper;


import com.sails.client_connect.dto.RoleDTO;
import com.sails.client_connect.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    Role toEntity(RoleDTO roleDTO);

    RoleDTO toDto(Role role);
}
 