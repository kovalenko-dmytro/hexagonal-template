package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto;

import lombok.Builder;

@Builder
public record SignInOutputType(
    String tokenType,
    String accessToken,
    long accessTokenExpired
) {}
