package com.gmail.apach.jenkins_demo.application.input.file;

import com.gmail.apach.jenkins_demo.domain.file.wrapper.FilePayloadWrapper;

public interface DownloadFileInputPort {

    FilePayloadWrapper downloadFile(String fileId);
}
