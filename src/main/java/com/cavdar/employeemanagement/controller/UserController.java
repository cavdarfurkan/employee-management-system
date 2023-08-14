package com.cavdar.employeemanagement.controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEES') or hasRole('MANAGER') or hasRole('ADMIN')")
    public void getUser(@PathVariable Long id) {

    }
}
