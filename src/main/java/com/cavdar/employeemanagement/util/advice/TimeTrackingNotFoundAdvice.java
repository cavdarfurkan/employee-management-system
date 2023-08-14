package com.cavdar.employeemanagement.util.advice;

import com.cavdar.employeemanagement.util.exception.TimeTrackingNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TimeTrackingNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> adviceHandler(TimeTrackingNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
