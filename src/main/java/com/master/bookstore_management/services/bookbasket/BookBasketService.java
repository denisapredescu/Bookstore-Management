package com.master.bookstore_management.services.bookbasket;

import com.master.bookstore_management.models.Basket;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.BookBasket;

public interface BookBasketService {
    Integer addBookToBasket(Integer bookId, Basket basket);
    Integer removeBookToBasket(int bookId, int basketId);
    Integer decrementBookFromBasket(int bookId, int basketId);

}
