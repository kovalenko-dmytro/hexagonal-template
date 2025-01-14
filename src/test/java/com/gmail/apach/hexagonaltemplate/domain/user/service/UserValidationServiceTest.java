package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.PolicyViolationException;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserValidationContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {

    @Mock
    private MessageSource messageSource;

    @Test
    void validateAdminCreatesUserOrManager_success() {
        final var createManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.manager())
            .principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkCreateUserPolicy(createManagerContext));

        final var createUserContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.userCreatedByAdmin()).principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkCreateUserPolicy(createUserContext));
    }

    @Test
    void validateManagerCreatesUser_success() {
        final var createUserContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.userCreatedByManager()).principal(UsersTestData.manager())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkCreateUserPolicy(createUserContext));
    }

    @Test
    void validateManagerCreatesManager_forbidden() {
        final var createManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.manager()).principal(UsersTestData.anotherManager())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkCreateUserPolicy(createManagerContext));
    }

    @Test
    void validateUserCreatesAny_forbidden() {
        final var createAdminContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.admin()).principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var createManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.manager()).principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var createUserContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.userCreatedByManager()).principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkCreateUserPolicy(createAdminContext));
        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkCreateUserPolicy(createManagerContext));
        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkCreateUserPolicy(createUserContext));
    }

    @Test
    void validateAdminDeleteAnyOtherUsers_success() {
        final var deleteManagerContext = UserValidationContext.builder()
            .processed(UsersTestData.manager())
            .principal(UsersTestData.admin())
            .build();
        final var deleteUserContext = UserValidationContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkDeleteUserPolicy(deleteManagerContext));
        assertDoesNotThrow(() -> UserValidationService.checkDeleteUserPolicy(deleteUserContext));
    }

    @Test
    void validateAdminDeleteSelf_forbidden() {
        final var deleteAdminContext = UserValidationContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.admin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkDeleteUserPolicy(deleteAdminContext));
    }

    @Test
    void validateManagerDeleteAnyOtherUsers_forbidden() {
        final var deleteAdminContext = UserValidationContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();
        final var deleteManagerContext = UserValidationContext.builder()
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();
        final var deleteUserContext = UserValidationContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkDeleteUserPolicy(deleteAdminContext));
        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkDeleteUserPolicy(deleteManagerContext));
        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkDeleteUserPolicy(deleteUserContext));
    }

    @Test
    void validateUserDeleteAnyOtherUsers_forbidden() {
        final var deleteAdminContext = UserValidationContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var deleteManagerContext = UserValidationContext.builder()
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var deleteUserContext = UserValidationContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkDeleteUserPolicy(deleteAdminContext));
        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkDeleteUserPolicy(deleteManagerContext));
        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkDeleteUserPolicy(deleteUserContext));
    }

    @Test
    void validateAdminGetAnyUser_success() {
        final var getAdminContext = UserValidationContext.builder()
            .processed(UsersTestData.admin()).principal(UsersTestData.admin())
            .build();
        final var getManagerContext = UserValidationContext.builder()
            .processed(UsersTestData.manager()).principal(UsersTestData.admin())
            .build();
        final var getUserContext = UserValidationContext.builder()
            .processed(UsersTestData.userCreatedByManager()).principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkGetUserByIdPolicy(getAdminContext));
        assertDoesNotThrow(() -> UserValidationService.checkGetUserByIdPolicy(getManagerContext));
        assertDoesNotThrow(() -> UserValidationService.checkGetUserByIdPolicy(getUserContext));
    }

    @Test
    void validateManagerGetAnyExceptAdmin_success() {
        final var getManagerContext = UserValidationContext.builder()
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();
        final var getUserContext = UserValidationContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.manager())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkGetUserByIdPolicy(getManagerContext));
        assertDoesNotThrow(() -> UserValidationService.checkGetUserByIdPolicy(getUserContext));
    }

    @Test
    void validateManagerGetAdmin_forbidden() {
        final var getAdminContext = UserValidationContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkGetUserByIdPolicy(getAdminContext));
    }

    @Test
    void validateUserGetSelf_success() {
        final var getUserContext = UserValidationContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByManager())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkGetUserByIdPolicy(getUserContext));
    }

    @Test
    void validateUserGetNotSelf_forbidden() {
        final var getAdminContext = UserValidationContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var getManagerContext = UserValidationContext.builder()
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var getUserContext = UserValidationContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkGetUserByIdPolicy(getAdminContext));
        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkGetUserByIdPolicy(getManagerContext));
        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkGetUserByIdPolicy(getUserContext));
    }

    @Test
    void validateAdminGetUsers_success() {
        final var getUsersContext = UserValidationContext.builder()
            .principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkGetUsersPolicy(getUsersContext));
    }

    @Test
    void validateManagerGetUsers_success() {
        final var getUsersContext = UserValidationContext.builder()
            .principal(UsersTestData.manager())
            .build();

        assertDoesNotThrow(() -> UserValidationService.checkGetUsersPolicy(getUsersContext));
    }

    @Test
    void validateUserGetUsers_forbidden() {
        final var getUsersContext = UserValidationContext.builder()
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserValidationService.checkGetUsersPolicy(getUsersContext));
    }

    @Test
    void validateAdminUpdateAnyUser_success() {
        final var updateAdminContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.admin())
            .build();
        final var updateManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.admin())
            .build();
        final var updateUserContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.userCreatedByAdmin())
            .principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() ->
            UserValidationService.checkUpdateUserPolicy(updateAdminContext));

        assertDoesNotThrow(() ->
            UserValidationService.checkUpdateUserPolicy(updateManagerContext));

        assertDoesNotThrow(() ->
            UserValidationService.checkUpdateUserPolicy(updateUserContext));
    }

    @Test
    void validateAdminUpdateOwnRolesAndEnabled_forbidden() {
        final var updateAdminEnabledContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.admin())
            .build();
        final var updateAdminRolesContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.admin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateAdminEnabledContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateAdminRolesContext));
    }

    @Test
    void validateManagerUpdateSelfOrAnyUser_success() {
        final var updateManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.manager())
            .build();
        final var updateUserContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.userCreatedByAdmin())
            .principal(UsersTestData.manager())
            .build();

        assertDoesNotThrow(() ->
            UserValidationService.checkUpdateUserPolicy(updateManagerContext));

        assertDoesNotThrow(() ->
            UserValidationService.checkUpdateUserPolicy(updateUserContext));
    }

    @Test
    void validateManagerUpdateAnotherManagerOrAdmin_forbidden() {
        final var updateAdminContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();
        final var updateManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateAdminContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateManagerContext));
    }

    @Test
    void validateManagerUpdateEnabledToSelfOrAnotherManagerOrAdmin_forbidden() {
        final var updateAdminContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();
        final var updateSelfContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.manager())
            .build();
        final var updateManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateAdminContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateSelfContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateManagerContext));
    }

    @Test
    void validateManagerUpdateRoles_forbidden() {
        final var updateAdminContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();
        final var updateManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();
        final var updateUserContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.userCreatedByAdmin())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateAdminContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateManagerContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateUserContext));
    }

    @Test
    void validateUserUpdateSelf_success() {
        final var updateUserSelfContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.userCreatedByAdmin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertDoesNotThrow(() ->
            UserValidationService.checkUpdateUserPolicy(updateUserSelfContext));
    }

    @Test
    void validateUserUpdateNotSelf_forbidden() {
        final var updateAdminContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateManagerContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateUserContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateAdminContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateManagerContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateUserContext));
    }

    @Test
    void validateUserUpdateEnabledOrRoles_forbidden() {
        final var updateAdminEnabledContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateManagerEnabledContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateUserEnabledContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateAdminRolesContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateManagerRolesContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateUserRolesContext = UserValidationContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateAdminEnabledContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateManagerEnabledContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateUserEnabledContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateAdminRolesContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateManagerRolesContext));

        assertThrows(PolicyViolationException.class, () ->
            UserValidationService.checkUpdateUserPolicy(updateUserRolesContext));
    }
}