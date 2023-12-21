package com.master.bookstore_management.service.category;

import com.master.bookstore_management.model.Category;

import java.util.List;

public interface CategoryService {
    Category addCategory(String token, Category newCategory);
    Category updateCategory(String token, Category updateCategory, int id);
    void deleteCategory(String token, int id);
    List<Category> getCategories();
}
