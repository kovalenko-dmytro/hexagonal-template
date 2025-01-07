package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.batch.dto.GetBatchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class GetBatchRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/batches/";
    private static final String CORRECT_BATCH_ID = "e64f50af-49c2-4e30-af47-f920a4d7887b";
    private static final String INCORRECT_BATCH_ID = "e64f50af-49c2-4e30-af47-f920a4d7887c";

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/batch/get_batch_setup.yml")
    void getBatch_success() throws Exception {
        final var result = mvc.perform(
                get(BASE_PATH + CORRECT_BATCH_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        final var content = result.getResponse().getContentAsString();

        assertNotNull(content);

        final var batchResponse = objectMapper.readValue(content, GetBatchResponse.class);

        assertNotNull(batchResponse);
        assertEquals(CORRECT_BATCH_ID, batchResponse.batchId());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/batch/get_batch_setup.yml")
    void getBatch_notFound() throws Exception {
        final var result = mvc.perform(
                get(BASE_PATH + INCORRECT_BATCH_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser()
    @DataSet("datasets/infrastructure/input/rest/batch/get_batch_setup.yml")
    void getBatch_forbidden() throws Exception {
        final var result = mvc.perform(
                get(BASE_PATH + CORRECT_BATCH_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}