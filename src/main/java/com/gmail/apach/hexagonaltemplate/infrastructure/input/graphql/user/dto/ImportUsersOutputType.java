package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto;

import lombok.Builder;

@Builder
public record ImportUsersOutputType(
    String jobId
) {
}
