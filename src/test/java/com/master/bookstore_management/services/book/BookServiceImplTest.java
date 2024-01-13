package com.master.bookstore_management.services.book;

import com.master.bookstore_management.exceptions.exceptions.*;
import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.repositories.book.BookRepository;
import com.master.bookstore_management.services.author.AuthorService;
import com.master.bookstore_management.services.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.concurrent.atomic.AtomicInteger;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    private static final String TOKEN_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y";
    private static final String TOKEN_CUSTOMER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VydXNlciIsInJvbGUiOiJVU0VSIn0.lkIf0276-9gS9nk9M_VEgoabl39m9qYIZuPa5zP4vpg";
    private static final String TOKEN_NOT_LOGGED_IN = "";
    private static final String TOKEN_INVALID = "invalid";
    private static final Integer BOOK_ID = 0;
    private static  final Book BOOK = new Book(
            BOOK_ID,
            "book",
            20,
            2000,
            1,
            "Series",
            false
    );
    private static final Author AUTHOR = new Author(
            0,
            "firstName",
            "lastName",
            "nationality"
    );
    private static final List<Category> CATEGORIES = List.of(
            new Category(),
            new Category()
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
        when(bookRepository.save(BOOK)).thenReturn(BOOK);

        var result = bookServiceUnderTest.addBook(TOKEN_ADMIN, BOOK);
        verify(bookRepository, times(1)).save(BOOK);

        assertEquals(BOOK, result);
    }

    @Test
    void addBook_DatabaseError() {
        when(bookRepository.save(BOOK)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.addBook(TOKEN_ADMIN, BOOK));
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void addBook_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> bookServiceUnderTest.addBook(TOKEN_CUSTOMER, BOOK));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addBook_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> bookServiceUnderTest.addBook(TOKEN_NOT_LOGGED_IN, BOOK));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addBook_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> bookServiceUnderTest.addBook(TOKEN_INVALID, BOOK));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addAuthorToBook() {
        BOOK.setIs_deleted(false);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(authorService.save(AUTHOR)).thenReturn(AUTHOR);

        BOOK.setAuthor(AUTHOR);

        when(bookRepository.save(BOOK)).thenReturn(BOOK);

        var result = bookServiceUnderTest.addAuthorToBook(TOKEN_ADMIN, BOOK_ID, AUTHOR);
        verify(bookRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(authorService, times(1)).save(AUTHOR);
        assertEquals(BOOK.getAuthor(), AUTHOR);
        verify(bookRepository, times(1)).save(BOOK);
        assertEquals(BOOK, result);
    }

    @Test
    void addAuthorToBook_NoSuchElementException() {
        when(bookRepository.findById(eq(BOOK_ID))).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> bookServiceUnderTest.addAuthorToBook(TOKEN_ADMIN, BOOK_ID, any()));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addAuthorToBook_DatabaseError_at_findById(){
        when(bookRepository.findById(eq(BOOK_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.addAuthorToBook(TOKEN_ADMIN, BOOK_ID, any()));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addAuthorToBook_DeletedBookException() {
        BOOK.setIs_deleted(true);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        assertThrows(DeletedBookException.class, () ->  bookServiceUnderTest.addAuthorToBook(TOKEN_ADMIN, BOOK_ID, any()));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addAuthorToBook_DatabaseError_at_save_author() {
        BOOK.setIs_deleted(false);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(authorService.save(AUTHOR)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.addAuthorToBook(TOKEN_ADMIN, BOOK_ID, AUTHOR));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(authorService, times(1)).save(AUTHOR);
        verify(bookRepository, never()).save(BOOK);
    }

    @Test
    void addAuthorToBook_DatabaseError_at_update_book() {
        BOOK.setIs_deleted(false);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(authorService.save(AUTHOR)).thenReturn(AUTHOR);

        BOOK.setAuthor(AUTHOR);

        when(bookRepository.save(BOOK)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.addAuthorToBook(TOKEN_ADMIN, BOOK_ID, AUTHOR));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(authorService, times(1)).save(AUTHOR);
        assertEquals(BOOK.getAuthor(), AUTHOR);
        verify(bookRepository, times(1)).save(BOOK);
    }

    @Test
    void addAuthorToBook_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> bookServiceUnderTest.addAuthorToBook(TOKEN_CUSTOMER, BOOK_ID, AUTHOR));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addAuthorToBook_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> bookServiceUnderTest.addAuthorToBook(TOKEN_NOT_LOGGED_IN, BOOK_ID, AUTHOR));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addAuthorToBook_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> bookServiceUnderTest.addAuthorToBook(TOKEN_INVALID, BOOK_ID, AUTHOR));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(authorService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addCategoriesToBook() {
        BOOK.setBookCategories(new ArrayList<>());
        BOOK.setIs_deleted(false);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        AtomicInteger categoryIdCounter = new AtomicInteger(1);
        when(categoryService.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(categoryIdCounter.getAndIncrement());
            return category;
        });

        when(bookRepository.save(BOOK)).thenReturn(BOOK);

        var result = bookServiceUnderTest.addCategoriesToBook(TOKEN_ADMIN, BOOK_ID, CATEGORIES);
        verify(bookRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(categoryService, times(CATEGORIES.size())).save(any(Category.class));
        assertEquals(new HashSet<>(BOOK.getBookCategories()), new HashSet<>(CATEGORIES));
        verify(bookRepository, times(1)).save(BOOK);
        assertEquals(BOOK, result);
        assertEquals(BOOK.getBookCategories().size(), CATEGORIES.size());
    }

    @Test
    void addCategoriesToBook_NoSuchElementException() {
        when(bookRepository.findById(eq(BOOK_ID))).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> bookServiceUnderTest.addCategoriesToBook(TOKEN_ADMIN, BOOK_ID, CATEGORIES));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(categoryService, never()).save(any(Category.class));
        verify(bookRepository, never()).save(BOOK);
    }

    @Test
    void addCategoriesToBook_DatabaseError_at_findById() {
        when(bookRepository.findById(eq(BOOK_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.addCategoriesToBook(TOKEN_ADMIN, BOOK_ID, CATEGORIES));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(categoryService, never()).save(any(Category.class));
        verify(bookRepository, never()).save(BOOK);
    }

    @Test
    void addCategoriesToBook_DeletedBookException() {
        BOOK.setIs_deleted(true);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        assertThrows(DeletedBookException.class, () ->  bookServiceUnderTest.addCategoriesToBook(TOKEN_ADMIN, BOOK_ID, CATEGORIES));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(categoryService, never()).save(any(Category.class));
        verify(bookRepository, never()).save(BOOK);
    }

    @Test
    void addCategoriesToBook_DatabaseError_at_save_category() {
        BOOK.setBookCategories(new ArrayList<>());
        BOOK.setIs_deleted(false);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(categoryService.save(CATEGORIES.get(0))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.addCategoriesToBook(TOKEN_ADMIN, BOOK_ID, CATEGORIES));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(categoryService, times(1)).save(any(Category.class));
        verify(bookRepository, never()).save(BOOK);
    }

    @Test
    void addCategoriesToBook_DatabaseError_at_update_book() {
        BOOK.setBookCategories(new ArrayList<>());
        BOOK.setIs_deleted(false);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        AtomicInteger categoryIdCounter = new AtomicInteger(1);
        when(categoryService.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(categoryIdCounter.getAndIncrement());
            return category;
        });

        when(bookRepository.save(BOOK)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.addCategoriesToBook(TOKEN_ADMIN, BOOK_ID, CATEGORIES));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK.getIs_deleted(), false);
        verify(categoryService, times(CATEGORIES.size())).save(any(Category.class));
        assertEquals(BOOK.getBookCategories(), CATEGORIES);
        verify(bookRepository, times(1)).save(BOOK);
    }

    @Test
    void addCategoriesToBook_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> bookServiceUnderTest.addCategoriesToBook(TOKEN_CUSTOMER, BOOK_ID, CATEGORIES));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(categoryService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addCategoriesToBook_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> bookServiceUnderTest.addCategoriesToBook(TOKEN_NOT_LOGGED_IN, BOOK_ID, CATEGORIES));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(categoryService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void addCategoriesToBook_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> bookServiceUnderTest.addCategoriesToBook(TOKEN_INVALID, BOOK_ID, CATEGORIES));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(categoryService, never()).save(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook() {
        BOOK.setIs_deleted(false);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        Book update_data = new Book(
                BOOK_ID,
                "update book",
                30,
                2000,
                0,
                null,
                false
                );
        BOOK.setName(update_data.getName());
        BOOK.setPrice(update_data.getPrice());
        BOOK.setSeries_name(update_data.getSeries_name());
        BOOK.setVolume(update_data.getVolume());
        BOOK.setYear(update_data.getYear());

        when(bookRepository.save(BOOK)).thenReturn(BOOK);

        var result = bookServiceUnderTest.updateBook(TOKEN_ADMIN, update_data, eq(BOOK_ID));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository).save(BOOK);

        assertEquals(BOOK, result);
    }

    @Test
    void updateBook_NoSuchElementException() {
        when(bookRepository.findById(eq(BOOK_ID))).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> bookServiceUnderTest.updateBook(TOKEN_ADMIN, new Book(), eq(BOOK_ID)));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_DatabaseError_at_findById() {
        when(bookRepository.findById(eq(BOOK_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.updateBook(TOKEN_ADMIN, new Book(), eq(BOOK_ID)));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_DeletedBookException() {
        BOOK.setIs_deleted(true);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        assertThrows(DeletedBookException.class, () -> bookServiceUnderTest.updateBook(TOKEN_ADMIN, new Book(), eq(BOOK_ID)));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_DatabaseError_at_update_book() {
        BOOK.setIs_deleted(false);

        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));
        when(bookRepository.save(any())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.updateBook(TOKEN_ADMIN, new Book(), eq(BOOK_ID)));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, times(1)).save(any());
    }

    @Test
    void updateBook_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> bookServiceUnderTest.updateBook(TOKEN_CUSTOMER, new Book(), BOOK_ID));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> bookServiceUnderTest.updateBook(TOKEN_NOT_LOGGED_IN, new Book(), BOOK_ID));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBook_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> bookServiceUnderTest.updateBook(TOKEN_INVALID, new Book(), BOOK_ID));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }


    @Test
    void deleteBook() {
        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        BOOK.setIs_deleted(true);

        when(bookRepository.save(BOOK)).thenReturn(BOOK);

        bookServiceUnderTest.deleteBook(TOKEN_ADMIN, eq(BOOK_ID));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, times(1)).save(BOOK);
    }

    @Test
    void deleteBook_NoSuchElementException()  {
        when(bookRepository.findById(eq(BOOK_ID))).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> bookServiceUnderTest.deleteBook(TOKEN_ADMIN, eq(BOOK_ID)));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteBook_DatabaseError_at_findById()  {
        when(bookRepository.findById(eq(BOOK_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.deleteBook(TOKEN_ADMIN, eq(BOOK_ID)));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteBook_DatabaseError_at_update_book() {
        when(bookRepository.findById(eq(BOOK_ID))).thenReturn(Optional.of(BOOK));

        BOOK.setIs_deleted(true);

        when(bookRepository.save(BOOK)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.deleteBook(TOKEN_ADMIN, eq(BOOK_ID)));
        verify(bookRepository, times(1)).findById(BOOK_ID);
        verify(bookRepository, times(1)).save(BOOK);
    }

    @Test
    void deleteBook_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> bookServiceUnderTest.deleteBook(TOKEN_CUSTOMER, BOOK_ID));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteBook_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> bookServiceUnderTest.deleteBook(TOKEN_NOT_LOGGED_IN, BOOK_ID));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void deleteBook_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> bookServiceUnderTest.deleteBook(TOKEN_INVALID, BOOK_ID));
        verify(bookRepository, never()).findById(BOOK_ID);
        verify(bookRepository, never()).save(any());
    }

    @Test
    void getBooks() {
        List<Book> books = List.of(new Book(), new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        var result = bookServiceUnderTest.getBooks(TOKEN_ADMIN);
        assertEquals(books, result);
        assertEquals(books.size(), result.size());
    }

    @Test
    void getBooks_DatabaseError() {
        when(bookRepository.findAll()).thenThrow(DatabaseError.class);
        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.getBooks(TOKEN_ADMIN));
    }

    @Test
    void getBooks_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> bookServiceUnderTest.getBooks(TOKEN_CUSTOMER));
        verify(bookRepository, never()).findAll();
    }

    @Test
    void getBooks_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> bookServiceUnderTest.getBooks(TOKEN_NOT_LOGGED_IN));
        verify(bookRepository, never()).findAll();
    }

    @Test
    void getBooks_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> bookServiceUnderTest.getBooks(TOKEN_INVALID));
        verify(bookRepository, never()).findAll();
    }

    @Test
    void getAvailableBooks() {
        List<Book> books = List.of(new Book(), new Book(), new Book());
        when(bookRepository.getAvailableBooks()).thenReturn(books);

        var result = bookServiceUnderTest.getAvailableBooks();
        assertEquals(books, result);
        assertEquals(books.size(), result.size());
    }

    @Test
    void getAvailableBooks_DatabaseError() {
        when(bookRepository.getAvailableBooks()).thenThrow(DatabaseError.class);
        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.getAvailableBooks());
    }

    @Test
    void getBooksByAuthor() {
        BOOK.setAuthor(AUTHOR);
        List<Book> books = List.of(BOOK, BOOK);

        when(bookRepository.getBooksByAuthor(AUTHOR.getFirstName(), AUTHOR.getLastName())).thenReturn(books);

        var result = bookServiceUnderTest.getBooksByAuthor(AUTHOR.getFirstName(), AUTHOR.getLastName());
        verify(bookRepository, times(1)).getBooksByAuthor(AUTHOR.getFirstName(), AUTHOR.getLastName());
        assertEquals(books, result);
    }

    @Test
    void getBooksByAuthor_DatabaseError() {
        when(bookRepository.getBooksByAuthor(any(), any())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.getBooksByAuthor(any(), any()));
        verify(bookRepository, times(1)).getBooksByAuthor(any(), any());
    }

    @Test
    void getBooksByCategory() {
        List<Book> books = List.of(BOOK, BOOK);

        when(bookRepository.getBooksByCategory(any())).thenReturn(books);

        var result = bookServiceUnderTest.getBooksByCategory(any());
        verify(bookRepository, times(1)).getBooksByCategory(any());
        assertEquals(books, result);
    }

    @Test
    void getBooksByCategory_DatabaseError() {
        when(bookRepository.getBooksByCategory(any())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> bookServiceUnderTest.getBooksByCategory(any()));
        verify(bookRepository, times(1)).getBooksByCategory(any());
    }

    @Test
    void getBookById() {
        when(bookRepository.findById(BOOK_ID)).thenReturn(Optional.of(BOOK));

        var result = bookServiceUnderTest.getBookById(BOOK_ID);
        verify(bookRepository, times(1)).findById(BOOK_ID);
        assertEquals(BOOK, result);
    }

    @Test
    void getBookById_NoSuchElementException() {
        when(bookRepository.findById(BOOK_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () ->  bookServiceUnderTest.getBookById(BOOK_ID));
        verify(bookRepository, times(1)).findById(BOOK_ID);
    }

    @Test
    void getBookById_DatabaseError() {
        when(bookRepository.findById(BOOK_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () ->  bookServiceUnderTest.getBookById(BOOK_ID));
        verify(bookRepository, times(1)).findById(BOOK_ID);
    }
}