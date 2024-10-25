package com.gmail.apach.hexagonaltemplate.domain.user.policy;

import com.gmail.apach.hexagonaltemplate.domain.user.model.AuthPrincipal;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UpdateUserPermissionPolicy {

    private final MessageSource messageSource;

    public void check(User requestedUser, User updatedData) {
        final var authPrincipal = AuthPrincipal.getDetails();
        validateUpdatePermissions(requestedUser, authPrincipal);
        validateRequestedChanges(requestedUser, updatedData, authPrincipal);
    }

    private void validateUpdatePermissions(User requestedUser, AuthPrincipal authPrincipal) {
        final var isAdmin = authPrincipal.isAdmin();
        final var managerTriesToUpdateSelfOrAnyUsers =
            authPrincipal.isManager() && (requestedUser.isSelf(authPrincipal) || requestedUser.isUser());
        final var userTriesToUpdateSelf = authPrincipal.isUser() && requestedUser.isSelf(authPrincipal);

        if (!(isAdmin || managerTriesToUpdateSelfOrAnyUsers || userTriesToUpdateSelf)) {
            throw new ForbiddenException(
                messageSource.getMessage(
                    Error.FORBIDDEN_USER_UPDATE_BY_ID.getKey(),
                    getArgs(requestedUser),
                    LocaleContextHolder.getLocale()));
        }
    }

    private void validateRequestedChanges(User requestedUser, User updatedData, AuthPrincipal authPrincipal) {
        if (Objects.nonNull(updatedData.getEnabled()) && !requestedUser.getEnabled().equals(updatedData.getEnabled())) {
            final var isAdmin = authPrincipal.isAdmin() && !requestedUser.isAdmin();
            final var managerTriesToUpdateEnabledToAnyUsers = authPrincipal.isManager() && requestedUser.isUser();
            if (!(isAdmin || managerTriesToUpdateEnabledToAnyUsers)) {
                throw new ForbiddenException(
                    messageSource.getMessage(
                        Error.FORBIDDEN_USER_UPDATE_ENABLED.getKey(),
                        getArgs(requestedUser),
                        LocaleContextHolder.getLocale()));
            }
        }
        if (updatedData.rolesExist()) {
            final var notAdmin = !authPrincipal.isAdmin();
            final var requestedUserIsAdmin = requestedUser.isAdmin();
            final var containsAdminRole = updatedData.isAdmin();

            if (notAdmin || requestedUserIsAdmin || containsAdminRole) {
                throw new ForbiddenException(messageSource.getMessage(
                    Error.FORBIDDEN_USER_UPDATE_ROLES.getKey(),
                    getArgs(requestedUser),
                    LocaleContextHolder.getLocale()));
            }
        }
    }

    private static Object[] getArgs(User requestedUser) {
        return new Object[]{requestedUser.getUserId()};
    }
}
