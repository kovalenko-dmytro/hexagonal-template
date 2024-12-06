package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ImportUsersInputType(
    @NotBlank
    String fileId
) {
}
