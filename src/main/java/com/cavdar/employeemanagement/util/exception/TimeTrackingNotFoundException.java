package com.cavdar.employeemanagement.util.exception;

public class TimeTrackingNotFoundException extends RuntimeException {
    public TimeTrackingNotFoundException(String message) {
        super(message);
    }
    public TimeTrackingNotFoundException(Long id) {
        super("Time Tracking ID: " + id + " does not exists.");
    }
}
