package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileUseCase {
    StoredFile uploadFile(MultipartFile file);
}
