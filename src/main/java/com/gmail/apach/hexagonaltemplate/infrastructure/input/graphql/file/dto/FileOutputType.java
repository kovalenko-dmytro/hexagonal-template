package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.file.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record FileOutputType(
    String fileId,
    String storageKey,
    String fileName,
    String contentType,
    long size,
    LocalDateTime created
) {
}
