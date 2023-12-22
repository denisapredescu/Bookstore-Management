package com.master.bookstore_management.repositories.category;

import com.master.bookstore_management.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepositoryJPA extends JpaRepository<Category, Integer> {
    @Query("SELECT c FROM Category c WHERE c.name = :name")
    Optional<Category> findByName(String name);
}
