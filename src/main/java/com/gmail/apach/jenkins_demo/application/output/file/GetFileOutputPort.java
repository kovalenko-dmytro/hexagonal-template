package com.gmail.apach.jenkins_demo.application.output.file;

import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;

public interface GetFileOutputPort {

    StoredFile getByFileId(String fileId);
}
