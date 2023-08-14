package com.cavdar.employeemanagement.util.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task ID: " + id + " does not exists.");
    }
}
