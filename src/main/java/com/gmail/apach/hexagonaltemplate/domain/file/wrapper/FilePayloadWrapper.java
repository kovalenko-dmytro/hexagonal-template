package com.gmail.apach.hexagonaltemplate.domain.file.wrapper;

import lombok.Builder;

@Builder
public record FilePayloadWrapper(
    String fileName,
    byte[] payload
) {
}
