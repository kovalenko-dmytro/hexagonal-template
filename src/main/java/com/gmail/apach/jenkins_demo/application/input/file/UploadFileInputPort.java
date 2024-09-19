package com.gmail.apach.jenkins_demo.application.input.file;

import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import org.springframework.web.multipart.MultipartFile;

public interface UploadFileInputPort {

    StoredFile uploadFile(MultipartFile file);
}
