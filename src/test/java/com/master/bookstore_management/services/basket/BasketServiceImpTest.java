package com.master.bookstore_management.services.basket;

import com.master.bookstore_management.dtos.BasketDetails;
import com.master.bookstore_management.dtos.BookFromBasketDetails;
import com.master.bookstore_management.exceptions.exceptions.DatabaseError;
import com.master.bookstore_management.exceptions.exceptions.InvalidTokenException;
import com.master.bookstore_management.exceptions.exceptions.UserNotLoggedInException;
import com.master.bookstore_management.models.*;
import com.master.bookstore_management.repositories.basket.BasketRepository;
import com.master.bookstore_management.services.bookbasket.BookBasketService;
import com.master.bookstore_management.services.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceImpTest {
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
    private static final Integer USER_ID = 0;
    private static final User USER = new User(
            USER_ID,
            "firstName",
            "lastName",
            "08-02-2001",
            "denisa.predescu@gmail.com",
            "Denisa01!",
            "CUSTOMER"
    );
    private static final Integer BASKET_ID = 0;
    private static final Basket BASKET = new Basket(
            BASKET_ID,
            false,
            100,
            USER
    );
    private static final BookBasket BOOK_BASKET = new BookBasket(
            0,
            1,
            BOOK.getPrice(),
            BOOK,
            BASKET
    );

    @InjectMocks
    private BasketServiceImp basketServiceUnderTest;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private BookBasketService bookBasketService;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {

    }
    @Test
    void createBasket() {
        when(userService.getUser(USER_ID)).thenReturn(USER);
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(basketRepository.save(any(Basket.class))).thenReturn(BASKET);

        var result = basketServiceUnderTest.createBasket(TOKEN_CUSTOMER, USER_ID);
        verify(userService).getUser(USER_ID);
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository).save(any(Basket.class));
        assertEquals(BASKET, result);
    }

    @Test
    void createBasket_DatabaseError_at_getUser() {
        when(userService.getUser(USER_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.createBasket(TOKEN_CUSTOMER, USER_ID));
        verify(userService).getUser(USER_ID);
        verify(basketRepository, never()).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void createBasket_NoSuchElementException_at_getUser() {
        when(userService.getUser(USER_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> basketServiceUnderTest.createBasket(TOKEN_CUSTOMER, USER_ID));
        verify(userService).getUser(USER_ID);
        verify(basketRepository, never()).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void createBasket_already_created() {
        when(userService.getUser(USER_ID)).thenReturn(USER);
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.of(BASKET));

        var result = basketServiceUnderTest.createBasket(TOKEN_CUSTOMER, USER_ID);
        verify(userService).getUser(USER_ID);
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
        assertEquals(BASKET, result);
    }

    @Test
    void createBasket_DatabaseError_at_save() {
        when(userService.getUser(USER_ID)).thenReturn(USER);
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(basketRepository.save(any(Basket.class))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.createBasket(TOKEN_CUSTOMER, USER_ID));
        verify(userService).getUser(USER_ID);
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository).save(any(Basket.class));
    }


    @Test
    void createBasket_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> basketServiceUnderTest.createBasket(TOKEN_NOT_LOGGED_IN, USER_ID));
        verify(userService, never()).getUser(USER_ID);
        verify(basketRepository, never()).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any());
    }

    @Test
    void createBasket_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> basketServiceUnderTest.createBasket(TOKEN_INVALID, USER_ID));
        verify(userService, never()).getUser(USER_ID);
        verify(basketRepository, never()).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any());
    }

    @Test
    void sentOrder() {
        BASKET.setCost(10);

        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.of(BASKET));

        BASKET.setSent(true);

        when(basketRepository.save(any(Basket.class))).thenReturn(BASKET);

        var result = basketServiceUnderTest.sentOrder(TOKEN_CUSTOMER, USER_ID);
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository).save(any(Basket.class));
        assertEquals(BASKET, result);
    }

    @Test
    void sentOrder_DatabaseError_at_findByUserId()  {
        when(basketRepository.findByUserId(USER_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.sentOrder(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository, times(1)).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void sentOrder_NoSuchElementException_at_findByUserId()  {
        when(basketRepository.findByUserId(USER_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> basketServiceUnderTest.sentOrder(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository, times(1)).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void sentOrder_NoSuchElementException_cost_equals_zero() {
        BASKET.setCost(0);

        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.of(BASKET));

        assertThrows(NoSuchElementException.class, () -> basketServiceUnderTest.sentOrder(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository, times(1)).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void sentOrder_DatabaseError_at_save() {
        BASKET.setCost(10);

        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.of(BASKET));
        when(basketRepository.save(any(Basket.class))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.sentOrder(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository).save(any(Basket.class));
    }

    @Test
    void sentOrder_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> basketServiceUnderTest.sentOrder(TOKEN_NOT_LOGGED_IN, USER_ID));
        verify(basketRepository, never()).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any());
    }

    @Test
    void sentOrder_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> basketServiceUnderTest.sentOrder(TOKEN_INVALID, USER_ID));
        verify(basketRepository, never()).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any());
    }

    @Test
    void getBasket_createBasket() {
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(basketRepository.save(any(Basket.class))).thenReturn(BASKET);
        when(userService.getUser(USER_ID)).thenReturn(USER);
        when(basketRepository.findBooksFromCurrentBasket(BASKET_ID)).thenReturn(List.of());

        var result = basketServiceUnderTest.getBasket(TOKEN_CUSTOMER, USER_ID);
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository).save(any(Basket.class));
        verify(userService).getUser(USER_ID);
        verify(basketRepository).findBooksFromCurrentBasket(BASKET_ID);
        assertEquals(new BasketDetails(BASKET_ID,
                BASKET.getSent().toString(),
                BASKET.getUser().getId(),
                BASKET.getUser().getEmail(),
                BASKET.getCost(),
                List.of()
                ), result);
    }

    @Test
    void getBasket() {
        List<BookFromBasketDetails> books = List.of(
                new BookFromBasketDetails("book 1", 50, 1),
                new BookFromBasketDetails("book 2", 25, 2)
        );
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.of(BASKET));
        when(basketRepository.findBooksFromCurrentBasket(BASKET_ID)).thenReturn(books);

        var result = basketServiceUnderTest.getBasket(TOKEN_CUSTOMER, USER_ID);
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
        verify(userService, never()).getUser(USER_ID);
        verify(basketRepository).findBooksFromCurrentBasket(BASKET_ID);
        assertEquals(new BasketDetails(
                BASKET_ID,
                BASKET.getSent().toString(),
                USER_ID,
                USER.getEmail(),
                BASKET.getCost(),
                books
        ), result);
    }

    @Test
    void getBasket_DatabaseError_findByUserId() {
        when(basketRepository.findByUserId(USER_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.getBasket(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
        verify(userService, never()).getUser(USER_ID);
        verify(basketRepository, never()).findBooksFromCurrentBasket(BASKET_ID);
    }

    @Test
    void getBasket_DatabaseError_at_save() {
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(basketRepository.save(any(Basket.class))).thenThrow(DatabaseError.class);;

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.getBasket(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository).save(any(Basket.class));
        verify(userService).getUser(USER_ID);
        verify(basketRepository, never()).findBooksFromCurrentBasket(BASKET_ID);
    }

    @Test
    void getBasket_DatabaseError_at_getUser() {
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(userService.getUser(USER_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.getBasket(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
        verify(userService).getUser(USER_ID);
        verify(basketRepository, never()).findBooksFromCurrentBasket(BASKET_ID);
    }

    @Test
    void getBasket_NoSuchElementException_at_getUser() {
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(userService.getUser(USER_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> basketServiceUnderTest.getBasket(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
        verify(userService).getUser(USER_ID);
        verify(basketRepository, never()).findBooksFromCurrentBasket(BASKET_ID);
    }

    @Test
    void getBasket_DatabaseError_at_findBooksFromCurrentBasket() {
        when(basketRepository.findByUserId(USER_ID)).thenReturn(Optional.of(BASKET));
        when(basketRepository.findBooksFromCurrentBasket(BASKET_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.getBasket(TOKEN_CUSTOMER, USER_ID));
        verify(basketRepository).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
        verify(userService, never()).getUser(USER_ID);
        verify(basketRepository).findBooksFromCurrentBasket(BASKET_ID);
    }

    @Test
    void getBasket_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> basketServiceUnderTest.getBasket(TOKEN_NOT_LOGGED_IN, USER_ID));
        verify(basketRepository, never()).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
        verify(userService, never()).getUser(USER_ID);
        verify(basketRepository, never()).findBooksFromCurrentBasket(BASKET_ID);
    }

    @Test
    void getBasket_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> basketServiceUnderTest.getBasket(TOKEN_INVALID, USER_ID));
        verify(basketRepository, never()).findByUserId(USER_ID);
        verify(basketRepository, never()).save(any(Basket.class));
        verify(userService, never()).getUser(USER_ID);
        verify(basketRepository, never()).findBooksFromCurrentBasket(BASKET_ID);
    }

    @Test
    void addBookToBasket() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.addBookToBasket(BOOK_ID, BASKET)).thenReturn(BOOK.getPrice());

        BASKET.setCost(BASKET.getCost() + BOOK.getPrice());

        when(basketRepository.save(any(Basket.class))).thenReturn(BASKET);

        var result = basketServiceUnderTest.addBookToBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID);
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).addBookToBasket(BOOK_ID, BASKET);
        verify(basketRepository).save(any(Basket.class));
        assertEquals(BASKET, result);
    }

    @Test
    void addBookToBasket_DatabaseError_at_findById() {
        when(basketRepository.findById(BASKET_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.addBookToBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService, never()).addBookToBasket(BOOK_ID, BASKET);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void addBookToBasket_NoSuchElementException_at_findById() {
        when(basketRepository.findById(BASKET_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> basketServiceUnderTest.addBookToBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService, never()).addBookToBasket(BOOK_ID, BASKET);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void addBookToBasket_DatabaseError_at_addBook() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.addBookToBasket(BOOK_ID, BASKET)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () ->  basketServiceUnderTest.addBookToBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).addBookToBasket(BOOK_ID, BASKET);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void addBookToBasket_NoSuchElementException_at_addBook() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.addBookToBasket(BOOK_ID, BASKET)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () ->  basketServiceUnderTest.addBookToBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).addBookToBasket(BOOK_ID, BASKET);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void addBookToBasket_DatabaseError_at_save() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.addBookToBasket(BOOK_ID, BASKET)).thenReturn(BOOK.getPrice());
        when(basketRepository.save(any(Basket.class))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.addBookToBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).addBookToBasket(BOOK_ID, BASKET);
        verify(basketRepository).save(any(Basket.class));
    }

    @Test
    void addBookToBasket_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> basketServiceUnderTest.addBookToBasket(TOKEN_NOT_LOGGED_IN, BOOK_ID, BASKET_ID));
        verify(basketRepository, never()).findById(BASKET_ID);
        verify(bookBasketService, never()).addBookToBasket(BOOK_ID, new Basket());
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void addBookToBasket_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> basketServiceUnderTest.addBookToBasket(TOKEN_INVALID, BOOK_ID, BASKET_ID));
        verify(basketRepository, never()).findById(BASKET_ID);
        verify(bookBasketService, never()).addBookToBasket(BOOK_ID, new Basket());
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void removeBookFromBasket() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.removeBookToBasket(BOOK_ID, BASKET_ID)).thenReturn(BOOK.getPrice());

        BASKET.setCost(BASKET.getCost() - BOOK.getPrice());

        when(basketRepository.save(any(Basket.class))).thenReturn(BASKET);

        var result = basketServiceUnderTest.removeBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID);
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).removeBookToBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository).save(any(Basket.class));
        assertEquals(BASKET, result);
    }

    @Test
    void removeBookFromBasket_DatabaseError_at_findById() {
        when(basketRepository.findById(BASKET_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.addBookToBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService, never()).removeBookToBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void removeBookFromBasket_NoSuchElementException_at_findById() {
        when(basketRepository.findById(BASKET_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> basketServiceUnderTest.removeBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService, never()).removeBookToBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void removeBookFromBasket_DatabaseError_at_removeBook() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.removeBookToBasket(BOOK_ID, BASKET_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () ->  basketServiceUnderTest.removeBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).removeBookToBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void removeBookFromBasket_NoSuchElementException_at_removeBook() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.removeBookToBasket(BOOK_ID, BASKET_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () ->  basketServiceUnderTest.removeBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).removeBookToBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void removeBookFromBasket_DatabaseError_at_save() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.removeBookToBasket(BOOK_ID, BASKET_ID)).thenReturn(BOOK.getPrice());
        when(basketRepository.save(any(Basket.class))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.removeBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).removeBookToBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository).save(any(Basket.class));
    }

    @Test
    void removeBookFromBasket_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> basketServiceUnderTest.removeBookFromBasket(TOKEN_NOT_LOGGED_IN, BOOK_ID, BASKET_ID));
        verify(basketRepository, never()).findById(BASKET_ID);
        verify(bookBasketService, never()).removeBookToBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void removeBookFromBasket_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> basketServiceUnderTest.removeBookFromBasket(TOKEN_INVALID, BOOK_ID, BASKET_ID));
        verify(basketRepository, never()).findById(BASKET_ID);
        verify(bookBasketService, never()).removeBookToBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void decrementBookFromBasket() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.decrementBookFromBasket(BOOK_ID, BASKET_ID)).thenReturn(BOOK.getPrice());

        BASKET.setCost(BASKET.getCost() - BOOK.getPrice());

        when(basketRepository.save(any(Basket.class))).thenReturn(BASKET);

        var result = basketServiceUnderTest.decrementBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID);
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).decrementBookFromBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository).save(any(Basket.class));
        assertEquals(BASKET, result);
    }


    @Test
    void decrementBookFromBasket_DatabaseError_at_findById() {
        when(basketRepository.findById(BASKET_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.decrementBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService, never()).decrementBookFromBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void decrementBookFromBasket_NoSuchElementException_at_findById() {
        when(basketRepository.findById(BASKET_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> basketServiceUnderTest.decrementBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService, never()).decrementBookFromBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void decrementBookFromBasket_DatabaseError_at_decrementBook() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.decrementBookFromBasket(BOOK_ID, BASKET_ID)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () ->  basketServiceUnderTest.decrementBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).decrementBookFromBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void decrementBookFromBasket_NoSuchElementException_at_decrementBook() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.decrementBookFromBasket(BOOK_ID, BASKET_ID)).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> basketServiceUnderTest.decrementBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).decrementBookFromBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void decrementBookFromBasket_DatabaseError_at_save() {
        when(basketRepository.findById(BASKET_ID)).thenReturn(Optional.of(BASKET));
        when(bookBasketService.decrementBookFromBasket(BOOK_ID, BASKET_ID)).thenReturn(BOOK.getPrice());
        when(basketRepository.save(any(Basket.class))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> basketServiceUnderTest.decrementBookFromBasket(TOKEN_CUSTOMER, BOOK_ID, BASKET_ID));
        verify(basketRepository).findById(BASKET_ID);
        verify(bookBasketService).decrementBookFromBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository).save(any(Basket.class));
    }

    @Test
    void decrementBookFromBasket_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> basketServiceUnderTest.decrementBookFromBasket(TOKEN_NOT_LOGGED_IN, BOOK_ID, BASKET_ID));
        verify(basketRepository, never()).findById(BASKET_ID);
        verify(bookBasketService, never()).decrementBookFromBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }

    @Test
    void decrementBookFromBasket_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> basketServiceUnderTest.decrementBookFromBasket(TOKEN_INVALID, BOOK_ID, BASKET_ID));
        verify(basketRepository, never()).findById(BASKET_ID);
        verify(bookBasketService, never()).decrementBookFromBasket(BOOK_ID, BASKET_ID);
        verify(basketRepository, never()).save(any(Basket.class));
    }
}