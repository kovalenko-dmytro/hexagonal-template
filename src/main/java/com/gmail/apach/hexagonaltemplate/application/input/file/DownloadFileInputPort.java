package com.gmail.apach.hexagonaltemplate.application.input.file;

import com.gmail.apach.hexagonaltemplate.domain.file.wrapper.FilePayloadWrapper;

public interface DownloadFileInputPort {

    FilePayloadWrapper downloadFile(String fileId);
}
