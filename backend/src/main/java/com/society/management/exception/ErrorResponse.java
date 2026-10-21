package com.society.management.exception;

import java.time.OffsetDateTime;

public record ErrorResponse(
    int status,
    String error,
    String message,
    OffsetDateTime timestamp
) {
}
