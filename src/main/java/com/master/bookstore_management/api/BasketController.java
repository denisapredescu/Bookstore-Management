package com.master.bookstore_management.api;

import com.master.bookstore_management.dto.UserDetails;
import com.master.bookstore_management.model.Basket;
import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.model.BookBasket;
import com.master.bookstore_management.service.basket.BasketService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("/basket")
public class BasketController {
    BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @PostMapping("/createBasket/{userId}")
    public ResponseEntity<Basket> createBasket(@PathVariable int userId){
        return ResponseEntity.ok(basketService.createBasket(userId));
    }

    @PatchMapping("/sentOrder/{userId}")
    public ResponseEntity<Basket> sentOrder(@PathVariable int userId){
        return ResponseEntity.ok(basketService.sentOrder(userId));
    }

    @GetMapping("/getBasket/{userId}")
    public ResponseEntity<Basket> getBasket(@PathVariable int userId){
        return ResponseEntity.ok(basketService.getBasket(userId));
    }

    @PostMapping("/addBookToBasket/{bookId}/{basketId}")
    public  ResponseEntity<Basket> addBookToBasket(@PathVariable int bookId, @PathVariable int basketId) {
        return  ResponseEntity.ok(basketService.addBookToBasket(bookId, basketId));
    }

    @PostMapping("/removeBookFromBasket/{bookId}/{basketId}")
    public  ResponseEntity<Basket> removeBookFromBasket(@PathVariable int bookId, @PathVariable int basketId) {
        return  ResponseEntity.ok(basketService.removeBookFromBasket(bookId, basketId));
    }
}
