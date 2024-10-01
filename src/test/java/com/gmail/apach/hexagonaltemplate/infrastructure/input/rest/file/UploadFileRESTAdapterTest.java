package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file.dto.FileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

class UploadFileRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/files";

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/file/file_empty.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/input/rest/file/file_expected.yml",
        ignoreCols = {"file_", "storage_key", "file_size", "created"})
    void uploadFile_success() throws Exception {
        final var uploadedFile = FilesTestData.getUploadedFile();

        final var result = mvc.perform(
                multipart(BASE_PATH)
                    .file(uploadedFile))
            .andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

        final var content = result.getResponse().getContentAsString();

        assertNotNull(content);

        final var response = objectMapper.readValue(content, FileResponse.class);

        assertNotNull(response);
        assertNotNull(response.fileId());
        assertNotNull(response.storageKey());
        assertEquals(MediaType.TEXT_PLAIN_VALUE, response.contentType());
        assertEquals(uploadedFile.getOriginalFilename(), response.fileName());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void uploadFile_forbidden() throws Exception {
        final var uploadedFile = FilesTestData.getUploadedFile();

        final var result = mvc.perform(
                multipart(BASE_PATH)
                    .file(uploadedFile))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}