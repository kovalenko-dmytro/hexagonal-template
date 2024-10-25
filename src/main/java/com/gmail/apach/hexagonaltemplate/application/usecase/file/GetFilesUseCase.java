package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface GetFilesUseCase {
    Page<StoredFile> getFiles(String fileName,
                              LocalDate createdFrom,
                              LocalDate createdTo,
                              int page,
                              int size,
                              String[] sort);
}
