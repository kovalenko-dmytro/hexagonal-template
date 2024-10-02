package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AttributeForModel {

    USER("User"),
    ROLE("Role"),
    EMAIL("Email"),
    STORED_FILE("File");

    private final String name;

    @AllArgsConstructor
    @Getter
    public enum Field {

        ID("id"),
        USER_NAME("username"),
        NAME("name");

        private final String fieldName;
    }
}
