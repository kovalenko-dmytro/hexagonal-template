package com.gmail.apach.jenkins_demo.domain.user.validator;

import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.data.AuthoritiesTestData;
import com.gmail.apach.jenkins_demo.data.UsersTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserByIdPermissionsValidatorTest {

    @InjectMocks
    private GetUserByIdPermissionsValidator getUserByIdPermissionsValidator;
    @Mock
    private MessageSource messageSource;

    @Test
    void validateAdminGetAnyUser_success() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(AuthoritiesTestData.adminAuthorities());
        when(authentication.getName()).thenReturn("admin");

        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.admin()));
        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.manager()));
        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByAdmin()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerGetSelfOrOwnUser_success() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(AuthoritiesTestData.managerAuthorities());
        when(authentication.getName()).thenReturn("manager");

        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.manager()));
        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByManager()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateManagerNotGetSelfOrOwnUser_fail() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(AuthoritiesTestData.managerAuthorities());
        when(authentication.getName()).thenReturn("manager");

        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.admin()));
        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.anotherManager()));
        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByAnotherManager()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateUserGetSelf_success() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(AuthoritiesTestData.userAuthorities());
        when(authentication.getName()).thenReturn("userCreatedByAdmin");

        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByAdmin()));

        Mockito.reset(authentication, securityContext);
    }

    @Test
    void validateUserNotGetSelf_fail() {
        final var authentication = mock(AbstractAuthenticationToken.class);
        final var securityContext = mock(SecurityContext.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(AuthoritiesTestData.userAuthorities());
        when(authentication.getName()).thenReturn("userCreatedByAdmin");

        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.admin()));
        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.manager()));
        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.anotherManager()));
        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByAnotherManager()));

        Mockito.reset(authentication, securityContext);
    }
}