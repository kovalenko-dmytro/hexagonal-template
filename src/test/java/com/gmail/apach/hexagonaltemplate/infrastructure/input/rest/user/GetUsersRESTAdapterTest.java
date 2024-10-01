package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class GetUsersRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/users";

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/user/get_users_setup.yml")
    void getUsersByAdmin_success() throws Exception {
        final var result = mvc.perform(
                get(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        final var content = result.getResponse().getContentAsString();

        assertNotNull(content);

        final var resultFromCache = mvc.perform(
                get(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andReturn();

        assertEquals(HttpStatus.OK.value(), resultFromCache.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    @DataSet("datasets/infrastructure/input/rest/user/get_users_setup.yml")
    void getUsers_forbidden() throws Exception {
        final var result = mvc.perform(
                get(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}