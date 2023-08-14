package com.cavdar.employeemanagement.util.exception;

public class TimeTrackingAlreadyRunningException extends RuntimeException {
    public TimeTrackingAlreadyRunningException(Long id) {
        super("TimeTracking ID: " + id + " already running. To start a new tracker, stop it first.");
    }
}
