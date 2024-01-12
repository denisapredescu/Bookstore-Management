package com.master.bookstore_management.services.basket;

import com.master.bookstore_management.dtos.BasketDetails;
import com.master.bookstore_management.models.Basket;

public interface BasketService {
    Basket createBasket(String token, int userId);
    Basket sentOrder(String token, int userId);
    BasketDetails getBasket(String token, int userId);
    Basket addBookToBasket(String token, int bookId, int basketId);
    Basket removeBookFromBasket(String token, int bookId, int basketId);
    Basket decrementBookFromBasket(String token, int bookId, int basketId);
}
