package com.master.bookstore_management.apis;

import com.master.bookstore_management.dtos.BasketDetails;
import com.master.bookstore_management.models.Basket;
import com.master.bookstore_management.services.basket.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
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
    public ResponseEntity<BasketDetails> getBasket(@PathVariable int userId){
        return ResponseEntity.ok(basketService.getBasket(userId));
    }

    @PostMapping("/addBookToBasket/{bookId}/{basketId}")
    public  ResponseEntity<Basket> addBookToBasket(@PathVariable int bookId,
                                                   @PathVariable int basketId) {
        return  ResponseEntity.ok(basketService.addBookToBasket(bookId, basketId));
    }

    @DeleteMapping("/removeBookFromBasket/{bookId}/{basketId}")
    public  ResponseEntity<Basket> removeBookFromBasket(@PathVariable int bookId, @PathVariable int basketId) {
        return  ResponseEntity.ok(basketService.removeBookFromBasket(bookId, basketId));
    }

    @PatchMapping("/decrementBookFromBasket/{bookId}/{basketId}")
    public ResponseEntity<Basket> decrementBookFromBasket(@PathVariable int bookId, @PathVariable int basketId) {
        return ResponseEntity.ok(basketService.decrementBookFromBasket(bookId, basketId));
    }
}
