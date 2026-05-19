package com.jih.model;

import jakarta.validation.constraints.NotBlank;

public record QuestionCreateRequest(
        @NotBlank String question,
        @NotBlank String answer) {
}
