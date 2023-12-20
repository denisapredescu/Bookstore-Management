package com.master.bookstore_management.repository.book;

import com.master.bookstore_management.model.Author;
import com.master.bookstore_management.model.Book;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@Primary
public interface BookRepositoryJPA extends JpaRepository<Book, Integer>, BookRepository {
    @Override
    @Query("SELECT b FROM Book b WHERE b.is_deleted = false")
    List<Book> getAvailableBooks();

    @Override
    @Query("SELECT DISTINCT b FROM Book b JOIN b.bookCategories c WHERE c.name = :category AND b.is_deleted = false")
    List<Book> getBooksByCategory(String category);

    @Override
    @Query("SELECT DISTINCT b FROM Book b JOIN b.author a WHERE a.firstName = :firstName AND a.lastName = :lastName AND b.is_deleted = false")
    List<Book> getBooksByAuthor(String firstName, String lastName);
}
