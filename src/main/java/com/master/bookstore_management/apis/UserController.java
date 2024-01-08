package com.master.bookstore_management.apis;

import com.master.bookstore_management.dtos.UserDetails;
import com.master.bookstore_management.dtos.UserResponse;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.services.user.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<User> createUser(@Valid @RequestBody User newUser){
        return ResponseEntity.ok(userService.createUser(newUser));
    }

    @GetMapping("/login/{email}")
    public ResponseEntity<UserResponse> login(@PathVariable String email,
                                              @RequestHeader(name = "password") String password){
        return ResponseEntity.ok(userService.login(email, password));
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDetails>> getUsers(@RequestHeader(name = "userToken") String token){
        return ResponseEntity.ok(userService.getUsers(token));
    }

    @PatchMapping("/softDelete/{email}")
    public ResponseEntity<Void> delete(@PathVariable String email){
        userService.delete(email);
        return ResponseEntity.noContent().build();
    }
}
