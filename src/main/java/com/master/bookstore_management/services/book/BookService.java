package com.master.bookstore_management.services.book;

import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.Category;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Book addBook(String token, Book newBook);
    Book addAuthorToBook(String token, Integer bookId, Author author);
    Book addCategoriesToBook(String token, Integer bookId, List<Category> categories);
    Book updateBook(String token,  Book bookToUpdate, Integer id);
    void deleteBook(String token, Integer id);
    List<Book> getBooks(String token);
    List<Book> getAvailableBooks();
    List<Book> getBooksByAuthor(String firstName, String lastName);
    List<Book> getBooksByCategory(String category);
    Book getBookById(Integer bookId);
}
