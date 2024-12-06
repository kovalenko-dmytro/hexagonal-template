package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum JobRegistry {

    IMPORT_USERS_JOB("importUsersJob");

    private final String jobBean;
}
