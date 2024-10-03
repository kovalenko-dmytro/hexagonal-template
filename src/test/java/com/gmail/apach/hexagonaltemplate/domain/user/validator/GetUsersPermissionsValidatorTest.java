package com.gmail.apach.hexagonaltemplate.domain.user.validator;

import com.gmail.apach.hexagonaltemplate.data.AuthoritiesTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.CurrentUserAuthContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class GetUsersPermissionsValidatorTest {

    @InjectMocks
    private GetUsersPermissionsValidator getUsersPermissionsValidator;
    @Mock
    private MessageSource messageSource;

    @Test
    void validateAdminOrManagerGetUsers_success() {
        final var adminContext = CurrentUserAuthContext.builder()
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();
        final var managerContext = CurrentUserAuthContext.builder()
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        assertDoesNotThrow(() -> getUsersPermissionsValidator.validate(adminContext));

        assertDoesNotThrow(() -> getUsersPermissionsValidator.validate(managerContext));
    }

    @Test
    void validateUserGetUsers_forbidden() {
        final var context = CurrentUserAuthContext.builder()
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        assertThrows(ForbiddenException.class, () -> getUsersPermissionsValidator.validate(context));
    }
}