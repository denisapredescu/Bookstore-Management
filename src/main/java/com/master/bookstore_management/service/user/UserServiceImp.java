package com.master.bookstore_management.service.user;

import com.master.bookstore_management.dto.UserDetails;
import com.master.bookstore_management.dto.UserResponse;
import com.master.bookstore_management.model.User;
import com.master.bookstore_management.repository.user.UserRepositoryJPA;
import com.master.bookstore_management.token.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

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
        User user = userRepository.getUser(email, password).orElseThrow();
        System.out.println(user.getEmail());
        UserResponse userResponse = new UserResponse(
                user.getEmail(),
                JwtUtil.generateToken(user.getFirstName() + user.getLastName(), user.getRole())
        );
        System.out.println(userResponse);
        return userResponse;
    }

    @Override
    public List<UserDetails> getUsers(String token) {
        JwtUtil.verifyAdmin(token);
        return userRepository.getUsers();
    }
}
