package com.cavdar.employeemanagement.payload.response;

import com.cavdar.employeemanagement.domain.model.User;

public class RandomUserResponse {
    private String username, password;

    public RandomUserResponse(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
