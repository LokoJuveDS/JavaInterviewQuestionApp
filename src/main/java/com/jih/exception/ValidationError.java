package com.jih.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationError(
        String message,
        int status,
        LocalDateTime timestamp,
        Map<String, String> errors
) {
}
