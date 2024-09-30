package com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.constant.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OpenApi {

    OPEN_API_INFO_TITLE("Backend application REST API documentation");

    private final String value;
}
