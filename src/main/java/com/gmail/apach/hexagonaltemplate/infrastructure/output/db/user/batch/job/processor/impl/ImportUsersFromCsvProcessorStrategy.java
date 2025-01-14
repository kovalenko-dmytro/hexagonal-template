package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.impl;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.FileContentType;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor.ImportUsersFromFileProcessorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Component;

import java.util.List;

@Component(FileContentType.CSV)
@RequiredArgsConstructor
public class ImportUsersFromCsvProcessorStrategy implements ImportUsersFromFileProcessorStrategy {

    @Override
    public List<User> process(StoredFile file, JobParameters jobParameters) {
        throw new UnsupportedOperationException("hasn't supported yet");
    }
}
