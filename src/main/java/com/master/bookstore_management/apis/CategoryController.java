package com.master.bookstore_management.apis;

import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.services.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestHeader(name = "userToken") String token,
                                                @Valid @RequestBody Category newCategory){
        return ResponseEntity.ok(categoryService.addCategory(token, newCategory));
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable int id,
                                               @RequestHeader(name = "userToken") String token,
                                               @Valid @RequestBody Category updateCategory){
        return ResponseEntity.ok(categoryService.updateCategory(token, updateCategory, id));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id,
                                               @RequestHeader(name = "userToken") String token){
        categoryService.deleteCategory(token, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getCategories")
    public ResponseEntity<List<Category>> getCategories(){
        return ResponseEntity.ok(categoryService.getCategories());
    }
}
