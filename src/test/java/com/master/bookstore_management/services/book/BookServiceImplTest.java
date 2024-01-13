package com.master.bookstore_management.services.book;

import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.repositories.book.BookRepository;
import com.master.bookstore_management.services.author.AuthorService;
import com.master.bookstore_management.services.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static final String TOKEN_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y";
    private static final String TOKEN_CUSTOMER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VydXNlciIsInJvbGUiOiJVU0VSIn0.lkIf0276-9gS9nk9M_VEgoabl39m9qYIZuPa5zP4vpg";
    private static final String TOKEN_NOT_LOGGED_IN = "";
    private static final String TOKEN_INVALID = "invalid";
    private static final Integer BOOK_ID = 0;
    private static  final Book BOOK = new Book(
            BOOK_ID
    );

    @InjectMocks
    private BookServiceImpl bookServiceUnderTest;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private AuthorService authorService;

    @Test
    void addBook() {
    }

    @Test
    void addAuthorToBook() {
    }

    @Test
    void addCategoriesToBook() {
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }

    @Test
    void getBooks() {
    }

    @Test
    void getAvailableBooks() {
    }

    @Test
    void getBooksByAuthor() {
    }

    @Test
    void getBooksByCategory() {
    }

    @Test
    void getBookById() {
    }
}