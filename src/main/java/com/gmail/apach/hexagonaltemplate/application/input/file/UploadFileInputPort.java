package com.gmail.apach.hexagonaltemplate.application.input.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileInputPort {

    StoredFile uploadFile(MultipartFile file);
}
