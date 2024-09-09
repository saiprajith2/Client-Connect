package com.sails.client_connect.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String message;
    private HttpStatus status;
    private T data;
    private LocalDateTime timestamp;

    public ApiResponse(String message, HttpStatus status, T data) {
        this.message = message;
        this.status = status;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

}

