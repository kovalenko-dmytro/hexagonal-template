package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.domain.common.constant.DomainError;
import com.gmail.apach.hexagonaltemplate.domain.common.exception.PolicyViolationException;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.*;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserPermissionPolicyService {

    public static void checkCreateUserPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .or(new ManagerCreatesUserPolicy());

        if (!policy.isSatisfiedWith(context)) {
            throw new PolicyViolationException(
                DomainError.FORBIDDEN_USER_CREATION, new Object[]{context.inputAttributes().rolesJoiningToString()});
        }
    }

    public static void checkDeleteUserPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .and(new AdminDeletesNotSelfPolicy());

        if (!policy.isSatisfiedWith(context)) {
            throw new PolicyViolationException(
                DomainError.FORBIDDEN_USER_DELETE_BY_ID, new Object[]{context.processed().getUserId()});
        }
    }

    public static void checkGetUserByIdPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .or(new ManagerGetsManagerPolicy())
                .or(new ManagerGetsUserPolicy())
                .or(new UserGetsSelfPolicy());

        if (!policy.isSatisfiedWith(context)) {
            throw new PolicyViolationException(
                DomainError.FORBIDDEN_USER_GET_BY_ID, new Object[]{context.processed().getUserId()});
        }
    }

    public static void checkUpdateUserPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .or(new ManagerUpdatesSelfPolicy())
                .or(new ManagerUpdatesUsersPolicy())
                .or(new UserUpdatesSelfPolicy());

        if (!policy.isSatisfiedWith(context)) {
            throw new PolicyViolationException(
                DomainError.FORBIDDEN_USER_UPDATE_BY_ID, new Object[]{context.processed().getUserId()});
        }

        final var enabledPassed = Objects.nonNull(context.inputAttributes().getEnabled());
        final var enabledNonMatch = !context.processed().getEnabled().equals(context.inputAttributes().getEnabled());
        if (enabledPassed && enabledNonMatch) {
            checkUpdateUserEnabledPolicy(context);
        }
        if (context.inputAttributes().rolesExist()) {
            checkUpdateUserRolesPolicy(context);
        }
    }

    private static void checkUpdateUserEnabledPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminUpdatesEnabledPolicy()
                .or(new ManagerUpdatesUserEnabledPolicy());

        if (!policy.isSatisfiedWith(context)) {
            throw new PolicyViolationException(
                DomainError.FORBIDDEN_USER_UPDATE_ENABLED, new Object[]{context.processed().getUserId()});
        }
    }

    private static void checkUpdateUserRolesPolicy(UserPermissionPolicyContext context) {
        final var policy = new AdminUpdatesRolesPolicy();

        if (!policy.isSatisfiedWith(context)) {
            throw new PolicyViolationException(
                DomainError.FORBIDDEN_USER_UPDATE_ROLES, new Object[]{context.processed().getUserId()});
        }
    }

    public static void checkGetUsersPolicy(UserPermissionPolicyContext context) {
        final var policy =
            new AdminGrantsPolicy()
                .or(new ManagerGrantsPolicy());

        if (!policy.isSatisfiedWith(context)) {
            throw new PolicyViolationException(
                DomainError.FORBIDDEN_USER_GET_LIST, new Object[]{RoleType.USER.getAuthority()});
        }
    }
}
