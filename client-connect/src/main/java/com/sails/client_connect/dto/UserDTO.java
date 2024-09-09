package com.sails.client_connect.dto;


import com.sails.client_connect.entity.RoleName;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    @NotNull(message = "The user id is required")
    private Long user_id;

    @NotBlank(message = "The username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "The role names is required")
    private Set<RoleName> roleNames;


}
