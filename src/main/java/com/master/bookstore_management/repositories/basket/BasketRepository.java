package com.master.bookstore_management.repositories.basket;

import com.master.bookstore_management.dtos.BookFromBasketDetails;
import com.master.bookstore_management.models.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Integer> {
    @Query("SELECT b FROM Basket b WHERE b.user.id = :userId AND b.sent = false")
    Optional<Basket> findByUserId(Integer userId);

    @Query("SELECT new com.master.bookstore_management.dtos.BookFromBasketDetails(bookBasket.book.name, bookBasket.price, bookBasket.copies) " +
            "FROM BookBasket bookBasket " +
            "WHERE bookBasket.basket.id = :basketId")
    List<BookFromBasketDetails> findBooksFromCurrentBasket(Integer basketId);
}
