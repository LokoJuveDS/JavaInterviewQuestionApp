package com.jih.dto;

import jakarta.validation.constraints.NotBlank;

public record QuestionCreateRequest(
        @NotBlank(message = "Question is required")
        String question,
        @NotBlank(message = "Answer is required")
        String answer) {
}
