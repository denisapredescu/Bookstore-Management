package com.master.bookstore_management.api;

import com.master.bookstore_management.dto.UserDetails;
import com.master.bookstore_management.dto.UserLogin;
import com.master.bookstore_management.dto.UserResponse;
import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.model.User;
import com.master.bookstore_management.service.user.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/user")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> createUser(@RequestBody User newUser){
        return ResponseEntity.ok(userService.createUser(newUser));
    }
    @GetMapping("/login/{email}")
    public ResponseEntity<UserResponse> login(@PathVariable String email,
                                              @RequestHeader(name = "password") String password){
//        UserLogin userToLogin = new UserLogin(email, password);
        System.out.println(email);
        return ResponseEntity.ok(userService.login(email, password));
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDetails>> getUsers(@RequestHeader(name = "userToken") String token){
        return ResponseEntity.ok(userService.getUsers(token));
    }
}
