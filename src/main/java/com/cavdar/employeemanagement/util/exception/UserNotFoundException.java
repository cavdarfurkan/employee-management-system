package com.cavdar.employeemanagement.util.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long id) {
        super("User ID: " + id + " does not exists.");
    }
}
