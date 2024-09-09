package com.sails.client_connect.dto;


import com.sails.client_connect.entity.RoleName;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleUpdateDTO {
    @NotNull(message = "The role id is required")
    private Long role_id;

    @NotNull(message = "The role name is required")
    private RoleName name;
}
