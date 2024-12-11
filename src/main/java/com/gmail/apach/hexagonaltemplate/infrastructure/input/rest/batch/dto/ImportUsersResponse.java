package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto;

import lombok.Builder;

@Builder
public record ImportUsersResponse(
    String batchId
) {
}
