package com.gmail.apach.hexagonaltemplate.application.port.input.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.DownloadedFile;

public interface DownloadFileInputPort {
    DownloadedFile downloadFile(String fileId);
}
