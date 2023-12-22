package com.master.bookstore_management.services.book;

import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.repositories.author.AuthorRepositoryJPA;
import com.master.bookstore_management.repositories.book.BookRepositoryJPA;
import com.master.bookstore_management.repositories.category.CategoryRepositoryJPA;
import com.master.bookstore_management.token.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepositoryJPA bookRepository;
    private final AuthorRepositoryJPA authorRepository;
    private final CategoryRepositoryJPA categoryRepository;

    public BookServiceImpl(BookRepositoryJPA bookRepository, AuthorRepositoryJPA authorRepository, CategoryRepositoryJPA categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    @Override
    public List<Book> getBooks(String token) {
        JwtUtil.verifyAdmin(token);
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

    @Transactional
    @Override
    public Book addBook(String token, Book newBook) {
        JwtUtil.verifyAdmin(token);
        Author author = null;
        List<Category> categories = new ArrayList<>();

        if (newBook.getAuthor() != null) {
            author = authorRepository.save(newBook.getAuthor());
        }

        if (newBook.getBookCategories() != null) {
            for (Category category:
                    newBook.getBookCategories()) {
                categories.add(categoryRepository.save(category));
            }
        }

        Book book = bookRepository.save(newBook);

        book.setAuthor(author);
        book.setBookCategories(categories);

        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book updateBook(String token, Book bookToUpdate, Integer id) {
        JwtUtil.verifyAdmin(token);
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Book with this id not found"));
        book.setName(bookToUpdate.getName());
        book.setIs_deleted(bookToUpdate.getIs_deleted());
        book.setSeries_name(bookToUpdate.getSeries_name());
        book.setPrice(bookToUpdate.getPrice());
        book.setVolume(bookToUpdate.getVolume());
        book.setYear(bookToUpdate.getYear());
        book.setAuthor(bookToUpdate.getAuthor());

        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public void deleteBook(String token, Integer id) {
        JwtUtil.verifyAdmin(token);
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Book with this id not found"));
        book.setIs_deleted(true);
        bookRepository.save(book);
    }


}
