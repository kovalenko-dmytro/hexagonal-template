package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record ImportUsersFromFileRequest(
    @NotBlank
    String fileId
) {
}
