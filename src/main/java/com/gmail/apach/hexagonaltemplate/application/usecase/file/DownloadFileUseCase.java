package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.DownloadedFile;

public interface DownloadFileUseCase {
    DownloadedFile downloadFile(String fileId);
}
