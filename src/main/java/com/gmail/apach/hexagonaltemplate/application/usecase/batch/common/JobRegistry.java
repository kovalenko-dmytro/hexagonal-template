package com.gmail.apach.hexagonaltemplate.application.usecase.batch.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JobRegistry {

    IMPORT_USERS_JOB("importUsersJob", "IMPORT-USERS-JOB");

    private final String jobBean;
    private final String jobName;
}
