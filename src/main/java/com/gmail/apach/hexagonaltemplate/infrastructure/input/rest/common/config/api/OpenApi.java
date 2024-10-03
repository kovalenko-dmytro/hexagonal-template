package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.config.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OpenApi {

    OPEN_API_INFO_TITLE("Backend application REST API documentation"),
    SECURITY_SCHEME_NAME("bearerAuth"),
    BEARER_FORMAT("JWT"),
    SECURITY_SCHEME("bearer");

    private final String value;
}
