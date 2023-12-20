package com.master.bookstore_management.api;

import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.service.book.BookService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/add")
    public ResponseEntity<Book> addBook(@RequestHeader(name = "userToken") String token,
                                        @Valid @RequestBody Book newBook){
        return ResponseEntity.ok(bookService.addBook(token, newBook));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable int id,
                                           @RequestHeader(name = "userToken") String token,
                                           @Valid @RequestBody Book newBook){
        return ResponseEntity.ok(bookService.updateBook(token, newBook, id));
    }

    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id,
                                           @RequestHeader(name = "userToken") String token){
        bookService.deleteBook(token, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllByAdmin")
    public ResponseEntity<List<Book>> getAllBooks(@RequestHeader(name = "userToken") String token){
        return ResponseEntity.ok(bookService.getBooks(token));
    }

    @GetMapping("/getAvailable")
    public ResponseEntity<List<Book>> getAvailableBooks(){
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @GetMapping("/getBooksByAuthor")
    public ResponseEntity<List<Book>> getBooksByAuthor(@PathVariable String firstname, @PathVariable String lastName){
        return ResponseEntity.ok(bookService.getBooksByAuthor(firstname, lastName));
    }
    @GetMapping("/getBooksByCategory")
    public ResponseEntity<List<Book>> getBooksByCategory(@PathVariable String categoryy){
        return ResponseEntity.ok(bookService.getBooksByCategory(categoryy));
    }

}
