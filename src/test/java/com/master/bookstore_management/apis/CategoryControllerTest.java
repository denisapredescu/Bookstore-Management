package com.master.bookstore_management.apis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.services.category.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {
    private static final String TOKEN_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y";
    private static final Category INPUT_CATEGORY = new Category(
            0,
            "category"
    );
    private static final Category CATEGORY = new Category(
            INPUT_CATEGORY.getId(),
            INPUT_CATEGORY.getName()
    );
    private static final Integer CATEGORY_ID = 0;
    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Test
    void addCategory() throws Exception {
        when(categoryService.addCategory(TOKEN_ADMIN, INPUT_CATEGORY)).thenReturn(CATEGORY);

        String inputCategoryJson = mapper.writeValueAsString(INPUT_CATEGORY);
        String insertedCategoryJson = mapper.writeValueAsString(CATEGORY);

        mockMvc.perform(post("/category/add").header("userToken", TOKEN_ADMIN)
                        .content(inputCategoryJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(insertedCategoryJson));
    }

    @Test
    void updateCategory() throws Exception {
        when(categoryService.updateCategory(TOKEN_ADMIN, INPUT_CATEGORY, CATEGORY_ID)).thenReturn(CATEGORY);

        String inputCategoryJson = mapper.writeValueAsString(INPUT_CATEGORY);
        String updatedCategoryJson = mapper.writeValueAsString(CATEGORY);

        mockMvc.perform(patch("/category/update/{id}", CATEGORY_ID)
                        .header("userToken", TOKEN_ADMIN)
                        .content(inputCategoryJson)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(content().json(updatedCategoryJson));
    }

    @Test
    void deleteCategory() throws Exception {
        categoryService.deleteCategory(TOKEN_ADMIN, CATEGORY_ID);

        mockMvc.perform(delete("/category/delete/{id}", CATEGORY_ID)
                .header("userToken", TOKEN_ADMIN)
        ).andExpect(status().isNoContent());
    }

    @Test
    void getCategories() throws Exception {
        when(categoryService.getCategories()).thenReturn(List.of(CATEGORY));

        String categoriesJson = mapper.writeValueAsString(List.of(CATEGORY));

        mockMvc.perform(get("/category/getCategories")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(content().json(categoriesJson));
    }
}