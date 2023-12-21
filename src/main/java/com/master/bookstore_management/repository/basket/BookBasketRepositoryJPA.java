package com.master.bookstore_management.repository.basket;

import com.master.bookstore_management.model.BookBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookBasketRepositoryJPA extends JpaRepository<BookBasket, Integer> {
    @Query("SELECT b FROM BookBasket b WHERE b.book.id = :bookId AND b.basket.id = :basketId")
    Optional<BookBasket> findBookInBasket(Integer bookId, Integer basketId);
}
