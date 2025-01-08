package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto.ImportUsersRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file.dto.FileResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class ImportUsersRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/batches/import-users-from-file";
    private static final String UPLOAD_FILE_BASE_PATH = "/api/v1/files";
    private static final String REDIRECT_URI_BASE_PATH = "/api/v1/batches/";

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void importUsers_success() throws Exception {
        final var uploadedFile = FilesTestData.getImportUsersExcel();

        final var uploadedResult = mvc.perform(
                multipart(UPLOAD_FILE_BASE_PATH)
                    .file(uploadedFile))
            .andReturn();

        assertEquals(HttpStatus.CREATED.value(), uploadedResult.getResponse().getStatus());

        final var uploadedContent = uploadedResult.getResponse().getContentAsString();

        assertNotNull(uploadedContent);

        final var uploadedResponse = objectMapper.readValue(uploadedContent, FileResponse.class);

        assertNotNull(uploadedResponse);
        assertNotNull(uploadedResponse.fileId());

        final var jsonRequest = objectMapper.writeValueAsString(
            ImportUsersRequest.builder().fileId(uploadedResponse.fileId()).build());

        final var result = mvc.perform(
                post(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());
        final var locationHeader = result.getResponse().getHeader(HttpHeaders.LOCATION);
        assertNotNull(locationHeader);
        assertTrue(locationHeader.contains(REDIRECT_URI_BASE_PATH));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "MANAGER", "USER"})
    void importUsers_badRequest() throws Exception {
        final var jsonRequest = objectMapper.writeValueAsString(
            ImportUsersRequest.builder().fileId(StringUtils.EMPTY).build());

        final var result = mvc.perform(
                post(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser()
    void importUsers_forbidden() throws Exception {
        final var jsonRequest = objectMapper.writeValueAsString(
            ImportUsersRequest.builder().fileId("some-file-id").build());

        final var result = mvc.perform(
                post(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}