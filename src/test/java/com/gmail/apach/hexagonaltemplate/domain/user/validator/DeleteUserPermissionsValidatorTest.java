package com.gmail.apach.hexagonaltemplate.domain.user.validator;

import com.gmail.apach.hexagonaltemplate.common.dto.CurrentUserContext;
import com.gmail.apach.hexagonaltemplate.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.common.util.CurrentUserContextUtil;
import com.gmail.apach.hexagonaltemplate.data.AuthoritiesTestData;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteUserPermissionsValidatorTest {

    @InjectMocks
    private DeleteUserPermissionsValidator deleteUserPermissionsValidator;
    @Mock
    private CurrentUserContextUtil currentUserContextUtil;
    @Mock
    private MessageSource messageSource;

    @Test
    void validateAdminDeleteAnyOtherUsers_success() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() ->
            deleteUserPermissionsValidator.validate(UsersTestData.userCreatedByAdmin()));
        assertDoesNotThrow(() ->
            deleteUserPermissionsValidator.validate(UsersTestData.userCreatedByManager()));
        assertDoesNotThrow(() ->
            deleteUserPermissionsValidator.validate(UsersTestData.manager()));
    }

    @Test
    void validateAdminDeleteSelf_fail() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.admin()));
    }

    @Test
    void validateManagerDeleteAnyOtherUsers_fail() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.userCreatedByAdmin()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.userCreatedByManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.userCreatedByAnotherManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.manager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.anotherManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.admin()));
    }

    @Test
    void validateUserDeleteAnyOtherUsers_fail() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.userCreatedByAdmin()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.userCreatedByManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.userCreatedByAnotherManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.manager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.anotherManager()));
        assertThrows(ForbiddenException.class, () ->
            deleteUserPermissionsValidator.validate(UsersTestData.admin()));
    }
}