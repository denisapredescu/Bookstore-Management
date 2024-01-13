package com.master.bookstore_management.apis;

import com.master.bookstore_management.dtos.BasketDetails;
import com.master.bookstore_management.models.Basket;
import com.master.bookstore_management.services.basket.BasketService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/basket")
@Tag(name = "Basket API", description = "Endpoints for managing baskets")
public class BasketController {
    BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @Operation(summary = "Create a empty basket for a user", responses = {
            @ApiResponse(responseCode = "200", description = "Basket created successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/createBasket/{userId}")
    public ResponseEntity<Basket> createBasket(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. Anyone can create a basket, but the user should be logged in aka to have a valid token", required = true) String token,
            @PathVariable @Parameter(description = "User ID", required = true) int userId) {
        return ResponseEntity.ok(basketService.createBasket(token, userId));
    }

    @Operation(summary = "Send an order from the bookstore. All the books from basket will be ordered", responses = {
            @ApiResponse(responseCode = "200", description = "Order sent successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/sentOrder/{userId}")
    public ResponseEntity<Basket> sentOrder(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. Anyone can create an order, but the user should be logged in aka to have a valid token", required = true) String token,
            @PathVariable @Parameter(description = "User ID", required = true) int userId) {
        return ResponseEntity.ok(basketService.sentOrder(token, userId));
    }

    @Operation(summary = "Get basket details for a user. The user has to be logged in to access his basket", responses = {
            @ApiResponse(responseCode = "200", description = "Basket details retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getBasket/{userId}")
    public ResponseEntity<BasketDetails> getBasket(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. Anyone can get his current basket, but the user should be logged in aka to have a valid token", required = true) String token,
            @PathVariable @Parameter(description = "User ID", required = true) int userId) {
        return ResponseEntity.ok(basketService.getBasket(token, userId));
    }

    @Operation(summary = "Add a book to the basket or increase the number of that book in basket", responses = {
            @ApiResponse(responseCode = "200", description = "Book added to the basket successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/addBookToBasket/{bookId}/{basketId}")
    public  ResponseEntity<Basket> addBookToBasket(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. Anyone can add books to basket, but the user should be logged in aka to have a valid token", required = true) String token,
            @PathVariable @Parameter(description = "Book ID", required = true) int bookId,
            @PathVariable @Parameter(description = "Basket ID", required = true) int basketId) {

        return  ResponseEntity.ok(basketService.addBookToBasket(token, bookId, basketId));
    }

    @Operation(summary = "Remove a book from the basket. It removes all the copies of that book from basket", responses = {
            @ApiResponse(responseCode = "200", description = "Book removed from the basket successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/removeBookFromBasket/{bookId}/{basketId}")
    public  ResponseEntity<Basket> removeBookFromBasket(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. Anyone can remove books from basket, but the user should be logged in aka to have a valid token", required = true) String token,
            @PathVariable @Parameter(description = "Book ID", required = true) int bookId,
            @PathVariable @Parameter(description = "Basket ID", required = true) int basketId) {
        return  ResponseEntity.ok(basketService.removeBookFromBasket(token, bookId, basketId));
    }

    @Operation(summary = "Decrement a book from the basket. It removes one copy of that book from basket", responses = {
            @ApiResponse(responseCode = "200", description = "Book decremented from the basket successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/decrementBookFromBasket/{bookId}/{basketId}")
    public ResponseEntity<Basket> decrementBookFromBasket(
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication. Anyone can decrease the number of the same book from basket, but the user should be logged in aka to have a valid token", required = true) String token,
            @PathVariable @Parameter(description = "Book ID", required = true) int bookId,
            @PathVariable @Parameter(description = "Basket ID", required = true) int basketId) {
        return ResponseEntity.ok(basketService.decrementBookFromBasket(token, bookId, basketId));
    }
}
