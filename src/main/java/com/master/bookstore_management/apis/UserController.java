package com.master.bookstore_management.apis;

import com.master.bookstore_management.dtos.UpdateUser;
import com.master.bookstore_management.dtos.UserDetails;
import com.master.bookstore_management.dtos.UserResponse;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.services.user.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<User> create(@Valid @RequestBody User newUser){
        return ResponseEntity.ok(userService.create(newUser));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id,
                                       @Valid @RequestBody UpdateUser updateUser){
        return ResponseEntity.ok(userService.update(id, updateUser));
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/login/{email}")
    public ResponseEntity<UserResponse> login(@PathVariable String email,
                                              @RequestHeader(name = "password") String password){
        return ResponseEntity.ok(userService.login(email, password));
    }

    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable Integer id){
        return ResponseEntity.ok(userService.getUser(id));
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDetails>> getUsers(@RequestHeader(name = "userToken") String token){
        return ResponseEntity.ok(userService.getUsers(token));
    }
}
