package com.cavdar.employeemanagement.domain.service;

import com.cavdar.employeemanagement.domain.model.User;
import com.cavdar.employeemanagement.domain.repository.UserRepository;
import com.cavdar.employeemanagement.util.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateUserById(User updatedUser, Long id) {
        User user = this.getUserById(id);

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setAuthorities(updatedUser.getAuthorities());

        if (updatedUser.getPassword() == null || updatedUser.getPassword().isEmpty() || updatedUser.getPassword().isBlank()) {
            return this.saveUser(user);
        }

        user.setPassword(updatedUser.getPassword());

        return this.saveUserAndEncryptPassword(user);
    }

    public User getUserByUsername(String username) {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with " + username + " username not found"));
    }

    public User getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    public User saveUserAndEncryptPassword(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return this.userRepository.save(user);
    }

    public User saveUser(User user) {
        return this.userRepository.save(user);
    }

    public boolean isExistsByUsername(String username) {
        return this.userRepository.existsByUsername(username);
    }

    public boolean isExistsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }
}
