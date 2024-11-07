package com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.util.List;

@Builder
public record ErrorResponse(
     HttpStatus status,
     String message,
     List<String> errors,
     String timestamp
) {}
