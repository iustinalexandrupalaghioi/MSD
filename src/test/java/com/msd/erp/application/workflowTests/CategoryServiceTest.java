package com.msd.erp.application.workflowTests;

import com.msd.erp.application.services.CategoryService;
import com.msd.erp.application.validations.DomainValidationService;
import com.msd.erp.domain.Category;
import com.msd.erp.infrastructure.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private CategoryService categoryService;
    private CategoryRepository categoryRepository;
    private DomainValidationService validationService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        validationService = mock(DomainValidationService.class);
        categoryService = new CategoryService(categoryRepository, validationService);
    }

    @Test
    void createCategory_ShouldSaveAndReturnCategory() {
        Category category = new Category(null, "Electronics", "All electronic items");
        Category savedCategory = new Category(1L, "Electronics", "All electronic items");

        when(categoryRepository.save(category)).thenReturn(savedCategory);

        Category result = categoryService.createCategory(category);

        verify(validationService).validateEntity(category);
        verify(categoryRepository).save(category);
        assertEquals(savedCategory, result);
    }

    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        Category category1 = new Category(1L, "Electronics", "All electronic items");
        Category category2 = new Category(2L, "Furniture", "Home and office furniture");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        List<Category> result = categoryService.getAllCategories();

        verify(categoryRepository).findAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(category1));
        assertTrue(result.contains(category2));
    }

    @Test
    void getCategoryById_ShouldReturnCategoryIfFound() {
        Category category = new Category(1L, "Electronics", "All electronic items");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryById(1L);

        verify(categoryRepository).findById(1L);
        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void getCategoryById_ShouldReturnEmptyOptionalIfNotFound() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryById(1L);

        verify(categoryRepository).findById(1L);
        assertTrue(result.isEmpty());
    }

    @Test
    void updateCategory_ShouldUpdateAndReturnUpdatedCategory() {
        Category existingCategory = new Category(1L, "Electronics", "All electronic items");
        Category updatedCategory = new Category(1L, "Electronics", "Updated description");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);

        Optional<Category> result = categoryService.updateCategory(1L, updatedCategory);

        assertTrue(result.isPresent());
        assertEquals("Updated description", result.get().getDescription());

        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(existingCategory);

        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        verify(categoryRepository).save(captor.capture());
        assertEquals("Updated description", captor.getValue().getDescription());
    }

    @Test
    void deleteCategory_ShouldDeleteIfExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        categoryService.deleteCategory(1L);

        verify(categoryRepository).existsById(1L);
        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_ShouldThrowExceptionIfNotExists() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.deleteCategory(1L);
        });

        assertEquals("Category with ID 1 does not exist.", exception.getMessage());
        verify(categoryRepository).existsById(1L);
        verify(categoryRepository, never()).deleteById(1L);
    }

    @Test
    void categoryExists_ShouldReturnTrueIfExists() {
        when(categoryRepository.existsById(1L)).thenReturn(true);

        boolean exists = categoryService.categoryExists(1L);

        verify(categoryRepository).existsById(1L);
        assertTrue(exists);
    }

    @Test
    void categoryExists_ShouldReturnFalseIfNotExists() {
        when(categoryRepository.existsById(1L)).thenReturn(false);

        boolean exists = categoryService.categoryExists(1L);

        verify(categoryRepository).existsById(1L);
        assertFalse(exists);
    }

    @Test
    void getCategoryByName_ShouldReturnCategoryIfFound() {
        Category category = new Category(1L, "Electronics", "All electronic items");

        when(categoryRepository.findByName("Electronics")).thenReturn(Optional.of(category));

        Optional<Category> result = categoryService.getCategoryByName("Electronics");

        verify(categoryRepository).findByName("Electronics");
        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void getCategoryByName_ShouldReturnEmptyOptionalIfNotFound() {
        when(categoryRepository.findByName("Unknown")).thenReturn(Optional.empty());

        Optional<Category> result = categoryService.getCategoryByName("Unknown");

        verify(categoryRepository).findByName("Unknown");
        assertTrue(result.isEmpty());
    }
}
