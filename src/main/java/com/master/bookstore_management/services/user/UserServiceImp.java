package com.master.bookstore_management.services.user;

import com.master.bookstore_management.dtos.UserDetails;
import com.master.bookstore_management.dtos.UserResponse;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.repositories.user.UserRepositoryJPA;
import com.master.bookstore_management.token.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

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
    public UserResponse login(String email, String password) {
        User user = userRepository.getUser(email, password).orElseThrow(
                () -> new NoSuchElementException("User not found"));

        return new UserResponse(
                user.getEmail(),
                JwtUtil.generateToken(user.getFirstName() + user.getLastName(), user.getRole())
        );
    }

    @Override
    public List<UserDetails> getUsers(String token) {
        JwtUtil.verifyAdmin(token);
        return userRepository.getUsers();
    }
}
