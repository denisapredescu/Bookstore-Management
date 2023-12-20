package com.master.bookstore_management.repository.book;

import com.master.bookstore_management.model.Book;

import java.util.List;
import java.util.Optional;

public class BookRepositoryImpl implements BookRepository {
    @Override
    public List<Book> getAvailableBooks() {
        return null;
    }

    @Override
    public List<Book> findAll() {
        return null;
    }

    @Override
    public List<Book> getBooksByAuthor(String firstName, String lastName) {
        return null;
    }

    @Override
    public List<Book> getBooksByCategory(String category) {
        return null;
    }

    @Override
    public Book save(Book newBook) {
        return null;
    }

    @Override
    public Optional<Book> findById(Integer id) {
        return Optional.empty();
    }
}
