package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto;

import lombok.Builder;

@Builder
public record SignInResponse(
    String tokenType,
    String accessToken,
    long accessTokenExpired
) {}
