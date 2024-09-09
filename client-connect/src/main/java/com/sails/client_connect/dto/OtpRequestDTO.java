package com.sails.client_connect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OtpRequestDTO {

    @NotBlank(message = "User name is required")
    private String username;

    @NotBlank(message = "Otp is required")
    private String otp;
}
