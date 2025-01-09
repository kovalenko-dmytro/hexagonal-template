package com.gmail.apach.hexagonaltemplate.domain.user.constant;

import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.CommonConstant;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
@Getter
public enum ImportUsersExcelHeaders {

    USERNAME("username"),
    PASSWORD("password"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    EMAIL("email"),
    USER("user"),
    MANAGER("manager");

    public static final Map<String, ImportUsersExcelHeaders> headers = new HashMap<>();
    private final String header;

    static {
        for (var header : ImportUsersExcelHeaders.values()) {
            headers.put(header.getHeader(), header);
        }
    }

    public static boolean existAndNoDuplicates(List<String> fileHeaders) {
        final var normalized = new HashSet<>(fileHeaders);
        return fileHeaders.size() == normalized.size() && normalized.containsAll(headers.keySet());
    }

    public static String headersJoinToString() {
        return String.join(CommonConstant.COMMA.getValue(), headers.keySet());
    }

    public static String stringTypeheadersJoinToString() {
        return String.join(CommonConstant.COMMA.getValue(),
            Set.of(
                ImportUsersExcelHeaders.USERNAME.header,
                ImportUsersExcelHeaders.PASSWORD.header,
                ImportUsersExcelHeaders.FIRST_NAME.header,
                ImportUsersExcelHeaders.LAST_NAME.header,
                ImportUsersExcelHeaders.EMAIL.header
            ));
    }

    public static String booleanTypeheadersJoinToString() {
        return String.join(CommonConstant.COMMA.getValue(),
            Set.of(
                ImportUsersExcelHeaders.USER.header,
                ImportUsersExcelHeaders.MANAGER.header
            ));
    }
}
