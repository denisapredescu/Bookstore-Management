package com.master.bookstore_management.service.book;

import com.master.bookstore_management.model.Author;
import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.repository.book.BookRepository;
import com.master.bookstore_management.repository.book.BookRepositoryJPA;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> getBooks(String token) {
        verifyUser(token);
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getAvailableBooks() {
        return bookRepository.getAvailableBooks();
    }

    @Override
    public List<Book> getBooksByAuthor(String firstName, String lastName) {
        return bookRepository.getBooksByAuthor(firstName, lastName);
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        return bookRepository.getBooksByCategory(category);
    }

    @Override
    public Book addBook(String token, Book newBook) {
        verifyUser(token);
        return bookRepository.save(newBook);
    }

    @Override
    public Book updateBook(String token, Book bookToUpdate, Integer id) {
        verifyUser(token);
        Book book = bookRepository.findById(id).orElseThrow();
        book.setName(bookToUpdate.getName());
        book.setIs_deleted(bookToUpdate.getIs_deleted());
        book.setCopies(bookToUpdate.getCopies());
        book.setSeries_name(bookToUpdate.getSeries_name());
        book.setPrice(bookToUpdate.getPrice());
        book.setVolume(bookToUpdate.getVolume());
        book.setYear(bookToUpdate.getYear());

        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(String token, Integer id) {
        verifyUser(token);
        Book book = bookRepository.findById(id).orElseThrow();
        book.setIs_deleted(true);
        bookRepository.save(book);
    }

    private static void verifyUser(String token) {
        if (token == null || Strings.isBlank(token)){
            throw new RuntimeException("User not logged in");
        }
    }
}
