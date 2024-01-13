package com.master.bookstore_management.apis;

import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.services.book.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@Tag(name = "Book API", description = "Endpoints for managing books")
public class BookController {
    BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @Operation(summary = "Add a new book. Just an user with an admin role can add", responses = {
            @ApiResponse(responseCode = "200", description = "Book added successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add")
    public ResponseEntity<Book> addBook(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New book details", required = true) Book newBook){
        return ResponseEntity.ok(bookService.addBook(token, newBook));
    }

    @Operation(summary = "Update an existing book. Just an user with an admin role can update", responses = {
            @ApiResponse(responseCode = "200", description = "Book updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable @Parameter(description = "Book ID", required = true) int id,
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update book details. The field that will be updated are: name, price, year, volume, series", required = true) Book updateBook){
        return ResponseEntity.ok(bookService.updateBook(token, updateBook, id));
    }

    @Operation(summary = "Add an author to a book. Just an user with an admin role can add", responses = {
            @ApiResponse(responseCode = "200", description = "Author added to the book successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/addAuthorToBook/{bookId}")
    public ResponseEntity<Book> addAuthorToBook(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication.If is not contain an admin role, it will throw error", required = true) String token,
            @PathVariable @Parameter(description = "Book ID", required = true) int bookId,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New or existing author details. The ID can be not completed. The matching is made after firstname and lastname", required = true) Author newAuthor) {
        return ResponseEntity.ok(bookService.addAuthorToBook(token, bookId, newAuthor));
    }

    @Operation(summary = "Add categories to a book. Just an user with an admin role can add", responses = {
            @ApiResponse(responseCode = "200", description = "Categories added to the book successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Book.class))),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/addCategoriesToBook/{bookId}")
    public ResponseEntity<Book> addCategoriesToBook(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token,
            @PathVariable @Parameter(description = "Book ID", required = true) int bookId,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of new or existing categories. The IDs can be not completed.  The matching is made after category name", required = true) List<Category> newCategories) {
        return ResponseEntity.ok(bookService.addCategoriesToBook(token, bookId, newCategories));
    }

    @Operation(summary = "Delete a book by ID. Just an user with an admin role can delete", responses = {
            @ApiResponse(responseCode = "204", description = "Book added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBook(
            @PathVariable @Parameter(description = "Book ID", required = true) int id,
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token) {
        bookService.deleteBook(token, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get all books by admin. Just an user with an admin role can access the list", responses = {
            @ApiResponse(responseCode = "200", description = "List of books retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAllByAdmin")
    public ResponseEntity<List<Book>> getAllBooks(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. If is not contain an admin role, it will throw error", required = true) String token) {
        return ResponseEntity.ok(bookService.getBooks(token));
    }

    @Operation(summary = "Get available books", responses = {
            @ApiResponse(responseCode = "200", description = "List of available books retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getAvailable")
    public ResponseEntity<List<Book>> getAvailableBooks(){
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }

    @Operation(summary = "Get books by author's first and last name", responses = {
            @ApiResponse(responseCode = "200", description = "List of books by author retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getBooksByAuthor/{firstname}/{lastName}")
    public ResponseEntity<List<Book>> getBooksByAuthor(
            @PathVariable @Parameter(description = "Author's first name", required = true) String firstname,
            @PathVariable @Parameter(description = "Author's last name", required = true) String lastName) {
        return ResponseEntity.ok(bookService.getBooksByAuthor(firstname, lastName));
    }

    @Operation(summary = "Get books by category's name", responses = {
            @ApiResponse(responseCode = "200", description = "List of books by category retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getBooksByCategory/{category}")
    public ResponseEntity<List<Book>> getBooksByCategory(
            @PathVariable @Parameter(description = "Category's name", required = true) String category){
        return ResponseEntity.ok(bookService.getBooksByCategory(category));
    }
}
