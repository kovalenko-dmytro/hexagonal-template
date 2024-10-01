package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FileResponse(
    String fileId,
    String storageKey,
    String fileName,
    String contentType,
    long size,
    LocalDateTime created
) {
}
