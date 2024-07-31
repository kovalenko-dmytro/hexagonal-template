package com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.constant.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum OpenApiAsset {

    API_DOCS("/api-docs/**"),
    API_DOCS_V3("/v3/api-docs/**"),
    CONFIG_UI("/configuration/ui"),
    SWAGGER_RESOURCES("/swagger-resources/**"),
    CONFIG_SECURITY("/configuration/security"),
    WEBJARS("/webjars/**"),
    SWAGGER_UI("/swagger-ui/**");

    private final String value;

    public static String[] getAssets() {
        return Arrays
            .stream(OpenApiAsset.values())
            .map(OpenApiAsset::getValue)
            .toArray(String[]::new);
    }
}
