package com.gmail.apach.hexagonaltemplate.application.port.input.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;

public interface DownloadFileInputPort {
    StoredFile download(String fileId);
}
