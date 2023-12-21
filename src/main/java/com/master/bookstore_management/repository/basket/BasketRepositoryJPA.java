package com.master.bookstore_management.repository.basket;

import com.master.bookstore_management.model.Basket;
import com.master.bookstore_management.model.BookBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BasketRepositoryJPA extends JpaRepository<Basket, Integer> {
    @Query("SELECT b FROM Basket b WHERE b.user.id = :userId AND b.sent = false")
    Optional<Basket> findByUserId(Integer userId);


}
