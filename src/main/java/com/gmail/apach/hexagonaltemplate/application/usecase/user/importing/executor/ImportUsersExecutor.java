package com.gmail.apach.hexagonaltemplate.application.usecase.user.importing.executor;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;

public interface ImportUsersExecutor {

    void execute(StoredFile file);
}
