package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

class DeleteFileRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/files/";
    private static final String CORRECT_FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512441";
    private static final String INCORRECT_FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512442";

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/file/file_setup.yml")
    @ExpectedDataSet("datasets/infrastructure/input/rest/file/file_empty.yml")
    void deleteFile_success() throws Exception {
        final var result = mvc.perform(
                delete(BASE_PATH + CORRECT_FILE_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/file/file_setup.yml")
    void deleteFile_notFound() throws Exception {
        final var result = mvc.perform(
                delete(BASE_PATH + INCORRECT_FILE_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void deleteFile_forbidden() throws Exception {
        final var result = mvc.perform(
                delete(BASE_PATH + CORRECT_FILE_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}