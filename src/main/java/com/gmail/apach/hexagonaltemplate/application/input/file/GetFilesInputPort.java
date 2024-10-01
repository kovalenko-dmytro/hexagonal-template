package com.gmail.apach.hexagonaltemplate.application.input.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.file.wrapper.GetFilesRequestWrapper;
import org.springframework.data.domain.Page;

public interface GetFilesInputPort {

    Page<StoredFile> getFiles(GetFilesRequestWrapper wrapper);
}
