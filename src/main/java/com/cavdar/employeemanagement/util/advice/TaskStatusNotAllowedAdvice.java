package com.cavdar.employeemanagement.util.advice;

import com.cavdar.employeemanagement.util.exception.TaskStatusNotAllowedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class TaskStatusNotAllowedAdvice {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ResponseEntity<?> adviceHandler(TaskStatusNotAllowedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
