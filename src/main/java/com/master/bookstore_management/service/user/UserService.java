package com.master.bookstore_management.service.user;

import com.master.bookstore_management.dto.UserLogin;
import com.master.bookstore_management.dto.UserResponse;
import com.master.bookstore_management.model.User;

public interface UserService {
    User createUser(User user);

    UserResponse login(UserLogin user);
}
