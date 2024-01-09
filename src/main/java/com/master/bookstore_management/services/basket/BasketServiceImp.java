package com.master.bookstore_management.services.basket;

import com.master.bookstore_management.dtos.BasketDetails;
import com.master.bookstore_management.models.Basket;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.repositories.basket.BasketRepository;
import com.master.bookstore_management.services.book.BookService;
import com.master.bookstore_management.services.bookbasket.BookBasketService;
import com.master.bookstore_management.services.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BasketServiceImp implements BasketService {
    private final BasketRepository basketRepository;
    private final BookBasketService bookBasketService;
    private final UserService userService;

    public BasketServiceImp(BasketRepository basketRepository, BookBasketService bookBasketService, UserService userService) {
        this.basketRepository = basketRepository;
        this.bookBasketService = bookBasketService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Basket createBasket(int userId) {
        User user = userService.getUser(userId);

        Basket basket = basketRepository.findByUserId(userId).orElse(null);

        if (basket == null) {
            return basketRepository.save(new Basket(
                    0,
                    false,
                    0,
                    user
            ));
        }

        return basket;
    }

    @Transactional
    @Override
    public Basket sentOrder(int userId) {
        Basket basket = basketRepository.findByUserId(userId).orElseThrow(
                () -> new NoSuchElementException("User does not have a current basket"));

        if (basket.getCost() == 0)
            throw new NoSuchElementException("User does not have books in basket");

        basket.setSent(true);
        return basketRepository.save(basket);
    }

    @Override
    public BasketDetails getBasket(int userId) {
        Basket basket =  basketRepository.findByUserId(userId).orElse(null);

        if (basket == null)
            basket = createBasket(userId);

        return new BasketDetails(
                basket.getId(),
                basket.getSent().toString(),
                basket.getUser().getId(),
                basket.getUser().getEmail(),
                basket.getCost(),
                basketRepository.findBooksFromCurrentBasket(basket.getId())
        );
    }

    @Transactional
    @Override
    public Basket addBookToBasket(int bookId, int basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(
                () -> new NoSuchElementException("Does not exist a basket with this id")
        );
//        BookBasket bookBasket = bookBasketRepository.findBookInBasket(bookId, basketId).orElse(null);
//        BookBasket bookBasket = bookBasketService.findBookInBasket(bookId, basketId).orElse(null);
//
//        if(bookBasket == null) {
//            bookBasket = bookBasketService.save(new BookBasket(
//                    0,
//                    1,
//                    book.getPrice(),
//                    book,
//                    basket
//            ));
//        } else {
//            bookBasket.setCopies(bookBasket.getCopies() + 1);
//            bookBasketService.save(bookBasket);
//        }
        Integer bookPriceInBasket = bookBasketService.addBookToBasket(bookId, basket);

        basket.setCost(basket.getCost() + bookPriceInBasket);
        return basketRepository.save(basket);
    }

    @Transactional
    @Override
    public Basket removeBookFromBasket(int bookId, int basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(
                () -> new NoSuchElementException("Does not exist a basket with this id"));

//        BookBasket bookBasket = bookBasketRepository.findBookInBasket(bookId, basketId).orElseThrow(
//                () -> new NoSuchElementException("The book is not in this basket"));

//        BookBasket bookBasket = bookBasketService.findBookInBasket(bookId, basketId).orElseThrow(
//                () -> new NoSuchElementException("The book is not in this basket"));
//
////        bookBasketRepository.delete(bookBasket);
//        bookBasketService.delete(bookBasket);

        Integer bookPriceInBasket = bookBasketService.removeBookToBasket(bookId, basketId);

        basket.setCost(basket.getCost() - bookPriceInBasket);
        return basketRepository.save(basket);
    }

    @Transactional
    @Override
    public Basket decrementBookFromBasket(int bookId, int basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(
                () -> new NoSuchElementException("Does not exist a basket with this id"));

        Integer bookPriceInBasket = bookBasketService.decrementBookFromBasket(bookId, basketId);

        basket.setCost(basket.getCost() - bookPriceInBasket);
        return basketRepository.save(basket);
    }
}
