package com.cavdar.employeemanagement.util.advice;

import com.cavdar.employeemanagement.util.exception.DepartmentNotFoundException;
import com.cavdar.employeemanagement.util.exception.JobNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DepartmentNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> adviceHandler(DepartmentNotFoundException e) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(e.getMessage());
    }
}
