package com.sails.client_connect.exception;

public class PasswordExpiredException extends RuntimeException {
    public PasswordExpiredException(String message) {
        super(message);
    }
}
