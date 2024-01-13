package com.master.bookstore_management.services.category;

import com.master.bookstore_management.exceptions.exceptions.DatabaseError;
import com.master.bookstore_management.exceptions.exceptions.InvalidTokenException;
import com.master.bookstore_management.exceptions.exceptions.UnauthorizedUserException;
import com.master.bookstore_management.exceptions.exceptions.UserNotLoggedInException;
import com.master.bookstore_management.models.Category;
import com.master.bookstore_management.repositories.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private static final String TOKEN_ADMIN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJEZW5pc2FQcmVkZXNjdSIsInJvbGUiOiJBRE1JTiJ9.ZA0vxSE7keltGZWcNYlRTor-TBOXOrUxFbCsUleok4Y";
    private static final String TOKEN_CUSTOMER = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VydXNlciIsInJvbGUiOiJVU0VSIn0.lkIf0276-9gS9nk9M_VEgoabl39m9qYIZuPa5zP4vpg";
    private static final String TOKEN_NOT_LOGGED_IN = "";
    private static final String TOKEN_INVALID = "invalid";
    private static final Integer CATEGORY_ID = 0;
    private static final Category CATEGORY = new Category(
            CATEGORY_ID,
            "category"
    );

    @InjectMocks
    private CategoryServiceImpl categoryServiceUnderTest;
    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void addCategory_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> categoryServiceUnderTest.addCategory(TOKEN_CUSTOMER, CATEGORY));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void addCategory_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> categoryServiceUnderTest.addCategory(TOKEN_NOT_LOGGED_IN, CATEGORY));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void addCategory_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> categoryServiceUnderTest.addCategory(TOKEN_INVALID, CATEGORY));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void save() {
        when(categoryRepository.findByName(CATEGORY.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(CATEGORY)).thenReturn(CATEGORY);

        var result = categoryServiceUnderTest.save(CATEGORY);
        verify(categoryRepository, times(1)).findByName(CATEGORY.getName());
        verify(categoryRepository, times(1)).save(CATEGORY);

        assertEquals(CATEGORY, result);
    }

    @Test
    void save_category_is_null() {
        var result = categoryServiceUnderTest.save(null);
        verify(categoryRepository, never()).findByName(any());
        verify(categoryRepository, never()).save(any());

        assertNull(result);
    }

    @Test
    void save_already_in() {
        when(categoryRepository.findByName(CATEGORY.getName())).thenReturn(Optional.of(CATEGORY));

        var result = categoryServiceUnderTest.save(CATEGORY);
        verify(categoryRepository, times(1)).findByName(CATEGORY.getName());
        verify(categoryRepository, never()).save(CATEGORY);

        assertEquals(CATEGORY, result);
    }

    @Test
    void save_DatabaseError_at_findByName() {
        when(categoryRepository.findByName(CATEGORY.getName())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> categoryServiceUnderTest.save(CATEGORY));
        verify(categoryRepository, times(1)).findByName(CATEGORY.getName());
        verify(categoryRepository, never()).save(CATEGORY);
    }

    @Test
    void save_DatabaseError_at_save() {
        when(categoryRepository.findByName(CATEGORY.getName())).thenReturn(Optional.empty());
        when(categoryRepository.save(CATEGORY)).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> categoryServiceUnderTest.save(CATEGORY));
        verify(categoryRepository, times(1)).findByName(CATEGORY.getName());
        verify(categoryRepository, times(1)).save(CATEGORY);
    }

    @Test
    void updateCategory() {
        when(categoryRepository.findById(eq(CATEGORY_ID))).thenReturn(Optional.of(CATEGORY));

        Category update_data = new Category(CATEGORY_ID, "update category");
        CATEGORY.setName(update_data.getName());

        when(categoryRepository.save(any())).thenReturn(CATEGORY);

        var result = categoryServiceUnderTest.updateCategory(TOKEN_ADMIN, update_data, CATEGORY_ID);
        verify(categoryRepository, times(1)).findById(CATEGORY_ID);
        verify(categoryRepository).save(CATEGORY);
        assertEquals(CATEGORY, result);
    }

    @Test
    void updateCategory_NoSuchElementException() {
        when(categoryRepository.findById(eq(CATEGORY_ID))).thenThrow(NoSuchElementException.class);

        assertThrows(NoSuchElementException.class, () -> categoryServiceUnderTest.updateCategory(TOKEN_ADMIN, any(), CATEGORY_ID));
        verify(categoryRepository, times(1)).findById(CATEGORY_ID);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> categoryServiceUnderTest.updateCategory(TOKEN_CUSTOMER, new Category(), CATEGORY_ID));
        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> categoryServiceUnderTest.updateCategory(TOKEN_NOT_LOGGED_IN, new Category(), CATEGORY_ID));
        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> categoryServiceUnderTest.updateCategory(TOKEN_INVALID, new Category(), CATEGORY_ID));
        verify(categoryRepository, never()).findById(any());
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_DatabaseError_at_findById() {
        when(categoryRepository.findById(eq(CATEGORY_ID))).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> categoryServiceUnderTest.updateCategory(TOKEN_ADMIN, new Category(), CATEGORY_ID));
        verify(categoryRepository, times(1)).findById(CATEGORY_ID);
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void updateCategory_DatabaseError_at_save() {
        when(categoryRepository.findById(eq(CATEGORY_ID))).thenReturn(Optional.of(CATEGORY));
        when(categoryRepository.save(any())).thenThrow(DatabaseError.class);

        assertThrows(DatabaseError.class, () -> categoryServiceUnderTest.updateCategory(TOKEN_ADMIN, new Category(), CATEGORY_ID));
        verify(categoryRepository, times(1)).findById(CATEGORY_ID);
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void deleteCategory() {
        categoryServiceUnderTest.deleteCategory(TOKEN_ADMIN, CATEGORY_ID);
        verify(categoryRepository).deleteById(CATEGORY_ID);
    }

    @Test
    void deleteCategory_UnauthorizedUserException() {
        assertThrows(UnauthorizedUserException.class, () -> categoryServiceUnderTest.deleteCategory(TOKEN_CUSTOMER, CATEGORY_ID));
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void deleteCategory_UserNotLoggedInException() {
        assertThrows(UserNotLoggedInException.class, () -> categoryServiceUnderTest.deleteCategory(TOKEN_NOT_LOGGED_IN, CATEGORY_ID));
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void deleteCategory_InvalidTokenException() {
        assertThrows(InvalidTokenException.class, () -> categoryServiceUnderTest.deleteCategory(TOKEN_INVALID, CATEGORY_ID));
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void getCategories() {
        List<Category> categories = List.of(new Category(), new Category(), new Category());
        when(categoryRepository.findAll()).thenReturn(categories);

        var result = categoryServiceUnderTest.getCategories();
        assertEquals(categories, result);
        assertEquals(categories.size(), result.size());
    }

    @Test
    void getCategories_DatabaseError() {
        when(categoryRepository.findAll()).thenThrow(DatabaseError.class);
        assertThrows(DatabaseError.class, () -> categoryServiceUnderTest.getCategories());
    }
}