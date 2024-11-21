package com.gmail.apach.hexagonaltemplate.application.port.input.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface GetFilesInputPort {
    Page<StoredFile> getFiles(String fileName,
                              LocalDate createdFrom,
                              LocalDate createdTo,
                              int page,
                              int size,
                              String[] sort);
}
