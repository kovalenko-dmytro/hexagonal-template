package com.gmail.apach.jenkins_demo.infrastructure.input.rest.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.jenkins_demo.AbstractControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

class DeleteUserRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/users/";
    private static final String CORRECT_USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";
    private static final String INCORRECT_USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512444";

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/user/delete_user_setup.yml")
    @ExpectedDataSet("datasets/infrastructure/input/rest/user/delete_user_expected.yml")
    void deleteByUserId_success() throws Exception {
        final var result = mvc.perform(
                delete(BASE_PATH + CORRECT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.NO_CONTENT.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/user/delete_user_setup.yml")
    void deleteByUserId_notFound() throws Exception {
        final var result = mvc.perform(
                delete(BASE_PATH + INCORRECT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/user/delete_user_setup.yml")
    void deleteByUserId_forbidden() throws Exception {
        final var result = mvc.perform(
                delete(BASE_PATH + CORRECT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}