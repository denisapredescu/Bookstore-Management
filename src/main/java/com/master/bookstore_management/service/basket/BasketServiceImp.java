package com.master.bookstore_management.service.basket;

import com.master.bookstore_management.model.Basket;
import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.model.BookBasket;
import com.master.bookstore_management.model.User;
import com.master.bookstore_management.repository.UserRepositoryJPA;
import com.master.bookstore_management.repository.basket.BasketRepositoryJPA;
import com.master.bookstore_management.repository.basket.BookBasketRepositoryJPA;
import com.master.bookstore_management.repository.book.BookRepositoryJPA;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findById(userId).orElseThrow();
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
        Basket basket = basketRepository.findByUserId(userId).orElseThrow();

        basket.setSent(true);
        return basketRepository.save(basket);
    }

    @Override
    public Basket getBasket(int userId) {
        return  basketRepository.findByUserId(userId).orElseThrow();
    }

    @Transactional
    @Override
    public Basket addBookToBasket(int bookId, int basketId) {
        Book book = bookRepository.findById(bookId).orElseThrow();
        Basket basket = basketRepository.findById(basketId).orElseThrow();
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
        Basket basket = basketRepository.findById(basketId).orElseThrow();

        BookBasket bookBasket = bookBasketRepository.findBookInBasket(bookId, basketId).orElseThrow();
        bookBasketRepository.delete(bookBasket);

        basket.setCost(basket.getCost() - bookBasket.getPrice() * bookBasket.getCopies());
        return basketRepository.save(basket);
    }
}
