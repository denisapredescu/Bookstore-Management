package com.master.bookstore_management.service.book;

import com.master.bookstore_management.model.Author;
import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.model.Category;
import com.master.bookstore_management.repository.author.AuthorRepositoryJPA;
import com.master.bookstore_management.repository.book.BookRepositoryJPA;
import com.master.bookstore_management.repository.category.CategoryRepositoryJPA;
import com.master.bookstore_management.token.JwtUtil;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
        System.out.println(token);
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
        Book book = bookRepository.findById(id).orElseThrow();
        book.setName(bookToUpdate.getName());
        book.setIs_deleted(bookToUpdate.getIs_deleted());
        book.setCopies(bookToUpdate.getCopies());
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
        Book book = bookRepository.findById(id).orElseThrow();
        book.setIs_deleted(true);
        bookRepository.save(book);
    }


}
