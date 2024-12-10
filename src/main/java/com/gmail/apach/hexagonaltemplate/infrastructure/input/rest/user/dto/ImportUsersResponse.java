package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto;

import lombok.Builder;

@Builder
public record ImportUsersResponse(
    String jobId
) {
}
