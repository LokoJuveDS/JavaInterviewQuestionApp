package com.jih.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record QuestionCreateRequest(
        @NotBlank(message = "Question is required")
        String question,
        @NotBlank(message = "Answer is required")
        String answer,
        @NotBlank(message = "Language is required")
        String language,
        @NotNull(message = "Category ID is required")
        Long categoryId) {
}
