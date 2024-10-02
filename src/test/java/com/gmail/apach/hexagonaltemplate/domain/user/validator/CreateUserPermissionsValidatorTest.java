package com.gmail.apach.hexagonaltemplate.domain.user.validator;

import com.gmail.apach.hexagonaltemplate.data.AuthoritiesTestData;
import com.gmail.apach.hexagonaltemplate.data.RolesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.util.CurrentUserContextUtil;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.CurrentUserContext;
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
class CreateUserPermissionsValidatorTest {

    @InjectMocks
    private CreateUserPermissionsValidator createUserPermissionsValidator;
    @Mock
    private MessageSource messageSource;
    @Mock
    private CurrentUserContextUtil currentUserContextUtil;

    @Test
    void validateAdminCreatesUserOrManager_success() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() -> createUserPermissionsValidator.validate(RolesTestData.userRoles()));
        assertDoesNotThrow(() -> createUserPermissionsValidator.validate(RolesTestData.managerRoles()));
    }

    @Test
    void validateManagerCreatesUser_success() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() -> createUserPermissionsValidator.validate(RolesTestData.userRoles()));
    }

    @Test
    void validateManagerCreatesManager_fail() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class,
            () -> createUserPermissionsValidator.validate(RolesTestData.managerRoles()));
    }

    @Test
    void validateUserCreatesAny_fail() {
        final var context = CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class,
            () -> createUserPermissionsValidator.validate(RolesTestData.userRoles()));
        assertThrows(ForbiddenException.class,
            () -> createUserPermissionsValidator.validate(RolesTestData.managerRoles()));
        assertThrows(ForbiddenException.class,
            () -> createUserPermissionsValidator.validate(RolesTestData.adminRoles()));
    }
}