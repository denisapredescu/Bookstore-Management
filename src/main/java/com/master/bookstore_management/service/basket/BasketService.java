package com.master.bookstore_management.service.basket;

import com.master.bookstore_management.model.Basket;
import com.master.bookstore_management.model.BookBasket;
import org.springframework.web.bind.annotation.PathVariable;

public interface BasketService {
    Basket createBasket(int userId);
    Basket sentOrder(int userId);
    Basket getBasket(int userId);
    Basket addBookToBasket(int bookId, int basketId);
    Basket removeBookFromBasket(int bookId, int basketId);
}
