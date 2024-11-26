package com.gmail.apach.hexagonaltemplate.domain.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DomainError {

    FORBIDDEN_AUTHORITIES_NOT_FOUND("error.forbidden.authorities.not.found"),
    FORBIDDEN_USER_CREATION("error.forbidden.user.creation"),
    FORBIDDEN_USER_GET_BY_ID("error.forbidden.user.get.id"),
    FORBIDDEN_USER_GET_LIST("error.forbidden.user.get.list"),
    FORBIDDEN_USER_UPDATE_BY_ID("error.forbidden.user.update.id"),
    FORBIDDEN_USER_UPDATE_ENABLED("error.forbidden.user.update.enabled"),
    FORBIDDEN_USER_UPDATE_ROLES("error.forbidden.user.update.roles"),
    FORBIDDEN_USER_DELETE_BY_ID("error.forbidden.user.delete");

    private final String key;
}
