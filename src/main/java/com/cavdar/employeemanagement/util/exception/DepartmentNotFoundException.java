package com.cavdar.employeemanagement.util.exception;

public class DepartmentNotFoundException extends RuntimeException {
    public DepartmentNotFoundException(Long id) {
        super("Department ID: " + id + " does not exists.");
    }
}
