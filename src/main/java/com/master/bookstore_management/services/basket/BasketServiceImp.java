package com.master.bookstore_management.services.basket;

import com.master.bookstore_management.dtos.BasketDetails;
import com.master.bookstore_management.exceptions.exceptions.NoSuchElementInDatabaseException;
import com.master.bookstore_management.models.Basket;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.BookBasket;
import com.master.bookstore_management.models.User;
import com.master.bookstore_management.repositories.user.UserRepositoryJPA;
import com.master.bookstore_management.repositories.basket.BasketRepositoryJPA;
import com.master.bookstore_management.repositories.basket.BookBasketRepositoryJPA;
import com.master.bookstore_management.repositories.book.BookRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BasketServiceImp implements BasketService {
    private final BasketRepositoryJPA basketRepository;
    private final UserRepositoryJPA userRepository;
    private final BookBasketRepositoryJPA bookBasketRepository;
    private final BookRepositoryJPA bookRepository;

    public BasketServiceImp(BasketRepositoryJPA basketRepository, UserRepositoryJPA userRepository, BookBasketRepositoryJPA bookBasketRepository, BookRepositoryJPA bookRepository) {
        this.basketRepository = basketRepository;
        this.userRepository = userRepository;
        this.bookBasketRepository = bookBasketRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    @Override
    public Basket createBasket(int userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NoSuchElementException("User not found in database")
        );
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
                basket.getSent().toString(),
                basket.getUser().getEmail(),
                basket.getCost(),
                basketRepository.findBooksFromCurrentBasket(basket.getId())
        );
    }

    @Transactional
    @Override
    public Basket addBookToBasket(int bookId, int basketId) {
        Book book = bookRepository.findById(bookId).orElseThrow(
                () -> new NoSuchElementException("Does not exist a book with this id")
        );
        Basket basket = basketRepository.findById(basketId).orElseThrow(
                () -> new NoSuchElementException("Does not exist a basket with this id")
        );
        BookBasket bookBasket = bookBasketRepository.findBookInBasket(bookId, basketId).orElse(null);

        if(bookBasket == null) {
            bookBasket = bookBasketRepository.save(new BookBasket(
                    0,
                    1,
                    book.getPrice(),
                    book,
                    basket
            ));
        } else {
            bookBasket.setCopies(bookBasket.getCopies() + 1);
        }

        basket.setCost(basket.getCost() + book.getPrice());
        return basketRepository.save(basket);
    }

    @Override
    public Basket removeBookFromBasket(int bookId, int basketId) {
        Basket basket = basketRepository.findById(basketId).orElseThrow(
                () -> new NoSuchElementException("Does not exist a basket with this id"));

        BookBasket bookBasket = bookBasketRepository.findBookInBasket(bookId, basketId).orElseThrow(
                () -> new NoSuchElementException("The book is not in this basket"));
        bookBasketRepository.delete(bookBasket);

        basket.setCost(basket.getCost() - bookBasket.getPrice() * bookBasket.getCopies());
        return basketRepository.save(basket);
    }
}
