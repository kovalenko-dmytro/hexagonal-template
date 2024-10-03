package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractControllerIntegrationTest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.CurrentUserResponse;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.SignInRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.user.dto.SignInResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class AuthUserAdapterTest extends AbstractControllerIntegrationTest {

    private static final String BASE_PATH = "/api/v1/auth";
    private static final String SIGN_IN_URI = "/sign-in";
    private static final String CURRENT_USER_URI = "/current-user";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    @Test
    @DataSet("datasets/infrastructure/input/rest/user/auth_user_setup.yml")
    void signIn_success() throws Exception {
        final var request = SignInRequest.builder().username(USERNAME).password(PASSWORD).build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                post(BASE_PATH + SIGN_IN_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());

        final var content = result.getResponse().getContentAsString();

        assertNotNull(content);

        final var response = objectMapper.readValue(content, SignInResponse.class);

        assertNotNull(response);
        assertNotNull(response.accessToken());
        assertNotNull(response.tokenType());
    }

    @Test
    @DataSet("datasets/infrastructure/input/rest/user/auth_user_setup.yml")
    void signIn_fail() throws Exception {
        final var request = SignInRequest.builder().username(USERNAME).password("wrong").build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var result = mvc.perform(
                post(BASE_PATH + SIGN_IN_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), result.getResponse().getStatus());
    }

    @Test
    @WithMockUser(roles = { "ADMIN", "MANAGER", "USER" })
    @DataSet("datasets/infrastructure/input/rest/user/current_user_setup.yml")
    void getCurrentUser_success() throws Exception {
        final var request = SignInRequest.builder().username("current_user").password(PASSWORD).build();
        final var jsonRequest = objectMapper.writeValueAsString(request);
        final var signIn = mvc.perform(
                post(BASE_PATH + SIGN_IN_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonRequest))
            .andReturn();

        assertEquals(HttpStatus.OK.value(), signIn.getResponse().getStatus());
        final var signInContent = signIn.getResponse().getContentAsString();

        assertNotNull(signInContent);

        final var response = objectMapper.readValue(signInContent, SignInResponse.class);
        final var token = "Bearer " + response.accessToken();

        final var currentUser = mvc.perform(
                get(BASE_PATH + CURRENT_USER_URI)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", token))
            .andReturn();

        assertEquals(HttpStatus.OK.value(), currentUser.getResponse().getStatus());
        final var currentUserContent = currentUser.getResponse().getContentAsString();

        assertNotNull(currentUserContent);

        final var currentUserResponse = objectMapper.readValue(currentUserContent, CurrentUserResponse.class);

        assertNotNull(currentUserResponse);
        assertEquals(request.username(), currentUserResponse.username());
    }

    @Test
    @DataSet("datasets/infrastructure/input/rest/user/auth_user_setup.yml")
    void getCurrentUser_fail() throws Exception {
        final var currentUser = mvc.perform(
                get(BASE_PATH + CURRENT_USER_URI)
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        assertEquals(HttpStatus.UNAUTHORIZED.value(), currentUser.getResponse().getStatus());
    }
}