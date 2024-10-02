package com.gmail.apach.hexagonaltemplate.domain.user.validator;

import com.gmail.apach.hexagonaltemplate.data.AuthoritiesTestData;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
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
class UpdateUserPermissionsValidatorTest {

    @InjectMocks
    private UpdateUserPermissionsValidator updateUserPermissionsValidator;
    @Mock
    private MessageSource messageSource;
    @Mock
    private CurrentUserContextUtil currentUserContextUtil;

    @Test
    void validateAdminUpdateAnyUser_success() {
        final var context = CurrentUserContext.builder()
            .username("admin")
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() ->
            updateUserPermissionsValidator.validate(
                UsersTestData.admin(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertDoesNotThrow(() ->
            updateUserPermissionsValidator.validate(
                UsersTestData.manager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertDoesNotThrow(() ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByAnotherManager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));
    }

    @Test
    void validateAdminUpdateOwnRolesAndEnabled_fail() {
        final var context = CurrentUserContext.builder()
            .username("admin")
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewRoles()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewEnabled()));
    }

    @Test
    void validateManagerUpdateSelfOrAnyUser_success() {
        final var context = CurrentUserContext.builder()
            .username("manager")
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() ->
            updateUserPermissionsValidator.validate(
                UsersTestData.manager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertDoesNotThrow(() ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByManager(), UsersTestData.updateUserDataWithNewEnabled()));

        assertDoesNotThrow(() ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByAnotherManager(), UsersTestData.updateUserDataWithNewEnabled()));
    }

    @Test
    void validateManagerUpdateAnotherManagerOrAdmin_fail() {
        final var context = CurrentUserContext.builder()
            .username("manager")
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.admin(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));
    }

    @Test
    void validateManagerUpdateEnabledToSelfOrAnotherManagerOrAdmin_fail() {
        final var context = CurrentUserContext.builder()
            .username("manager")
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.manager(), UsersTestData.updateUserDataWithNewEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithNewEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewEnabled()));
    }

    @Test
    void validateManagerUpdateRoles_fail() {
        final var context = CurrentUserContext.builder()
            .username("manager")
            .authorities(AuthoritiesTestData.managerAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.manager(), UsersTestData.updateUserDataWithNewRoles()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithNewRoles()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewRoles()));
    }

    @Test
    void validateUserUpdateSelf_success() {
        final var context = CurrentUserContext.builder()
            .username("userCreatedByAdmin")
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertDoesNotThrow(() ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByAdmin(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));
    }

    @Test
    void validateUserUpdateNotSelf_fail() {
        final var context = CurrentUserContext.builder()
            .username("userCreatedByAdmin")
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByManager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByAnotherManager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.manager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.admin(), UsersTestData.updateUserDataWithoutNewRolesAndEnabled()));
    }

    @Test
    void validateUserUpdateEnabledOrRoles_fail() {
        final var context = CurrentUserContext.builder()
            .username("userCreatedByAdmin")
            .authorities(AuthoritiesTestData.userAuthorities())
            .build();

        when(currentUserContextUtil.getContext()).thenReturn(context);

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByAdmin(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByManager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.userCreatedByAnotherManager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.manager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.anotherManager(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));

        assertThrows(ForbiddenException.class, () ->
            updateUserPermissionsValidator.validate(
                UsersTestData.admin(), UsersTestData.updateUserDataWithNewRolesAndEnabled()));
    }
}