package com.cavdar.employeemanagement.util.advice;

import com.cavdar.employeemanagement.util.exception.TimeTrackingAlreadyRunningException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TimeTrackingAlreadyRunningAdvice {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.LOCKED)
    public ResponseEntity<?> adviceHandler(TimeTrackingAlreadyRunningException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
