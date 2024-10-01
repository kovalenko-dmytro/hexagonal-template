package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.dto;

import lombok.Builder;

@Builder
public record SignInResponse(
    String tokenType,
    String accessToken,
    long accessTokenExpired
) {}
