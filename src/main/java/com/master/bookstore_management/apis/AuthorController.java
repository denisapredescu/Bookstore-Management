package com.master.bookstore_management.apis;

import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.services.author.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping("/author")
@Tag(name = "Author API", description = "Endpoints for managing authors")
public class AuthorController {
    AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Operation(summary = "Add a new author. Just an user with an admin role can add authors", responses = {
            @ApiResponse(responseCode = "200", description = "Author added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add")
    public ResponseEntity<Author> addAuthor(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New author details", required = true) Author newAuthor){
        return ResponseEntity.ok(authorService.addAuthor(token, newAuthor));
    }

    @Operation(summary = "Update an existing author. Just an user with an admin role can update authors", responses = {
            @ApiResponse(responseCode = "200", description = "Author updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<Author> updateAuthor(
            @PathVariable @Parameter(description = "Author ID", required = true) int id,
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated author details (firstname, lastname, nationality)", required = true) Author updateAuthor){
        return ResponseEntity.ok(authorService.updateAuthor(token, updateAuthor, id));
    }

    @Operation(summary = "Delete an author by ID. Just an user with an admin role can delete authors", responses = {
            @ApiResponse(responseCode = "204", description = "Author deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAuthor(
            @PathVariable @Parameter(description = "Author ID", required = true)  int id,
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token){
        authorService.deleteAuthor(token, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a list of all authors", responses = {
            @ApiResponse(responseCode = "200", description = "List of authors retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAuthors")
    public ResponseEntity<List<Author>> getAuthors(){
        return ResponseEntity.ok(authorService.getAuthors());
    }

    @Operation(summary = "Get an author by first and last name", responses = {
            @ApiResponse(responseCode = "200", description = "Author retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Author not found"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAuthor/{firstName}/{lastName}")
    public ResponseEntity<Author> getAuthor(
            @PathVariable @Parameter(description = "Author's first name", required = true) String firstName,
            @PathVariable @Parameter(description = "Author's last name", required = true) String lastName){
        return ResponseEntity.ok(authorService.getAuthor(firstName, lastName));
    }
}
