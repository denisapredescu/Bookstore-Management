package com.master.bookstore_management.services.user;

import com.master.bookstore_management.dtos.UserDetails;
import com.master.bookstore_management.dtos.UserResponse;
import com.master.bookstore_management.models.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    UserResponse login(String email, String password);

    List<UserDetails> getUsers(String token);
}
