package com.gmail.apach.hexagonaltemplate.infrastructure.common.config.batch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JobRegistry {

    IMPORT_USERS_FROM_FILE_JOB("importUsersFromFileJob", "IMPORT-USERS-FROM-FILE-JOB");

    private final String jobBean;
    private final String jobName;
}
