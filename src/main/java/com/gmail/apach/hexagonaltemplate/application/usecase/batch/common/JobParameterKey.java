package com.gmail.apach.hexagonaltemplate.application.usecase.batch.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JobParameterKey {
    public static final String BATCH_ID = "batchId";
    public static final String FILE_ID = "fileId";
    public static final String PRINCIPAL_USERNAME = "username";
}
