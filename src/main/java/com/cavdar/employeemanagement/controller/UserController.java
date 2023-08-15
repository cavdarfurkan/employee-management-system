package com.cavdar.employeemanagement.controller;

import com.cavdar.employeemanagement.domain.model.User;
import com.cavdar.employeemanagement.domain.repository.AuthorityRepository;
import com.cavdar.employeemanagement.domain.service.UserService;
import com.cavdar.employeemanagement.payload.response.RandomUsersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final AuthorityRepository authorityRepository;

    @Autowired
    public UserController(UserService userService, AuthorityRepository authorityRepository) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEES') or hasRole('MANAGER') or hasRole('ADMIN')")
    public void getUser(@PathVariable Long id) {

    }

    @GetMapping("/random")
    public ResponseEntity<?> getRandomUsers() {
        User admin = this.userService.getRandomUserByAuthority(authorityRepository.findByAuthority("ROLE_ADMIN"));
        User manager = this.userService.getRandomUserByAuthority(authorityRepository.findByAuthority("ROLE_MANAGER"));
        User employee = this.userService.getRandomUserByAuthority(authorityRepository.findByAuthority("ROLE_EMPLOYEE"), List.of(2L));

        return ResponseEntity.ok().body(new RandomUsersResponse(admin, manager, employee));
    }
}
