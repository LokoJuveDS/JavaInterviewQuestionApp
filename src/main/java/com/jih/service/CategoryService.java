package com.jih.service;

import com.jih.dto.CategoryCreateRequest;
import com.jih.dto.CategoryDto;
import com.jih.exception.DuplicateResourceException;
import com.jih.exception.ResourceNotFoundException;
import com.jih.model.entity.Category;
import com.jih.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    public CategoryDto findById(Long id) {
        return toDto(findCategoryByIdOrThrow(id));
    }

    @Transactional
    public CategoryDto create(CategoryCreateRequest request) {
        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateResourceException("Category", "name", request.name());
        }
        Category category = createEntity(request);

        Category saved = categoryRepository.save(category);

        log.info("Created category with id {}", saved.getId()); //add category name?

        return toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        Category category = findCategoryByIdOrThrow(id);
        categoryRepository.delete(category);

        log.info("Deleted category with id {}", id); // add category name?
    }

    private Category findCategoryByIdOrThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", id));
    }

    private Category createEntity(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.name());
        return category;
    }

    private CategoryDto toDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
