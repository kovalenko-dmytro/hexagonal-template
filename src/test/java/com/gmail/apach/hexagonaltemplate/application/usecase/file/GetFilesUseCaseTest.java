package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFilesOutputPort;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.wrapper.GetFilesFilterWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetFilesUseCaseTest {

    @InjectMocks
    private GetFilesUseCase getFilesUseCase;
    @Mock
    private GetFilesOutputPort getFilesOutputPort;

    @Test
    void getFiles_success() {
        final var files = new PageImpl<>(FilesTestData.storedFiles());

        when(getFilesOutputPort.getFiles(any(GetFilesFilterWrapper.class))).thenReturn(files);

        final var actual = getFilesUseCase.getFiles(null, null, null,
            1, 1, new String[]{"sendBy"});

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(3, actual.getTotalElements());
    }
}