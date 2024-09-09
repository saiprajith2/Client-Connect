package com.sails.client_connect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthRequest {
    @NotBlank(message = "The username is required")
    private String username;

    @NotBlank(message = "The password is required")
    private String password;

}
