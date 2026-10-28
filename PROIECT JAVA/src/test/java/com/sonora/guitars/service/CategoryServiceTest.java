package com.sonora.guitars.service;

import com.sonora.guitars.entity.Category;
import com.sonora.guitars.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = Category.builder()
                .id(1L)
                .name("Chitări Acustice")
                .description("Chitări acustice clasice")
                .build();
    }

    @Test
    void testSaveCategory_Success() {
        when(categoryRepository.findByName("Chitări Acustice")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory);

        Category saved = categoryService.saveCategory(testCategory);

        assertNotNull(saved);
        assertEquals("Chitări Acustice", saved.getName());
        verify(categoryRepository, times(1)).save(testCategory);
    }

    @Test
    void testSaveCategory_DuplicateName_ThrowsException() {
        Category existingCategory = Category.builder()
                .id(2L) // Different ID but same name
                .name("Chitări Acustice")
                .build();

        when(categoryRepository.findByName("Chitări Acustice")).thenReturn(Optional.of(existingCategory));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.saveCategory(testCategory);
        });

        assertTrue(exception.getMessage().contains("există deja"));
        verify(categoryRepository, never()).save(any());
    }

    @Test
    void testDeleteCategory_NotFound_ThrowsException() {
        when(categoryRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            categoryService.deleteCategory(99L);
        });

        assertTrue(exception.getMessage().contains("nu a fost găsită"));
        verify(categoryRepository, never()).deleteById(anyLong());
    }
}
