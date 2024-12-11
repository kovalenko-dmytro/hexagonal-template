package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.batch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ImportUsersInputType(
    @NotBlank
    String fileId
) {
}
