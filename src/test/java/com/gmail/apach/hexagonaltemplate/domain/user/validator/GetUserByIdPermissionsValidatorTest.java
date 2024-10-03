package com.gmail.apach.hexagonaltemplate.domain.user.validator;

import com.gmail.apach.hexagonaltemplate.data.AuthoritiesTestData;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.util.CurrentUserContextUtil;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.CurrentUserAuthContext;
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
class GetUserByIdPermissionsValidatorTest {

    @InjectMocks
    private GetUserByIdPermissionsValidator getUserByIdPermissionsValidator;
    @Mock
    private MessageSource messageSource;
    @Mock
    private CurrentUserContextUtil currentUserContextUtil;

    @Test
    void validateAdminGetAnyUser_success() {
        final var context = CurrentUserAuthContext.builder()
            .username("admin")
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.admin()));
        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.manager()));
        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByAdmin()));
    }

    @Test
    void validateManagerGetAnyExceptAdmin_success() {
        final var context = CurrentUserAuthContext.builder()
            .username("manager")
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.manager()));
        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.anotherManager()));
        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByManager()));
        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByAnotherManager()));
    }

    @Test
    void validateManagerNotGetAdmin_fail() {
        final var context = CurrentUserAuthContext.builder()
            .username("manager")
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.admin()));
    }

    @Test
    void validateUserGetSelf_success() {
        final var context = CurrentUserAuthContext.builder()
            .username("userCreatedByAdmin")
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByAdmin()));
    }

    @Test
    void validateUserNotGetSelf_fail() {
        final var context = CurrentUserAuthContext.builder()
            .username("userCreatedByAdmin")
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.admin()));
        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.manager()));
        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.anotherManager()));
        assertThrows(ForbiddenException.class,
            () -> getUserByIdPermissionsValidator.validate(UsersTestData.userCreatedByAnotherManager()));
    }
}