package com.master.bookstore_management.apis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.services.book.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    // valid token for an admin user
    private static final String TOKEN_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y";
    private static final Integer BOOK_ID = 0;
    private static final Book INPUT_BOOK = new Book(
            BOOK_ID,
            "book",
            20,
            2000,
            1,
            "Series",
            false
    );
    private static final Book BOOK = new Book(
            INPUT_BOOK.getId(),
            INPUT_BOOK.getName(),
            INPUT_BOOK.getPrice(),
            INPUT_BOOK.getYear(),
            INPUT_BOOK.getVolume(),
            INPUT_BOOK.getSeries_name(),
            INPUT_BOOK.getIs_deleted()
    );
    private static final Author AUTHOR = new Author(
            null,
            "firstName",
            "lastName",
            "nationality"
    );

    private static final List<Category> CATEGORIES = List.of(
            new Category(
                    0, "category 1"
            ),
            new Category(
                    1, "category 2"
            )
    );

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void addBook() throws Exception {
        when(bookService.addBook(TOKEN_ADMIN, INPUT_BOOK)).thenReturn(BOOK);

        String inputBookJson = mapper.writeValueAsString(INPUT_BOOK);
        String insertedBookJson = mapper.writeValueAsString(BOOK);

        mockMvc.perform(post("/book/add")
                        .header("userToken", TOKEN_ADMIN)
                        .content(inputBookJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(insertedBookJson));
    }

    @Test
    void updateBook() throws Exception {
        when(bookService.updateBook(TOKEN_ADMIN, INPUT_BOOK, BOOK_ID)).thenReturn(BOOK);

        String inputBookJson = mapper.writeValueAsString(INPUT_BOOK);
        String updatedBookJson = mapper.writeValueAsString(BOOK);

        mockMvc.perform(patch("/book/update/{id}", BOOK_ID)
                        .header("userToken", TOKEN_ADMIN)
                        .content(inputBookJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(updatedBookJson));
    }

    @Test
    void addAuthorToBook() throws Exception {
        BOOK.setAuthor(AUTHOR);
        when(bookService.addAuthorToBook(TOKEN_ADMIN, BOOK_ID, AUTHOR)).thenReturn(BOOK);

        String inputAuthorJson = mapper.writeValueAsString(AUTHOR);
        String updatedBookJson = mapper.writeValueAsString(BOOK);

        mockMvc.perform(patch("/book/addAuthorToBook/{id}", BOOK_ID)
                        .header("userToken", TOKEN_ADMIN)
                        .content(inputAuthorJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(updatedBookJson));
    }

    @Test
    void addCategoriesToBook() throws Exception {
        BOOK.setBookCategories(CATEGORIES);
        when(bookService.addCategoriesToBook(TOKEN_ADMIN, BOOK_ID, CATEGORIES)).thenReturn(BOOK);

        String inputCategoriesJson = mapper.writeValueAsString(CATEGORIES);
        String updatedBookJson = mapper.writeValueAsString(BOOK);

        mockMvc.perform(patch("/book/addCategoriesToBook/{id}", BOOK_ID)
                        .header("userToken", TOKEN_ADMIN)
                        .content(inputCategoriesJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(updatedBookJson));
    }

    @Test
    void deleteBook() throws Exception {
        bookService.deleteBook(TOKEN_ADMIN, BOOK_ID);

        mockMvc.perform(patch("/book/delete/{id}", BOOK_ID)
                .header("userToken", TOKEN_ADMIN)
        ).andExpect(status().isNoContent());
    }

    @Test
    void getAllBooks() throws Exception {
        when(bookService.getBooks(TOKEN_ADMIN)).thenReturn(List.of(BOOK));

        String booksJson = mapper.writeValueAsString(List.of(BOOK));

        mockMvc.perform(get("/book/getAllByAdmin")
                        .header("userToken", TOKEN_ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content().json(booksJson));
    }

    @Test
    void getAvailableBooks() throws Exception {
        when(bookService.getAvailableBooks()).thenReturn(List.of(BOOK));

        String booksJson = mapper.writeValueAsString(List.of(BOOK));

        mockMvc.perform(get("/book/getAvailable")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content().json(booksJson));
    }

    @Test
    void getBooksByAuthor() throws Exception {
        String firstName = AUTHOR.getFirstName();
        String lastName = AUTHOR.getLastName();

        BOOK.setAuthor(AUTHOR);
        when(bookService.getBooksByAuthor(firstName, lastName)).thenReturn(List.of(BOOK));

        String booksJson = mapper.writeValueAsString(List.of(BOOK));

        mockMvc.perform(get("/book/getBooksByAuthor/{firstname}/{lastName}", firstName, lastName)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content().json(booksJson));
    }

    @Test
    void getBooksByCategory() throws Exception {
        String category = CATEGORIES.get(0).getName();

        BOOK.setBookCategories(CATEGORIES);
        when(bookService.getBooksByCategory(category)).thenReturn(List.of(BOOK));

        String booksJson = mapper.writeValueAsString(List.of(BOOK));

        mockMvc.perform(get("/book/getBooksByCategory/{category}", category)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content().json(booksJson));
    }
}