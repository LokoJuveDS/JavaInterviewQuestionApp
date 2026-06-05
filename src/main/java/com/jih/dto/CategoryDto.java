package com.jih.dto;


import java.time.LocalDateTime;

public record CategoryDto(
        Long id,
        String name,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
