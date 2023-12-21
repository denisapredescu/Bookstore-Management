package com.master.bookstore_management.service.user;

import com.master.bookstore_management.dto.UserLogin;
import com.master.bookstore_management.dto.UserResponse;
import com.master.bookstore_management.model.User;
import com.master.bookstore_management.repository.UserRepositoryJPA;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImp implements UserService {
    UserRepositoryJPA userRepository;

    public UserServiceImp(UserRepositoryJPA userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public UserResponse login(UserLogin user) {
        UserResponse userResponse = userRepository.getUser(user.getEmail(), user.getPassword()).orElseThrow();
        return userResponse;
    }
}
