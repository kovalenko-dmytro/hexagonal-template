package com.gmail.apach.jenkins_demo.application.input.file;

import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import com.gmail.apach.jenkins_demo.domain.file.wrapper.GetFilesRequestWrapper;
import org.springframework.data.domain.Page;

public interface GetFilesInputPort {

    Page<StoredFile> getFiles(GetFilesRequestWrapper wrapper);
}
