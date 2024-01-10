package com.master.bookstore_management.services.book;

import com.master.bookstore_management.exceptions.exceptions.DeletedBookException;
import com.master.bookstore_management.models.Author;
import com.master.bookstore_management.models.Book;
import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.repositories.book.BookRepository;
import com.master.bookstore_management.services.author.AuthorService;
import com.master.bookstore_management.services.category.CategoryService;
import com.master.bookstore_management.token.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryService categoryService;
    private final AuthorService authorService;

    public BookServiceImpl(BookRepository bookRepository,
                           CategoryService categoryService,
                           AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.categoryService = categoryService;
        this.authorService = authorService;
    }

    @Transactional
    @Override
    public Book addBook(String token, Book newBook) {
        JwtUtil.verifyAdmin(token);
        Book book = bookRepository.save(newBook);
        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book addAuthorToBook(String token, Integer bookId, Author newAuthor) {
        JwtUtil.verifyAdmin(token);
        Book book = getBookById(bookId);

        if (book.getIs_deleted())
            throw new DeletedBookException("Cannot add author to a deleted book");

        Author author = authorService.save(newAuthor);
        book.setAuthor(author);

        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book addCategoriesToBook(String token, Integer bookId, List<Category> newCategories) {
        JwtUtil.verifyAdmin(token);
        Book book = getBookById(bookId);

        if (book.getIs_deleted())
            throw new DeletedBookException("Cannot add categories to a deleted book");

        Set<Category> categories = new HashSet<>(book.getBookCategories());
        for (Category category: newCategories) {
            Category addedCategory = categoryService.save(category);
            categories.add(addedCategory);
        }

        book.setBookCategories(new ArrayList<>(categories));

        return bookRepository.save(book);
    }

    @Transactional
    @Override
    public Book updateBook(String token, Book bookToUpdate, Integer id) {
        JwtUtil.verifyAdmin(token);
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Book with this id not found"));

        if (book.getIs_deleted())
            throw new DeletedBookException("Cannot update a deleted book");

        book.setName(bookToUpdate.getName());
        book.setIs_deleted(bookToUpdate.getIs_deleted());
        book.setSeries_name(bookToUpdate.getSeries_name());
        book.setPrice(bookToUpdate.getPrice());
        book.setVolume(bookToUpdate.getVolume());
        book.setYear(bookToUpdate.getYear());

        if (bookToUpdate.getAuthor() != null)
            addAuthorToBook(token, book.getId(), bookToUpdate.getAuthor());
        // TODO: update book category

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

    @Override
    public Book getBookById(Integer bookId) {
        return bookRepository.findById(bookId).orElseThrow(
                () -> new NoSuchElementException("Does not exist a book with this id")
        );
    }

}
