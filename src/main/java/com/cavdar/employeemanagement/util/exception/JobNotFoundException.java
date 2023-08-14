package com.cavdar.employeemanagement.util.exception;

public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(Long id) {
        super("Job ID: " + id + " does not exists.");
    }
}
