package com.gmail.apach.hexagonaltemplate.infrastructure.common.auth;

import com.gmail.apach.hexagonaltemplate.application.usecase.user.GetUserUseCase;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
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
    private AuthService authService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JWTService jwtService;
    @Mock
    private GetUserUseCase getUserInputPort;

    @AfterEach
    public void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void signIn_success() {
        final var user = User.builder().username(USERNAME).password(PASSWORD).build();
        final var authentication = mock(Authentication.class);

        when(authentication.getName()).thenReturn(USERNAME);
        when(authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)))
            .thenReturn(authentication);
        when(getUserInputPort.getByUsername(authentication.getName())).thenReturn(user);
        when(jwtService.generateAccessToken(user)).thenReturn(ACCESS_TOKEN);
        when(jwtService.getAccessTokenExpirationTime()).thenReturn(1L);

        final var actual = authService.signIn(USERNAME, PASSWORD);

        assertNotNull(actual);
        assertEquals("Bearer", actual.tokenType());
        assertEquals(ACCESS_TOKEN, actual.accessToken());
        assertEquals(1L, actual.accessTokenExpired());
    }

    @Test
    void signIn_fail() {
        when(authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD)))
            .thenThrow(new BadCredentialsException(BAD_CREDENTIALS_MESSAGE));

        final var exception = assertThrows(
            BadCredentialsException.class, () -> authService.signIn(USERNAME, PASSWORD));

        assertTrue(exception.getMessage().contains(BAD_CREDENTIALS_MESSAGE));
    }

    @Test
    void getCurrentUser_success() {
        final var user = UsersTestData.admin();

        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn(user.getUsername());
        when(getUserInputPort.getByUsername(authentication.getName())).thenReturn(user);

        final var actual = authService.getCurrentUser();

        assertNotNull(actual);
        assertEquals(user.getFirstName(), actual.getFirstName());
        assertEquals(user.getLastName(), actual.getLastName());

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void getCurrentUser_fail() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn("wrong_username");
        when(getUserInputPort.getByUsername(authentication.getName()))
            .thenThrow(new ResourceNotFoundException(USER_NOT_FOUND_MESSAGE));

        assertThrows(ResourceNotFoundException.class, () -> authService.getCurrentUser());

        Mockito.reset(authentication, securityContext);
    }
}
