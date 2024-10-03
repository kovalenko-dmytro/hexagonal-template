package com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper;

import lombok.Builder;

@Builder
public record AuthTokenDetails(
    String tokenType,
    String accessToken,
    long accessTokenExpired
) {}
