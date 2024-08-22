package com.gmail.apach.jenkins_demo.domain.user.validator;

import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.dto.CurrentUserContext;
import com.gmail.apach.jenkins_demo.common.exception.ForbiddenException;
import com.gmail.apach.jenkins_demo.common.util.CurrentUserContextUtil;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
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
        CurrentUserContext currentUserContext = currentUserContextUtil.getContext();
        validateUpdatePermissions(requestedUser, currentUserContext);
        validateRequestedChanges(requestedUser, updatedData, currentUserContext);
    }

    private void validateUpdatePermissions(User requestedUser, CurrentUserContext context) {
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

    private void validateRequestedChanges(User requestedUser, User updatedData, CurrentUserContext context) {
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
