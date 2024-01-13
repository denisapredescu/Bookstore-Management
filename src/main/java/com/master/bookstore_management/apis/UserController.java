package com.master.bookstore_management.apis;

import com.master.bookstore_management.dtos.UpdateUser;
import com.master.bookstore_management.dtos.UserDetails;
import com.master.bookstore_management.dtos.UserResponse;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.services.user.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/user")
@Tag(name = "User API", description = "Endpoints for managing users")
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", responses = {
            @ApiResponse(responseCode = "200", description = "User created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/signUp")
    public ResponseEntity<User> create(
            @Valid @RequestBody @Parameter(description = "User details", required = true) User newUser){
        return ResponseEntity.ok(userService.create(newUser));
    }

    @Operation(summary = "Update an existing user. Anyone can update his details, but the user should be logged in aka has a valid token", responses = {
            @ApiResponse(responseCode = "200", description = "User updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<User> update(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. Anyone can update his details, but the user should be logged in aka has a valid token", required = true) String token,
            @PathVariable @Parameter(description = "User ID", required = true) Integer id,
            @Valid @RequestBody @Parameter(description = "Updated user details (firstname, lastname, birthday)", required = true) UpdateUser updateUser){
        return ResponseEntity.ok(userService.update(token, id, updateUser));
    }

    @Operation(summary = "Delete a user by ID. Anyone can delete his account, but the user should be logged in aka has a valid token", responses = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> delete(
            @RequestHeader(name = "userToken")  @Parameter(description = "User token for authentication, Anyone can update his details, but the user should be logged in aka has a valid token", required = true) String token,
            @PathVariable @Parameter(description = "User ID", required = true) Integer id){
        userService.delete(token, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Login and retrieve user details (user ID, email, TOKEN)", responses = {
            @ApiResponse(responseCode = "200", description = "Login successful",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/login/{email}")
    public ResponseEntity<UserResponse> login(
            @PathVariable @Parameter(description = "User email", required = true) String email,
            @RequestHeader(name = "password") @Parameter(description = "User password", required = true) String password){
        return ResponseEntity.ok(userService.login(email, password));
    }

    @Operation(summary = "Get a user by ID", responses = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getUser/{id}")
    public ResponseEntity<User> getUser(@PathVariable @Parameter(description = "User ID", required = true) Integer id){
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(summary = "Get a list of all users. Just an user with an admin role can access the users", responses = {
            @ApiResponse(responseCode = "200", description = "List of users retrieved successfully",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getUsers")
    public ResponseEntity<List<UserDetails>> getUsers(
            @RequestHeader(name = "userToken")
            @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error",
                    required = true) String token){
        return ResponseEntity.ok(userService.getUsers(token));
    }
}
