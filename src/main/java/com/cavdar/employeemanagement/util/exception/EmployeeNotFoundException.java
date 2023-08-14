package com.cavdar.employeemanagement.util.exception;

import com.cavdar.employeemanagement.domain.model.User;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(User user) {
        super("Employee with User ID: " + user.getId() + " does not exists.");
    }
    public EmployeeNotFoundException(Long id) {
        super("Employee ID: " + id + " does not exists.");
    }
}
