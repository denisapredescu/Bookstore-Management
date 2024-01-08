package com.master.bookstore_management.repositories.bookbasket;

import com.master.bookstore_management.models.BookBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BookBasketRepository extends JpaRepository<BookBasket, Integer> {
    @Query("SELECT b FROM BookBasket b WHERE b.book.id = :bookId AND b.basket.id = :basketId")
    Optional<BookBasket> findBookInBasket(Integer bookId, Integer basketId);
}
