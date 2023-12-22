package com.master.bookstore_management.apis;

import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.services.author.AuthorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/author")
public class AuthorController {
    AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/addAuthor")
    public ResponseEntity<Author> addAuthor(@RequestHeader(name = "userToken") String token,
                                       @Valid @RequestBody Author newAuthor){
        return ResponseEntity.ok(authorService.addAuthor(token, newAuthor));
    }

    @PatchMapping("/updateAuthor/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable int id,
                                           @RequestHeader(name = "userToken") String token,
                                           @Valid @RequestBody Author newAuthor){
        return ResponseEntity.ok(authorService.updateAuthor(token, newAuthor, id));
    }

    @DeleteMapping("/deleteAuthor/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable int id,
                                           @RequestHeader(name = "userToken") String token){
        authorService.deleteAuthor(token, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAuthors")
    public ResponseEntity<List<Author>> getAuthors(@RequestHeader(name = "userToken") String token){
        return ResponseEntity.ok(authorService.getAuthors(token));
    }
}
