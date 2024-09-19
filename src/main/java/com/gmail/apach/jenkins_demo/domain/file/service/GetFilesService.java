package com.gmail.apach.jenkins_demo.domain.file.service;

import com.gmail.apach.jenkins_demo.application.input.file.GetFilesInputPort;
import com.gmail.apach.jenkins_demo.application.output.file.GetFilesOutputPort;
import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import com.gmail.apach.jenkins_demo.domain.file.wrapper.GetFilesRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetFilesService implements GetFilesInputPort {

    private final GetFilesOutputPort getFilesOutputPort;

    @Override
    public Page<StoredFile> getFiles(GetFilesRequestWrapper wrapper) {
        return getFilesOutputPort.getFiles(wrapper);
    }
}
