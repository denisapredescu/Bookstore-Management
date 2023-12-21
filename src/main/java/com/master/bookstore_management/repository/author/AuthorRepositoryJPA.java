package com.master.bookstore_management.repository.author;

import com.master.bookstore_management.model.Author;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;


public interface AuthorRepositoryJPA extends JpaRepository<Author, Integer> {

    @Query("SELECT a FROM Author a")
    List<Author> getAuthors();
}
