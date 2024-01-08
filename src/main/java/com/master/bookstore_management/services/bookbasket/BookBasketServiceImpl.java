package com.master.bookstore_management.services.bookbasket;

import com.master.bookstore_management.models.Basket;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.BookBasket;
import com.master.bookstore_management.repositories.bookbasket.BookBasketRepository;
import com.master.bookstore_management.services.book.BookService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class BookBasketServiceImpl implements BookBasketService {
    private final BookBasketRepository bookBasketRepository;
    private final BookService bookService;

    public BookBasketServiceImpl(BookBasketRepository bookBasketRepository, BookService bookService) {
        this.bookBasketRepository = bookBasketRepository;
        this.bookService = bookService;
    }

    @Transactional
    @Override
    public Integer addBookToBasket(Integer bookId, Basket basket) {
        Book book = bookService.getBookById(bookId);

        BookBasket bookBasket = bookBasketRepository.findBookInBasket(book.getId(), basket.getId()).orElse(null);

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
            bookBasketRepository.save(bookBasket);
        }

        return bookBasket.getPrice();
    }

    @Transactional
    @Override
    public Integer removeBookToBasket(int bookId, int basketId) {
        BookBasket bookBasket = bookBasketRepository.findBookInBasket(bookId, basketId).orElseThrow(
                () -> new NoSuchElementException("The book is not in this basket"));

        bookBasketRepository.delete(bookBasket);

        return bookBasket.getPrice() * bookBasket.getCopies();
    }

    @Transactional
    @Override
    public Integer decrementBookFromBasket(int bookId, int basketId) {
        BookBasket bookBasket = bookBasketRepository.findBookInBasket(bookId, basketId).orElseThrow(
                () -> new NoSuchElementException("The book is not in this basket"));

        if (bookBasket.getCopies() > 1)
        {
            bookBasket.setCopies(bookBasket.getCopies() - 1);
            bookBasketRepository.save(bookBasket);
        } else {
            bookBasketRepository.delete(bookBasket);
        }

        return bookBasket.getPrice();
    }
}
