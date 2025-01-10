package com.gmail.apach.hexagonaltemplate.domain.excel.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum ExcelFileExtension {
    HSSF("xls"), XSSF("xlsx");

    private static final Map<String, ExcelFileExtension> CACHE = new HashMap<>();
    private final String extension;

    static {
        for (var extension : ExcelFileExtension.values()) {
            CACHE.put(extension.extension, extension);
        }
    }

    public static ExcelFileExtension from(String extension) {
        return Optional.ofNullable(CACHE.get(extension))
            .orElseThrow(() ->
                new IllegalArgumentException("Excel file extension " + extension + " hasn't supported yet"));

    }
}
