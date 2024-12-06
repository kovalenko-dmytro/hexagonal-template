package com.gmail.apach.hexagonaltemplate.application.usecase.user.importing.executor;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component(FileContentType.CSV)
@RequiredArgsConstructor
public class CsvImportUsersExecutor implements ImportUsersExecutor {

    @Override
    public void execute(StoredFile file) {
        throw new UnsupportedOperationException("hasn't supported yet");
    }
}
