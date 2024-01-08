package com.master.bookstore_management.services.basket;

import com.master.bookstore_management.dtos.BasketDetails;
import com.master.bookstore_management.models.Basket;

public interface BasketService {
    Basket createBasket(int userId);
    Basket sentOrder(int userId);
    BasketDetails getBasket(int userId);
    Basket addBookToBasket(int bookId, int basketId);
    Basket removeBookFromBasket(int bookId, int basketId);
    Basket decrementBookFromBasket(int bookId, int basketId);
}
