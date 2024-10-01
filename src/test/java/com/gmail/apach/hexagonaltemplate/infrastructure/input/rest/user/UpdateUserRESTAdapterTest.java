package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.domain.user.model.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.UpdateUserRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

class UpdateUserRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/users/";
    private static final String CORRECT_USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";
    private static final String INCORRECT_USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512444";

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/user/update_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/input/rest/user/update_user_expected.yml",
        ignoreCols = {"user_", "created", "password"})
    void updateUser_success() throws Exception {
        final var request = UpdateUserRequest.builder()
            .email("new@new")
            .firstName("first_name_new")
            .lastName("last_name_new")
            .enabled(false)
            .build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                patch(BASE_PATH + CORRECT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        final var content = result.getResponse().getContentAsString();

        assertNotNull(content);

        final var userResponse = objectMapper.readValue(content, UserResponse.class);

        assertNotNull(userResponse);
        assertEquals(request.email(), userResponse.email());
        assertEquals(request.firstName(), userResponse.firstName());
        assertEquals(request.lastName(), userResponse.lastName());
        assertEquals(request.enabled(), userResponse.enabled());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/user/update_user_setup.yml")
    void updateUser_badRequest() throws Exception {
        final var request = UpdateUserRequest.builder()
            .email("new@new")
            .firstName("first_name_new")
            .lastName("last_name_new")
            .enabled(false)
            .roleType(RoleType.ADMIN)
            .build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                patch(BASE_PATH + CORRECT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/user/update_user_setup.yml")
    void updateUser_notFound() throws Exception {
        final var request = UpdateUserRequest.builder()
            .email("new@new")
            .firstName("first_name_new")
            .lastName("last_name_new")
            .enabled(false)
            .build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                patch(BASE_PATH + INCORRECT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = {"MANAGER", "USER"})
    @DataSet("datasets/infrastructure/input/rest/user/update_admin_setup.yml")
    void updateUser_forbidden() throws Exception {
        final var request = UpdateUserRequest.builder()
            .email("new@new")
            .firstName("first_name_new")
            .lastName("last_name_new")
            .enabled(false)
            .build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                patch(BASE_PATH + CORRECT_USER_ID)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}