package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.CreateUserRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class CreateUserRESTAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/users";

    @Test
    @WithMockUser(roles = { "ADMIN", "MANAGER", "USER" })
    @DataSet("datasets/infrastructure/input/rest/user/create_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/input/rest/user/create_manager_expected.yml",
        ignoreCols = {"user_", "created", "password"})
    void createUser_success() throws Exception {
        final var request = CreateUserRequest.builder()
            .username("manager")
            .password("password")
            .email("email@email")
            .firstName("first_name")
            .lastName("last_name")
            .roleType(RoleType.MANAGER)
            .build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                post(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.CREATED.value(), result.getResponse().getStatus());

        final var content = result.getResponse().getContentAsString();

        assertNotNull(content);

        final var response = objectMapper.readValue(content, UserResponse.class);

        assertNotNull(response);
        assertNotNull(response.userId());
        assertEquals("manager", response.username());
        assertFalse(response.roles().isEmpty());
        assertTrue(response.roles().containsAll(Set.of(RoleType.MANAGER, RoleType.USER)));
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "MANAGER", "USER" })
    @DataSet("datasets/infrastructure/input/rest/user/create_user_setup.yml")
    void createUser_badRequest() throws Exception {
        final var request = CreateUserRequest.builder()
            .username("manager")
            .password("password")
            .email("email@email")
            .firstName("first_name")
            .lastName("last_name")
            .roleType(RoleType.ADMIN)
            .build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                post(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = { "MANAGER", "USER" })
    @DataSet("datasets/infrastructure/input/rest/user/create_user_setup.yml")
    void createUser_forbidden() throws Exception {
        final var request = CreateUserRequest.builder()
            .username("manager")
            .password("password")
            .email("email@email")
            .firstName("first_name")
            .lastName("last_name")
            .roleType(RoleType.MANAGER)
            .build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                post(BASE_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
    }
}