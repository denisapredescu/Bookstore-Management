package com.master.bookstore_management.apis;

import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.services.category.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
@RequestMapping("/category")
@Tag(name = "Category API", description = "Endpoints for managing categories")
public class CategoryController {
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Add a new category. Just an user with an admin role can add authors", responses = {
            @ApiResponse(responseCode = "200", description = "Category added successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<Category> addCategory(
            @RequestHeader(name = "userToken")  @Parameter(description = "User token for authentication", required = true) String token,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "New category details", required = true) Category newCategory){
        return ResponseEntity.ok(categoryService.addCategory(token, newCategory));
    }

    @Operation(summary = "Update an existing category. Just an user with an admin role can modify categories", responses = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PatchMapping("/update/{id}")
    public ResponseEntity<Category> updateCategory(
            @PathVariable @Parameter(description = "Category ID", required = true) int id,
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication", required = true) String token,
            @Valid @RequestBody @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated category details", required = true) Category updateCategory){
        return ResponseEntity.ok(categoryService.updateCategory(token, updateCategory, id));
    }

    @Operation(summary = "Delete a category by ID. Just an user with an admin role can delete categories", responses = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @DeleteMapping("/delete/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public ResponseEntity<Void> deleteCategory(
            @PathVariable @Parameter(description = "Category ID", required = true) int id,
            @RequestHeader(name = "userToken") @Parameter(description = "User token for authentication", required = true) String token){
        categoryService.deleteCategory(token, id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get a list of all categories", responses = {
            @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/getCategories")
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseEntity<List<Category>> getCategories(){
        return ResponseEntity.ok(categoryService.getCategories());
    }
}
