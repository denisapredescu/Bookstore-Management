package com.master.bookstore_management.repository.book;

import com.master.bookstore_management.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    List<Book> getAvailableBooks();

    List<Book> findAll();

    List<Book> getBooksByAuthor(String firstName, String lastName);

    List<Book> getBooksByCategory(String category);

    Book save(Book newBook);

    Optional<Book> findById(Integer id);
}
