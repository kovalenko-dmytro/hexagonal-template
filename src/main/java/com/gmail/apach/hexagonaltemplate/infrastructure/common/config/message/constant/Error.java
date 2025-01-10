package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant;

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
    FORBIDDEN_AUTHORITIES_NOT_FOUND("error.forbidden.authorities.not.found"),
    FORBIDDEN_USER_CREATION("error.forbidden.user.creation"),
    FORBIDDEN_USER_GET_BY_ID("error.forbidden.user.get.id"),
    FORBIDDEN_USER_GET_LIST("error.forbidden.user.get.list"),
    FORBIDDEN_USER_UPDATE_BY_ID("error.forbidden.user.update.id"),
    FORBIDDEN_USER_UPDATE_ENABLED("error.forbidden.user.update.enabled"),
    FORBIDDEN_USER_UPDATE_ROLES("error.forbidden.user.update.roles"),
    FORBIDDEN_USER_DELETE_BY_ID("error.forbidden.user.delete"),
    INTERNAL_SERVER_ERROR_OCCURRED("error.internal.server.error.occurred"),
    EMAIL_SEND_ERROR("error.email.send"),
    EMAIL_TEMPLATE_NOT_FOUND("error.email.template.not.found"),
    FILE_STORAGE_UNABLE_UPLOAD("error.file.storage.unable.upload"),
    FILE_STORAGE_UNABLE_READ("error.file.storage.unable.read"),
    JOB_FAILED("error.job.failed");

    private final String key;
}
