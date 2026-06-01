package com.jih.dto;

import java.time.LocalDateTime;

public record QuestionDto(
        Long id,
        String question,
        String answer,
        String language,
        Long categoryId,
        String categoryName,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
