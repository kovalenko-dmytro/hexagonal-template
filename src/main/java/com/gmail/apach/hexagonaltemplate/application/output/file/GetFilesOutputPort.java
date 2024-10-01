package com.gmail.apach.hexagonaltemplate.application.output.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.file.wrapper.GetFilesRequestWrapper;
import org.springframework.data.domain.Page;

public interface GetFilesOutputPort {

    Page<StoredFile> getFiles(GetFilesRequestWrapper wrapper);
}
