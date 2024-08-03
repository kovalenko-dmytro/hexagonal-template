package com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record SignInRequest(
    @NotBlank
    @Size(min = 2, max = 50)
    String username,
    @Size(min = 2, max = 50)
    String password
) {


}
