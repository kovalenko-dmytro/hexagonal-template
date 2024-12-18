package com.gmail.apach.hexagonaltemplate.application.port.output.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.wrapper.GetFilesFilterWrapper;
import org.springframework.data.domain.Page;

public interface GetFilesOutputPort {
    Page<StoredFile> get(GetFilesFilterWrapper wrapper);
}
