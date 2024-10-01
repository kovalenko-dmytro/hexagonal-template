package com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.service;

import com.gmail.apach.hexagonaltemplate.application.input.user.GetUserInputPort;
import com.gmail.apach.hexagonaltemplate.common.dto.CurrentUserContext;
import com.gmail.apach.hexagonaltemplate.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.common.util.CurrentUserContextUtil;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.common.mapper.UserRESTMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.dto.CurrentUserResponse;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.dto.SignInRequest;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.rest.security.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String BAD_CREDENTIALS_MESSAGE = "bad credentials";
    private static final String USER_NOT_FOUND_MESSAGE = "No User entity with username username exists!";
    private static final String USERNAME = "John";
    private static final String PASSWORD = "password";
    private static final String ACCESS_TOKEN = "token";

    @InjectMocks
    private AuthServiceImpl authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private CurrentUserContextUtil currentUserContextUtil;
    @Mock
    private JWTService jwtService;
    @Mock
    private GetUserInputPort getUserInputPort;
    @Mock
    private UserRESTMapper userMapper;

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void signIn_success() {
        final var request = new SignInRequest(USERNAME, PASSWORD);
        final var user = User.builder().username(USERNAME).password(PASSWORD).build();
        final var authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(USERNAME);
        when(authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())))
            .thenReturn(authentication);
        when(getUserInputPort.getByUsername(authentication.getName())).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn(ACCESS_TOKEN);
        when(jwtService.getAccessTokenExpirationTime()).thenReturn(1L);

        final var actual = authService.signIn(request);

        assertNotNull(actual);
        assertEquals("Bearer", actual.tokenType());
        assertEquals(ACCESS_TOKEN, actual.accessToken());
        assertEquals(1L, actual.accessTokenExpired());
    }

    @Test
    void signIn_fail() {
        final var request = new SignInRequest(USERNAME, PASSWORD);

        when(authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.username(), request.password())))
            .thenThrow(new BadCredentialsException(BAD_CREDENTIALS_MESSAGE));

        final var exception = assertThrows(
            BadCredentialsException.class, () -> authService.signIn(request));


        assertTrue(exception.getMessage().contains(BAD_CREDENTIALS_MESSAGE));
    }

    @Test
    void getCurrentUser_success() {
        final var user = User.builder().firstName(USERNAME).lastName("Foo").build();
        final var response = CurrentUserResponse.builder()
            .firstName(USERNAME)
            .lastName("Foo")
            .build();
        final var context = CurrentUserContext.builder()
            .username(USERNAME)
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);
        when(getUserInputPort.getByUsername(context.username())).thenReturn(user);
        when(userMapper.toCurrentUserResponse(user)).thenReturn(response);

        final var actual = authService.getCurrentUser();

        assertNotNull(actual);
        assertEquals(user.getFirstName(), actual.firstName());
        assertEquals(user.getLastName(), actual.lastName());
    }

    @Test
    void getCurrentUser_fail() {
        final var context = CurrentUserContext.builder()
            .username(null)
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);
        when(getUserInputPort.getByUsername(context.username()))
            .thenThrow(new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE));

        assertThrows(ResourceNotFoundException.class, () -> authService.getCurrentUser());
    }
}
