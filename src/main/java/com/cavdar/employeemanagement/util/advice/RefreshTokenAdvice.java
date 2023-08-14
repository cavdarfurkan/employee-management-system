package com.cavdar.employeemanagement.util.advice;

import com.cavdar.employeemanagement.util.exception.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RefreshTokenAdvice {

    @ExceptionHandler(value = RefreshTokenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<?> adviceHandler(RefreshTokenException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
