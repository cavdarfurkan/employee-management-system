package com.cavdar.employeemanagement.payload.response;

import org.springframework.hateoas.EntityModel;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private EntityModel<UserDetails> user;

    public JwtResponse(String token, String refreshToken, EntityModel<UserDetails> user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EntityModel<UserDetails> getUser() {
        return user;
    }

    public void setUser(EntityModel<UserDetails> user) {
        this.user = user;
    }
}
