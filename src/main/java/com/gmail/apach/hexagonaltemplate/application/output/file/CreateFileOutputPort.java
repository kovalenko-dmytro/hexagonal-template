package com.gmail.apach.hexagonaltemplate.application.output.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;

public interface CreateFileOutputPort {

    StoredFile createFile(StoredFile file);
}
