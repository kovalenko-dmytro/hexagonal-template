package com.gmail.apach.hexagonaltemplate.data;

import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.domain.file.vo.StoredResource;
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
    private static final String IMPORT_USERS_FILE_PATH = "src/test/resources/file/insert_users.xlsx";
    private static final String IMPORT_USERS_ORIGINAL_FILE_NAME = "insert_users.xlsx";

    public static StoredFile file() {
        return StoredFile.builder()
            .fileName("test.txt")
            .contentType("text/plain")
            .size(29L)
            .created(LocalDateTime.now())
            .build();
    }

    public static StoredFile fileWithStorageKey() {
        return StoredFile.builder()
            .fileName("test.txt")
            .contentType("text/plain")
            .size(29L)
            .created(LocalDateTime.now())
            .storedResource(StoredResource.builder().storageKey("key68c8-2f28-4b53-ac5a-2db586512455").build())
            .build();
    }

    public static StoredFile storedFile() {
        return StoredFile.builder()
            .fileId("qqed68c8-2f28-4b53-ac5a-2db586512eee")
            .fileName("file")
            .contentType("text/plain")
            .size(123L)
            .created(LocalDateTime.now())
            .storedResource(StoredResource.builder().storageKey("eeed68c8-2f28-4b53-ac5a-2db586512e55").build())
            .build();
    }

    public static List<StoredFile> storedFiles() {
        return List.of(
            StoredFile.builder()
                .fileId("qqed68c8-2f28-4b53-ac5a-2db586512eee")
                .fileName("file1")
                .contentType("text/plain")
                .size(123L)
                .created(LocalDateTime.now())
                .storedResource(StoredResource.builder().storageKey("eeed68c8-2f28-4b53-ac5a-2db586512e55").build())
                .build(),
            StoredFile.builder()
                .fileId("qqed68c8-2f28-4b53-ac5a-2db586512ee1")
                .fileName("file2")
                .contentType("text/plain")
                .size(125L)
                .created(LocalDateTime.now())
                .storedResource(StoredResource.builder().storageKey("eeed68c8-2f28-4b53-ac5a-2db586512e56").build())
                .build(),
            StoredFile.builder()
                .fileId("qqed68c8-2f28-4b53-ac5a-2db586512ee2")
                .fileName("file3")
                .contentType("text/plain")
                .size(127L)
                .created(LocalDateTime.now())
                .storedResource(StoredResource.builder().storageKey("eeed68c8-2f28-4b53-ac5a-2db586512e57").build())
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

    public static MockMultipartFile getImportUsersExcel() throws IOException {
        return new MockMultipartFile(
            REQUEST_PARAM_NAME,
            IMPORT_USERS_ORIGINAL_FILE_NAME,
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            Files.readAllBytes(new File(IMPORT_USERS_FILE_PATH).toPath()));
    }
}
