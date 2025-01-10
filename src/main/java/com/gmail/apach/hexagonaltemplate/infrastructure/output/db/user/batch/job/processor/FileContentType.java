package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileContentType {

    public static final String EXCEL = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String CSV = "text/csv";
}
