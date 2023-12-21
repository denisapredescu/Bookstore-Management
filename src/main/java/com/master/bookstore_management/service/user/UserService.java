package com.master.bookstore_management.service.user;

import com.master.bookstore_management.dto.UserDetails;
import com.master.bookstore_management.dto.UserLogin;
import com.master.bookstore_management.dto.UserResponse;
import com.master.bookstore_management.model.User;

import java.util.List;

public interface UserService {
    User createUser(User user);

    UserResponse login(String email, String password);

    List<UserDetails> getUsers(String token);
}
