package com.gmail.apach.jenkins_demo.application.output.file;

import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import com.gmail.apach.jenkins_demo.domain.file.wrapper.GetFilesRequestWrapper;
import org.springframework.data.domain.Page;

public interface GetFilesOutputPort {

    Page<StoredFile> getFiles(GetFilesRequestWrapper wrapper);
}
