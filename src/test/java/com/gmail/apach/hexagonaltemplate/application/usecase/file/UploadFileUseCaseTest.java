package com.gmail.apach.hexagonaltemplate.application.usecase.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.CreateFileOutputPort;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.file.vo.StoredResource;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ApplicationServerException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.oss.s3.AwsS3StorageServiceAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadFileUseCaseTest {

    private static final String FILE_PATH = "src/test/resources/file/test.txt";
    private static final String ORIGINAL_FILE_NAME = "test.txt";
    private static final MockMultipartFile multipartFile;

    @InjectMocks
    private UploadFileUseCase uploadFileUseCase;
    @Mock
    private AwsS3StorageServiceAdapter awsS3Adapter;
    @Mock
    private CreateFileOutputPort createFileOutputPort;

    static {
        try {
            multipartFile = new MockMultipartFile(ORIGINAL_FILE_NAME, ORIGINAL_FILE_NAME,
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(new File(FILE_PATH).toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void uploadFile_success() {
        final var resource = mock(StoredResource.class);

        when(awsS3Adapter.save(multipartFile)).thenReturn(resource);
        when(createFileOutputPort.create(any(StoredFile.class))).thenReturn(FilesTestData.storedFile());

        assertDoesNotThrow(() -> uploadFileUseCase.upload(multipartFile));
    }

    @Test
    void uploadFile_cantSaveFile() {
        doThrow(new ApplicationServerException("uploadError"))
            .when(awsS3Adapter).save(multipartFile);

        assertThrows(ApplicationServerException.class, () -> uploadFileUseCase.upload(multipartFile));
    }
}