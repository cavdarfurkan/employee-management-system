package com.cavdar.employeemanagement.controller;

import com.cavdar.employeemanagement.domain.model.User;
import com.cavdar.employeemanagement.domain.repository.AuthorityRepository;
import com.cavdar.employeemanagement.domain.service.EmployeeService;
import com.cavdar.employeemanagement.domain.service.UserService;
import com.cavdar.employeemanagement.payload.response.RandomUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final EmployeeService employeeService;

    @Autowired
    public UserController(UserService userService, AuthorityRepository authorityRepository, EmployeeService employeeService) {
        this.userService = userService;
        this.authorityRepository = authorityRepository;
        this.employeeService = employeeService;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEES') or hasRole('MANAGER') or hasRole('ADMIN')")
    public void getUser(@PathVariable Long id) {

    }

/**
 * The getRandomUsers function retrieves random users with specific authorities and returns them as a
 * list of RandomUserResponse objects.
 * 
 * @return The method is returning a ResponseEntity object with a body containing a list of
 * RandomUserResponse objects.
 */
    @GetMapping("/random")
    public ResponseEntity<?> getRandomUsers() {
        User admin = this.userService.getRandomUserByAuthority(authorityRepository.findByAuthority("ROLE_ADMIN"));
        User randomManager = this.userService.getRandomUserByAuthority(authorityRepository.findByAuthority("ROLE_MANAGER"));
        User randomEmployee = this.userService.getRandomUserByAuthority(authorityRepository.findByAuthority("ROLE_EMPLOYEE"), List.of(2L));

        admin.setPassword("123123123");
        randomManager.setPassword(userService.getPlainPassword(employeeService.getEmployeeByUser(randomManager)));
        randomEmployee.setPassword(userService.getPlainPassword(employeeService.getEmployeeByUser(randomEmployee)));

        return ResponseEntity.ok().body(List.of(
                new RandomUserResponse(admin),
                new RandomUserResponse(randomManager),
                new RandomUserResponse(randomEmployee))
        );
    }
}
