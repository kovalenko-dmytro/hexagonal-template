package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.common.constant.CommonConstant;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file.dto.FileResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

class DownloadFileRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/files";
    private static final String CORRECT_FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512441";
    private static final String INCORRECT_FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512442";

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    void downloadFile_success() throws Exception {
        final var uploadedFile = FilesTestData.getUploadedFile();
        ;

        final var uploadedResult = mvc.perform(
                multipart(BASE_PATH)
                    .file(uploadedFile))
            .andReturn();

        assertEquals(HttpStatus.CREATED.value(), uploadedResult.getResponse().getStatus());
        final var uploadedContent = uploadedResult.getResponse().getContentAsString();
        assertNotNull(uploadedContent);
        final var uploadedResponse = objectMapper.readValue(uploadedContent, FileResponse.class);

        final var result = mvc.perform(
                get(BASE_PATH + CommonConstant.SLASH.getValue() + uploadedResponse.fileId())
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertNotNull(result.getResponse().getHeader("Content-Disposition"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/file/file_setup.yml")
    void downloadFile_notFound() throws Exception {
        final var result = mvc.perform(
                get(BASE_PATH + CommonConstant.SLASH.getValue() + INCORRECT_FILE_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DataSet("datasets/infrastructure/input/rest/file/file_setup.yml")
    void downloadFile_forbidden() throws Exception {
        final var result = mvc.perform(
                get(BASE_PATH + CommonConstant.SLASH.getValue() + CORRECT_FILE_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}