package com.gmail.apach.hexagonaltemplate.infrastructure.output.oss;

import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AwsS3StorageServiceAdapterTest extends AbstractIntegrationTest {

    private static final String FILE_PATH = "src/test/resources/file/test.txt";
    private static final String FILE_NAME = "file";
    private static final String ORIGINAL_FILE_NAME = "test.txt";
    private static final MockMultipartFile multipartFile;

    @Autowired
    private AwsS3StorageServiceAdapter awsS3Adapter;

    static {
        final var file = new File(FILE_PATH);
        try {
            multipartFile = new MockMultipartFile(FILE_NAME, ORIGINAL_FILE_NAME,
                MediaType.TEXT_PLAIN_VALUE,
                Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void save_success() {
        final var actual = awsS3Adapter.save(multipartFile);

        assertNotNull(actual);
        assertNotNull(actual.getLocation().getObject());

        awsS3Adapter.delete(actual.getLocation().getObject());
    }

    @Test
    void get_success() {
        final var saved = awsS3Adapter.save(multipartFile);

        assertNotNull(saved);

        final var actual = awsS3Adapter.get(saved.getLocation().getObject());

        assertNotNull(actual);
        assertEquals(saved.getLocation().getObject(), actual.getLocation().getObject());

        awsS3Adapter.delete(actual.getLocation().getObject());
    }

    @Test
    void delete_success() {
        final var saved = awsS3Adapter.save(multipartFile);

        assertNotNull(saved);

        awsS3Adapter.delete(saved.getLocation().getObject());
    }
}