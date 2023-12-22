package com.master.bookstore_management.dtos;

public class UserLogin {
    String email;
    String password;

    public UserLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
