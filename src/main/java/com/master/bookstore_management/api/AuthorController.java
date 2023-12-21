package com.master.bookstore_management.api;

import com.master.bookstore_management.model.Author;
import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.service.author.AuthorService;
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

    @PostMapping("/add")
    public ResponseEntity<Author> addAuthor(@RequestHeader(name = "userToken") String token,
                                       @Valid @RequestBody Author newAuthor){
        System.out.println("sdasds");
        return ResponseEntity.ok(authorService.addAuthor(token, newAuthor));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Author> updateAuthor(@PathVariable int id,
                                           @RequestHeader(name = "userToken") String token,
                                           @Valid @RequestBody Author newAuthor){
        return ResponseEntity.ok(authorService.updateAuthor(token, newAuthor, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable int id,
                                           @RequestHeader(name = "userToken") String token){
        authorService.deleteAuthor(token, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllByAdmin")
    public ResponseEntity<List<Author>> getAuthors(){
        return ResponseEntity.ok(authorService.getAuthors());
    }
}
