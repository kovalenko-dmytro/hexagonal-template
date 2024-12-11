package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ImportUsersRequest(
    @NotBlank
    String fileId
) {
}
