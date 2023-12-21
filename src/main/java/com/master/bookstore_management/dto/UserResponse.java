package com.master.bookstore_management.dto;

public class UserResponse {

    String email;
    String role;

    public UserResponse(String email, String role) {
        this.email = email;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
