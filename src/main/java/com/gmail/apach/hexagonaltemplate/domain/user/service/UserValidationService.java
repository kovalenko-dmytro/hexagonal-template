package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.api.*;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserValidationService {

    public static void checkCreateUserPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .or(new ManagerCreatesUserPolicy())
                .withErrorDetails(
                    DomainError.FORBIDDEN_USER_CREATION,
                    new Object[]{context.inputAttributes().rolesJoiningToString()})
                .terminateIfError();

        policy.isSatisfiedWith(context);
    }

    public static void checkDeleteUserPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .and(new AdminDeletesNotSelfPolicy())
                .withErrorDetails(
                    DomainError.FORBIDDEN_USER_DELETE_BY_ID,
                    new Object[]{context.processed().getUserId()})
                .terminateIfError();

        policy.isSatisfiedWith(context);
    }

    public static void checkGetUserByIdPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .or(new ManagerGetsManagerPolicy())
                .or(new ManagerGetsUserPolicy())
                .or(new UserGetsSelfPolicy())
                .withErrorDetails(
                    DomainError.FORBIDDEN_USER_GET_BY_ID,
                    new Object[]{context.processed().getUserId()})
                .terminateIfError();

        policy.isSatisfiedWith(context);
    }

    public static void checkUpdateUserPolicy(UserPermissionPolicyContext context) {
        var policy =
            new AdminGrantsPolicy()
                .or(new ManagerUpdatesSelfPolicy())
                .or(new ManagerUpdatesUsersPolicy())
                .or(new UserUpdatesSelfPolicy())
                .withErrorDetails(
                    DomainError.FORBIDDEN_USER_UPDATE_BY_ID,
                    new Object[]{context.processed().getUserId()})
                .terminateIfError();

        policy.isSatisfiedWith(context);

        final var enabledPassed = Objects.nonNull(context.inputAttributes().getEnabled());
        final var enabledNonMatch = !context.processed().getEnabled().equals(context.inputAttributes().getEnabled());
        if (enabledPassed && enabledNonMatch) {
            policy = new AdminUpdatesEnabledPolicy()

                .or(new ManagerUpdatesUserEnabledPolicy())
                .withErrorDetails(
                    DomainError.FORBIDDEN_USER_UPDATE_ENABLED,
                    new Object[]{context.processed().getUserId()})
                .terminateIfError();

            policy.isSatisfiedWith(context);
        }
        if (context.inputAttributes().rolesExist()) {
            policy = new AdminUpdatesRolesPolicy()
                .withErrorDetails(
                    DomainError.FORBIDDEN_USER_UPDATE_ROLES,
                    new Object[]{context.processed().getUserId()})
                .terminateIfError();

            policy.isSatisfiedWith(context);
        }
    }

    public static void checkGetUsersPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .or(new ManagerGrantsPolicy())
                .withErrorDetails(
                    DomainError.FORBIDDEN_USER_GET_LIST,
                    new Object[]{RoleType.USER.getAuthority()})
                .terminateIfError();

        policy.isSatisfiedWith(context);
    }
}
