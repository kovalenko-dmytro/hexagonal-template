package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.PolicyViolationException;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserPermissionPolicyServiceTest {

    @Mock
    private MessageSource messageSource;

    @Test
    void validateAdminCreatesUserOrManager_success() {
        final var createManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.manager())
            .principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkCreateUserPolicy(createManagerContext));

        final var createUserContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.userCreatedByAdmin()).principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkCreateUserPolicy(createUserContext));
    }

    @Test
    void validateManagerCreatesUser_success() {
        final var createUserContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.userCreatedByManager()).principal(UsersTestData.manager())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkCreateUserPolicy(createUserContext));
    }

    @Test
    void validateManagerCreatesManager_forbidden() {
        final var createManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.manager()).principal(UsersTestData.anotherManager())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkCreateUserPolicy(createManagerContext));
    }

    @Test
    void validateUserCreatesAny_forbidden() {
        final var createAdminContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.admin()).principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var createManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.manager()).principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var createUserContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.userCreatedByManager()).principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkCreateUserPolicy(createAdminContext));
        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkCreateUserPolicy(createManagerContext));
        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkCreateUserPolicy(createUserContext));
    }

    @Test
    void validateAdminDeleteAnyOtherUsers_success() {
        final var deleteManagerContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.manager())
            .principal(UsersTestData.admin())
            .build();
        final var deleteUserContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkDeleteUserPolicy(deleteManagerContext));
        assertDoesNotThrow(() -> UserPermissionPolicyService.checkDeleteUserPolicy(deleteUserContext));
    }

    @Test
    void validateAdminDeleteSelf_forbidden() {
        final var deleteAdminContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.admin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkDeleteUserPolicy(deleteAdminContext));
    }

    @Test
    void validateManagerDeleteAnyOtherUsers_forbidden() {
        final var deleteAdminContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();
        final var deleteManagerContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();
        final var deleteUserContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkDeleteUserPolicy(deleteAdminContext));
        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkDeleteUserPolicy(deleteManagerContext));
        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkDeleteUserPolicy(deleteUserContext));
    }

    @Test
    void validateUserDeleteAnyOtherUsers_forbidden() {
        final var deleteAdminContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var deleteManagerContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var deleteUserContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkDeleteUserPolicy(deleteAdminContext));
        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkDeleteUserPolicy(deleteManagerContext));
        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkDeleteUserPolicy(deleteUserContext));
    }

    @Test
    void validateAdminGetAnyUser_success() {
        final var getAdminContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.admin()).principal(UsersTestData.admin())
            .build();
        final var getManagerContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.manager()).principal(UsersTestData.admin())
            .build();
        final var getUserContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.userCreatedByManager()).principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkGetUserByIdPolicy(getAdminContext));
        assertDoesNotThrow(() -> UserPermissionPolicyService.checkGetUserByIdPolicy(getManagerContext));
        assertDoesNotThrow(() -> UserPermissionPolicyService.checkGetUserByIdPolicy(getUserContext));
    }

    @Test
    void validateManagerGetAnyExceptAdmin_success() {
        final var getManagerContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();
        final var getUserContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.manager())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkGetUserByIdPolicy(getManagerContext));
        assertDoesNotThrow(() -> UserPermissionPolicyService.checkGetUserByIdPolicy(getUserContext));
    }

    @Test
    void validateManagerGetAdmin_forbidden() {
        final var getAdminContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkGetUserByIdPolicy(getAdminContext));
    }

    @Test
    void validateUserGetSelf_success() {
        final var getUserContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByManager())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkGetUserByIdPolicy(getUserContext));
    }

    @Test
    void validateUserGetNotSelf_forbidden() {
        final var getAdminContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var getManagerContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var getUserContext = UserPermissionPolicyContext.builder()
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkGetUserByIdPolicy(getAdminContext));
        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkGetUserByIdPolicy(getManagerContext));
        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkGetUserByIdPolicy(getUserContext));
    }

    @Test
    void validateAdminGetUsers_success() {
        final var getUsersContext = UserPermissionPolicyContext.builder()
            .principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkGetUsersPolicy(getUsersContext));
    }

    @Test
    void validateManagerGetUsers_success() {
        final var getUsersContext = UserPermissionPolicyContext.builder()
            .principal(UsersTestData.manager())
            .build();

        assertDoesNotThrow(() -> UserPermissionPolicyService.checkGetUsersPolicy(getUsersContext));
    }

    @Test
    void validateUserGetUsers_forbidden() {
        final var getUsersContext = UserPermissionPolicyContext.builder()
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class,
            () -> UserPermissionPolicyService.checkGetUsersPolicy(getUsersContext));
    }

    @Test
    void validateAdminUpdateAnyUser_success() {
        final var updateAdminContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.admin())
            .build();
        final var updateManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.admin())
            .build();
        final var updateUserContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.userCreatedByAdmin())
            .principal(UsersTestData.admin())
            .build();

        assertDoesNotThrow(() ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminContext));

        assertDoesNotThrow(() ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateManagerContext));

        assertDoesNotThrow(() ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateUserContext));
    }

    @Test
    void validateAdminUpdateOwnRolesAndEnabled_forbidden() {
        final var updateAdminEnabledContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.admin())
            .build();
        final var updateAdminRolesContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.admin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminEnabledContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminRolesContext));
    }

    @Test
    void validateManagerUpdateSelfOrAnyUser_success() {
        final var updateManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.manager())
            .build();
        final var updateUserContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.userCreatedByAdmin())
            .principal(UsersTestData.manager())
            .build();

        assertDoesNotThrow(() ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateManagerContext));

        assertDoesNotThrow(() ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateUserContext));
    }

    @Test
    void validateManagerUpdateAnotherManagerOrAdmin_forbidden() {
        final var updateAdminContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();
        final var updateManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateManagerContext));
    }

    @Test
    void validateManagerUpdateEnabledToSelfOrAnotherManagerOrAdmin_forbidden() {
        final var updateAdminContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();
        final var updateSelfContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.manager())
            .build();
        final var updateManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateSelfContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateManagerContext));
    }

    @Test
    void validateManagerUpdateRoles_forbidden() {
        final var updateAdminContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.manager())
            .build();
        final var updateManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.anotherManager())
            .principal(UsersTestData.manager())
            .build();
        final var updateUserContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.userCreatedByAdmin())
            .principal(UsersTestData.manager())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateManagerContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateUserContext));
    }

    @Test
    void validateUserUpdateSelf_success() {
        final var updateUserSelfContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.userCreatedByAdmin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertDoesNotThrow(() ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateUserSelfContext));
    }

    @Test
    void validateUserUpdateNotSelf_forbidden() {
        final var updateAdminContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateManagerContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateUserContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithoutNewRolesAndEnabled())
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateManagerContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateUserContext));
    }

    @Test
    void validateUserUpdateEnabledOrRoles_forbidden() {
        final var updateAdminEnabledContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateManagerEnabledContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateUserEnabledContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewEnabled())
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateAdminRolesContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.admin())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateManagerRolesContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.manager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();
        final var updateUserRolesContext = UserPermissionPolicyContext.builder()
            .inputAttributes(UsersTestData.updateUserDataWithNewRoles())
            .processed(UsersTestData.userCreatedByManager())
            .principal(UsersTestData.userCreatedByAdmin())
            .build();

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminEnabledContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateManagerEnabledContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateUserEnabledContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateAdminRolesContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateManagerRolesContext));

        assertThrows(PolicyViolationException.class, () ->
            UserPermissionPolicyService.checkUpdateUserPolicy(updateUserRolesContext));
    }
}