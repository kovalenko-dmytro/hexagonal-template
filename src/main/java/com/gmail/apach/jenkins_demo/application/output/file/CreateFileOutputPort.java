package com.gmail.apach.jenkins_demo.application.output.file;

import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;

public interface CreateFileOutputPort {

    StoredFile createFile(StoredFile file);
}
