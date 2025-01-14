package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.batch.job.processor;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import org.springframework.batch.core.JobParameters;

import java.util.List;

public interface ImportUsersFromFileProcessorStrategy {

    List<User> process(StoredFile file, JobParameters jobParameters);
}
