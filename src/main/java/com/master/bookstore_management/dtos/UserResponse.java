package com.master.bookstore_management.dtos;

public class UserResponse {
    Integer id;
    String email;
    String token;

    public UserResponse(Integer id, String email, String token) {
        this.id = id;
        this.email = email;
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
