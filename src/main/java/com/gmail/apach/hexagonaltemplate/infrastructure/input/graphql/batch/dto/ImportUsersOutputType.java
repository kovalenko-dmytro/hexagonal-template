package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.batch.dto;

import lombok.Builder;

@Builder
public record ImportUsersOutputType(
    String jobId
) {
}
