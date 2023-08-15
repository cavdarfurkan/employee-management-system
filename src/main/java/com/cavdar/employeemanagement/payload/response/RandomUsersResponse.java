package com.cavdar.employeemanagement.payload.response;

import com.cavdar.employeemanagement.domain.model.User;

public class RandomUsersResponse {
    private String adminUsername, adminPassword;
    private String managerUsername, managerPassword;
    private String employeeUsername, employeePassword;

    public RandomUsersResponse(User admin, User manager, User employee) {
        this.adminUsername = admin.getUsername();
        this.adminPassword = admin.getPassword();
        this.managerUsername = manager.getUsername();
        this.managerPassword = manager.getPassword();
        this.employeeUsername = employee.getUsername();
        this.employeePassword = employee.getPassword();
    }

    public String getAdminUsername() {
        return adminUsername;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public void setManagerUsername(String managerUsername) {
        this.managerUsername = managerUsername;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public String getEmployeeUsername() {
        return employeeUsername;
    }

    public void setEmployeeUsername(String employeeUsername) {
        this.employeeUsername = employeeUsername;
    }

    public String getEmployeePassword() {
        return employeePassword;
    }

    public void setEmployeePassword(String employeePassword) {
        this.employeePassword = employeePassword;
    }
}
