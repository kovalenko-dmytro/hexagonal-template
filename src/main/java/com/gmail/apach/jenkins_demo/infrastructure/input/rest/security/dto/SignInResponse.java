package com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto;

import lombok.Builder;

@Builder
public record SignInResponse(
    String tokenType,
    String accessToken,
    long accessTokenExpired
) {}
