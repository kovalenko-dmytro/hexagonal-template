package com.gmail.apach.hexagonaltemplate.domain.user.validator;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ForbiddenException;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.util.CurrentUserContextUtil;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.CurrentUserAuthContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UpdateUserPermissionsValidator {

    private final CurrentUserContextUtil currentUserContextUtil;
    private final MessageSource messageSource;

    public void validate(User requestedUser, User updatedData) {
        CurrentUserAuthContext currentUserAuthContext = currentUserContextUtil.getContext();
        validateUpdatePermissions(requestedUser, currentUserAuthContext);
        validateRequestedChanges(requestedUser, updatedData, currentUserAuthContext);
    }

    private void validateUpdatePermissions(User requestedUser, CurrentUserAuthContext context) {
        final var isAdmin = context.isAdmin();

        final var managerTriesToUpdateSelfOrAnyUsers =
            context.isManager()
                && (requestedUser.getUsername().contentEquals(context.username())
                || requestedUser.isUser());

        final var userTriesToUpdateSelf =
            context.isUser()
                && requestedUser.getUsername().contentEquals(context.username());

        if (!(isAdmin || managerTriesToUpdateSelfOrAnyUsers || userTriesToUpdateSelf)) {
            throw new ForbiddenException(messageSource.getMessage(
                Error.FORBIDDEN_USER_UPDATE_BY_ID.getKey(),
                new Object[]{requestedUser.getUserId()},
                LocaleContextHolder.getLocale()));
        }
    }

    private void validateRequestedChanges(User requestedUser, User updatedData, CurrentUserAuthContext context) {
        if (Objects.nonNull(updatedData.getEnabled()) && !requestedUser.getEnabled().equals(updatedData.getEnabled())) {
            final var isAdmin = context.isAdmin() && !requestedUser.isAdmin();
            final var managerTriesToUpdateEnabledToAnyUsers = context.isManager() && requestedUser.isUser();
            if (!(isAdmin || managerTriesToUpdateEnabledToAnyUsers)) {
                throw new ForbiddenException(messageSource.getMessage(
                    Error.FORBIDDEN_USER_UPDATE_ENABLED.getKey(),
                    new Object[]{requestedUser.getUserId()},
                    LocaleContextHolder.getLocale()));
            }
        }
        if (CollectionUtils.isNotEmpty(updatedData.getRoles())) {
            final var notAdmin = !context.isAdmin();
            final var requestedUserIsAdmin = requestedUser.isAdmin();
            final var containsAdminRole = updatedData.isAdmin();

            if (notAdmin || requestedUserIsAdmin || containsAdminRole) {
                throw new ForbiddenException(messageSource.getMessage(
                    Error.FORBIDDEN_USER_UPDATE_ROLES.getKey(),
                    new Object[]{requestedUser.getUserId()},
                    LocaleContextHolder.getLocale()));
            }
        }
    }
}
