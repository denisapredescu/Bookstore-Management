package com.master.bookstore_management.repositories.author;

import com.master.bookstore_management.models.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AuthorRepositoryJPA extends JpaRepository<Author, Integer> {

    @Query("SELECT a FROM Author a")
    List<Author> getAuthors();
}
