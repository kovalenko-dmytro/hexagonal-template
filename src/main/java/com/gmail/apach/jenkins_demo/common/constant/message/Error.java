package com.gmail.apach.jenkins_demo.common.constant.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {

    ENTITY_NOT_FOUND("error.entity.not.found"),
    JWT_TOKEN_NOT_FOUND("error.token.not.found"),
    ENTITY_CREATION_EXCEPTION("error.entity.created"),
    VALIDATION_REQUEST("error.validation.request"),
    MISSING_REQUEST_PARAMETER("error.missing.request.parameter"),
    DATA_ACCESS("error.data.access"),
    NO_HANDLER_FOUND("error.no.handler.found"),
    HTTP_METHOD_NOT_ALLOWED("error.http.method.not.allowed"),
    MEDIA_TYPE_NOT_SUPPORTED("error.media.type.not.supported"),
    UNAUTHORISED("error.unauthorised"),
    INTERNAL_SERVER_ERROR_OCCURRED("error.internal.server.error.occurred");

    private final String key;
}