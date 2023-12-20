package com.master.bookstore_management.repository.author;

import com.master.bookstore_management.model.Author;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@Primary
public interface AuthorRepositoryJPA extends CrudRepository<Author, Integer>, AuthorRepository {
//    @Override
//    @Query("SELECT a FROM Author a")
//    List<Author> getAuthors();
}
