package com.master.bookstore_management.service.category;

import com.master.bookstore_management.model.Category;
import com.master.bookstore_management.repository.category.CategoryRepositoryJPA;
import com.master.bookstore_management.token.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    CategoryRepositoryJPA categoryRepository;

    public CategoryServiceImpl(CategoryRepositoryJPA categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category addCategory(String token, Category newCategory) {
        JwtUtil.verifyAdmin(token);
        Category category = categoryRepository.findByName(newCategory.getName()).orElse(null);

        if(category == null) {
            return categoryRepository.save(newCategory);
        }
        return category;
    }

    @Override
    public Category updateCategory(String token, Category updateCategory, int id) {
        JwtUtil.verifyAdmin(token);
        Category category = categoryRepository.findById(id).orElseThrow();
        category.setName(updateCategory.getName());

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(String token, int id) {
        JwtUtil.verifyAdmin(token);
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }
}
