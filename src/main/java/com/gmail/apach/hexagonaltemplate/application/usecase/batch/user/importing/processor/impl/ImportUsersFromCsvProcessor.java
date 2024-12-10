package com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor.impl;

import com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor.FileContentType;
import com.gmail.apach.hexagonaltemplate.application.usecase.batch.user.importing.processor.ImportUsersFromFileProcessor;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(FileContentType.CSV)
@RequiredArgsConstructor
public class ImportUsersFromCsvProcessor implements ImportUsersFromFileProcessor {

    @Override
    public List<User> process(StoredFile file) {
        throw new UnsupportedOperationException("hasn't supported yet");
    }
}
