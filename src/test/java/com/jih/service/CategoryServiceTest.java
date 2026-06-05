package com.jih.service;

import com.jih.dto.CategoryCreateRequest;
import com.jih.dto.CategoryDto;
import com.jih.exception.DuplicateResourceException;
import com.jih.exception.ResourceNotFoundException;
import com.jih.model.entity.Category;
import com.jih.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void shouldReturnAllCategories() {
        // given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("category 1");
        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("category 2");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        // when
        List<CategoryDto> result = categoryService.findAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().id()).isEqualTo(category1.getId());
        assertThat(result.getFirst().name()).isEqualTo(category1.getName());
    }

    @Test
    void shouldFindCategoryById() {
        // given
        Category existingCategory = new Category();
        existingCategory.setId(1L);
        existingCategory.setName("category");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(existingCategory));

        // when
        CategoryDto result = categoryService.findById(1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(existingCategory.getId());
        assertThat(result.name()).isEqualTo(existingCategory.getName());
    }

    @Test
    void shouldThrowExceptionWhenCategoryNotFoundById() {
        // given
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> categoryService.findById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found with id: 1");
    }


    @Test
    void shouldCreateCategory() {
        // given
        CategoryCreateRequest request = new CategoryCreateRequest("category 1");

        when(categoryRepository.existsByName(request.name())).thenReturn(false);
        when(categoryRepository.save(any(Category.class)))
                .thenAnswer(invocation -> {
                    Category category = invocation.getArgument(0);
                    category.setId(10L);
                    return category;
                });

        // when
        CategoryDto result = categoryService.create(request);

        // then
        verify(categoryRepository).existsByName(request.name());
        verify(categoryRepository).save(any(Category.class));

        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo(request.name());
    }

    @Test
    void shouldThrowExceptionWhenCategoryDuplicates() {
        // given
        CategoryCreateRequest request = new CategoryCreateRequest("category 1");

        when(categoryRepository.existsByName(request.name())).thenReturn(true);

        // when
        assertThatThrownBy(() -> categoryService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Category already exists with name: category 1");

        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void shouldDeleteCategory() {
        // given
        Long id = 1L;

        Category existingCategory = new Category();
        existingCategory.setId(id);
        existingCategory.setName("category 1");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));

        // when
        categoryService.delete(id);

        // then
        verify(categoryRepository).findById(id);
        verify(categoryRepository).delete(existingCategory);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingCategory() {
        // given
        Long id = 1L;

        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> categoryService.delete(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found with id: 1");

        verify(categoryRepository, never()).delete(any(Category.class));
    }
}
