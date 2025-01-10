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
    FORBIDDEN_USER_DELETE_BY_ID("error.forbidden.user.delete"),
    ERROR_READ_EXCEL_FILE("error.read.excel.file"),
    ERROR_EXCEL_FILE_EXTENSION("error.excel.file.extension.not.supported"),
    ERROR_EXCEL_CELL_DATA_TYPE("error.excel.file.cell.datatype.not.supported"),
    ERROR_EXCEL_SHEET_NOT_EXIST("error.excel.sheet.not.exist"),
    ERROR_EXCEL_SHEET_HEADERS("error.excel.sheet.headers"),
    ERROR_EXCEL_DATA_TYPE_MUST_STRING("error.excel.datatype.must.string"),
    ERROR_EXCEL_DATA_TYPE_MUST_BOOLEAN("error.excel.datatype.must.boolean"),
    ERROR_EXCEL_VALUES_MUST_PRESENT("error.excel.values.must.present"),
    ERROR_USER_USERNAME("error.user.username"),
    ERROR_USER_PASSWORD("error.user.password"),
    ERROR_USER_FIRST_NAME("error.user.firstname"),
    ERROR_USER_LAST_NAME("error.user.lastname"),
    ERROR_USER_EMAIL("error.user.email"),
    ERROR_USER_ROLE("error.user.role");

    private final String key;
}
