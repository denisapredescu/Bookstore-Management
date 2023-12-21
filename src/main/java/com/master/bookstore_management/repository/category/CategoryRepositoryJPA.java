package com.master.bookstore_management.repository.category;

import com.master.bookstore_management.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepositoryJPA extends JpaRepository<Category, Integer> {
}
