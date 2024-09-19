package com.gmail.apach.jenkins_demo.data;

import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilesTestData {

    private static final String FILE_PATH = "src/test/resources/file/test.txt";
    private static final String ORIGINAL_FILE_NAME = "test.txt";
    private static final String REQUEST_PARAM_NAME = "file";

    public static StoredFile file() {
        return StoredFile.builder()
            .fileName("test.txt")
            .contentType("text/plain")
            .size(29L)
            .created(LocalDateTime.now())
            .build();
    }

    public static StoredFile storedFile() {
        return StoredFile.builder()
            .fileId("qqed68c8-2f28-4b53-ac5a-2db586512eee")
            .storageKey("eeed68c8-2f28-4b53-ac5a-2db586512e55")
            .fileName("file")
            .contentType("text/plain")
            .size(123L)
            .created(LocalDateTime.now())
            .build();
    }

    public static List<StoredFile> storedFiles() {
        return List.of(
            StoredFile.builder()
                .fileId("qqed68c8-2f28-4b53-ac5a-2db586512eee")
                .storageKey("eeed68c8-2f28-4b53-ac5a-2db586512e55")
                .fileName("file1")
                .contentType("text/plain")
                .size(123L)
                .created(LocalDateTime.now())
                .build(),
            StoredFile.builder()
                .fileId("qqed68c8-2f28-4b53-ac5a-2db586512ee1")
                .storageKey("eeed68c8-2f28-4b53-ac5a-2db586512e56")
                .fileName("file2")
                .contentType("text/plain")
                .size(125L)
                .created(LocalDateTime.now())
                .build(),
            StoredFile.builder()
                .fileId("qqed68c8-2f28-4b53-ac5a-2db586512ee2")
                .storageKey("eeed68c8-2f28-4b53-ac5a-2db586512e57")
                .fileName("file3")
                .contentType("text/plain")
                .size(127L)
                .created(LocalDateTime.now())
                .build()
        );
    }

    public static MockMultipartFile getUploadedFile() throws IOException {
        return new MockMultipartFile(
            REQUEST_PARAM_NAME,
            ORIGINAL_FILE_NAME,
            MediaType.TEXT_PLAIN_VALUE,
            Files.readAllBytes(new File(FILE_PATH).toPath()));
    }
}
