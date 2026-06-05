package com.jih.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jih.dto.CategoryCreateRequest;
import com.jih.dto.CategoryDto;
import com.jih.exception.DuplicateResourceException;
import com.jih.exception.ResourceNotFoundException;
import com.jih.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    private static final String BASE_URL = "/api/v1/categories";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldFindCategoryById() throws Exception {
        // given
        CategoryDto category = createCategoryDto(1L, "category 1");

        when(categoryService.findById(1L)).thenReturn(category);

        // then
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("category 1"));
    }

    @Test
    void shouldReturn404WhenCategoryNotFound() throws Exception {
        // given
        when(categoryService.findById(1L))
                .thenThrow(new ResourceNotFoundException("Category", 1L));

        // then
        mockMvc.perform(get(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category not found with id: 1"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldReturnAllCategories() throws Exception {
        // given
        List<CategoryDto> categories = List.of(
                createCategoryDto(1L, "category 1"),
                createCategoryDto(2L, "category 2")
        );

        when(categoryService.findAll()).thenReturn(categories);

        // then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("category 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("category 2"));
    }

    @Test
    void shouldCreateCategory() throws Exception {
        // given
        CategoryCreateRequest request = createCategoryCreateRequest("category 1");

        CategoryDto response = createCategoryDto(1L, "category 1");

        when(categoryService.create(request)).thenReturn(response);

        // then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("category 1"));
    }

    @Test
    void shouldReturn400WhenCategoryNameIsBlank() throws Exception {
        // given
        CategoryCreateRequest request = createCategoryCreateRequest("");

        // then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).create(any(CategoryCreateRequest.class));
    }

    @Test
    void shouldReturn400WhenCategoryNameIsTooLong() throws Exception {
        // given
        CategoryCreateRequest request = createCategoryCreateRequest("a".repeat(101));

        // then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).create(any(CategoryCreateRequest.class));
    }

    @Test
    void shouldReturn409WhenCategoryAlreadyExists() throws Exception {
        // given
        CategoryCreateRequest request = createCategoryCreateRequest("category 1");

        when(categoryService.create(request))
                .thenThrow(new DuplicateResourceException("Category", "name", "category 1"));

        // then
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Category already exists with name: category 1"))
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void shouldDeleteCategory() throws Exception {
        // then
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(1L);
    }

    @Test
    void shouldReturn404WhenDeletingCategoryNotFound() throws Exception {
        // given
        doThrow(new ResourceNotFoundException("Category", 1L))
                .when(categoryService).delete(1L);

        // then
        mockMvc.perform(delete(BASE_URL + "/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Category not found with id: 1"))
                .andExpect(jsonPath("$.status").value(404));
    }

    private CategoryDto createCategoryDto(Long id, String name) {
        return new CategoryDto(
                id,
                name,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    private CategoryCreateRequest createCategoryCreateRequest(String name) {
        return new CategoryCreateRequest(name);
    }
}