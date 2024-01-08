package com.master.bookstore_management.repositories.book;

import com.master.bookstore_management.models.Book;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Primary
public interface BookRepository extends JpaRepository<Book, Integer> {
    @Query("SELECT b FROM Book b WHERE b.is_deleted = false")
    List<Book> getAvailableBooks();

    @Query("SELECT DISTINCT b FROM Book b JOIN b.bookCategories c WHERE c.name = :category AND b.is_deleted = false")
    List<Book> getBooksByCategory(String category);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.author a WHERE a.firstName = :firstName AND a.lastName = :lastName AND b.is_deleted = false")
    List<Book> getBooksByAuthor(String firstName, String lastName);
}
