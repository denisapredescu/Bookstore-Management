package com.master.bookstore_management.apis;

import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.services.author.AuthorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "Add a new author. Just an user with an admin role can add authors", response = Author.class)
    @PostMapping("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<Author> addAuthor(@RequestHeader(name = "userToken") @ApiParam(value = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token,
                                            @Valid @RequestBody @ApiParam(value = "New author details", required = true) Author newAuthor){
        return ResponseEntity.ok(authorService.addAuthor(token, newAuthor));
    }

    @ApiOperation(value = "Update an existing author. Just an user with an admin role can update authors")
    @PatchMapping("/update/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<Author> updateAuthor(@PathVariable @ApiParam(value = "Author ID", required = true) int id,
                                           @RequestHeader(name = "userToken") @ApiParam(value = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token,
                                           @Valid @RequestBody @ApiParam(value = "Updated author details (firstname, lastname, nationality)", required = true) Author updateAuthor){
        return ResponseEntity.ok(authorService.updateAuthor(token, updateAuthor, id));
    }

    @ApiOperation(value = "Delete an author by ID. Just an user with an admin role can delete authors", response = Author.class)
    @DeleteMapping("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<Void> deleteAuthor(@PathVariable @ApiParam(value = "Author ID", required = true)  int id,
                                             @RequestHeader(name = "userToken") @ApiParam(value = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token){
        authorService.deleteAuthor(token, id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Get a list of all authors", response = List.class)
    @GetMapping("/getAuthors")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<Author>> getAuthors(){
        return ResponseEntity.ok(authorService.getAuthors());
    }

    @ApiOperation(value = "Get an author by first and last name", response = Author.class)
    @GetMapping("/getAuthor/{firstName}/{lastName}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<Author> getAuthor(@PathVariable @ApiParam(value = "Author's first name", required = true) String firstName,
                                            @PathVariable @ApiParam(value = "Author's last name", required = true) String lastName){
        return ResponseEntity.ok(authorService.getAuthor(firstName, lastName));
    }
}
