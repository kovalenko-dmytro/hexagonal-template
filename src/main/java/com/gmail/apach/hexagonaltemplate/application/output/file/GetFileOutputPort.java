package com.gmail.apach.hexagonaltemplate.application.output.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;

public interface GetFileOutputPort {

    StoredFile getByFileId(String fileId);
}
