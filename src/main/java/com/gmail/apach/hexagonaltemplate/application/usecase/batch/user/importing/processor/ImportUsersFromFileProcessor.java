package com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

import java.util.List;

public interface ImportUsersFromFileProcessor {

    List<User> process(StoredFile file);
}
