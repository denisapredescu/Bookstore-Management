package com.master.bookstore_management.repository.category;

import com.master.bookstore_management.model.Book;
import com.master.bookstore_management.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepositoryJPA extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findByName(String name);
}
