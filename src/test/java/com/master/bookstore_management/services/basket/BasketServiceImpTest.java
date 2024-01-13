package com.master.bookstore_management.services.basket;

import com.master.bookstore_management.repositories.basket.BasketRepository;
import com.master.bookstore_management.services.bookbasket.BookBasketService;
import com.master.bookstore_management.services.user.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BasketServiceImpTest {
    @InjectMocks
    private BasketServiceImp basketServiceUnderTest;
    @Mock
    private BasketRepository basketRepository;
    @Mock
    private BookBasketService bookBasketService;
    @Mock
    private UserService userService;
    
    @Test
    void createBasket() {
    }

    @Test
    void sentOrder() {
    }

    @Test
    void getBasket() {
    }

    @Test
    void addBookToBasket() {
    }

    @Test
    void removeBookFromBasket() {
    }

    @Test
    void decrementBookFromBasket() {
    }
}