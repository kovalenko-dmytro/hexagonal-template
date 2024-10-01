package com.gmail.apach.hexagonaltemplate.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommonConstant {

    MESSAGE_SOURCE_PATH("classpath:messages/messages"),
    AUTH_HEADER("Authorization"),
    BASIC_AUTH_HEADER_PREFIX("Basic "),
    BEARER_AUTH_HEADER_PREFIX("Bearer "),
    EMAIL_TEMPLATES_PATH("/email-templates/"),
    EMAIL_PROPERTIES_SOURCE_PATH("classpath:email-properties/email_properties"),
    EMAIL_TEMPLATE_SUFFIX(".html"),
    COLON(":"),
    DOT("."),
    DASH("-"),
    EQUAL("="),
    COMMA(","),
    SPACE(" "),
    SLASH("/"),
    EMPTY(""),
    DESC("DESC"),
    DEFAULT_CHARSET("UTF-8");

    private final String value;
}
