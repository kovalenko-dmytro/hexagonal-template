package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignInInputType(
    @NotBlank
    @Size(min = 2, max = 50)
    String username,
    @Size(min = 2, max = 50)
    String password
) {


}
