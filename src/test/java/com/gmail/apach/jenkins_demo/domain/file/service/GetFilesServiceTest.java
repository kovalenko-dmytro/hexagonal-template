package com.gmail.apach.jenkins_demo.domain.file.service;

import com.gmail.apach.jenkins_demo.application.output.file.GetFilesOutputPort;
import com.gmail.apach.jenkins_demo.data.FilesTestData;
import com.gmail.apach.jenkins_demo.domain.file.wrapper.GetFilesRequestWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFilesServiceTest {

    @InjectMocks
    private GetFilesService getFilesService;
    @Mock
    private GetFilesOutputPort getFilesOutputPort;

    @Test
    void getFiles_success() {
        final var files = new PageImpl<>(FilesTestData.storedFiles());
        final var wrapper = GetFilesRequestWrapper.builder().page(1).size(1).build();

        when(getFilesOutputPort.getFiles(wrapper)).thenReturn(files);

        final var actual = getFilesService.getFiles(wrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(3, actual.getTotalElements());
    }
}