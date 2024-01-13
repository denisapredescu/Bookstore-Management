package com.master.bookstore_management.dtos;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserResponse that = (UserResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, token);
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
