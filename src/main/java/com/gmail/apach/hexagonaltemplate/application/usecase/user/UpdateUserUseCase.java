package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.UpdateUserInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.UpdateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.service.UserPermissionPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserUseCase implements UpdateUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final UpdateUserOutputPort updateUserOutputPort;
    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;

    @Override
    public User update(User user) {
        final var requestedUser = getUserOutputPort.getByUserId(user.getUserId());
        UserPermissionPolicyService.checkUpdateUserPolicy(preparePolicyContext(user, requestedUser));

        if (user.firstNameNonBlank()) {
            requestedUser.setFirstName(user.getFirstName());
        }
        if (user.lastNameNonBlank()) {
            requestedUser.setLastName(user.getLastName());
        }
        if (user.emailNonBlank()) {
            requestedUser.setEmail(user.getEmail());
        }
        if (user.enabledDefined()) {
            requestedUser.setEnabled(user.getEnabled());
        }
        if (user.rolesExist()) {
            requestedUser.setRoles(user.getRoles());
        }

        return updateUserOutputPort.update(requestedUser);
    }

    private UserPermissionPolicyContext preparePolicyContext(User inputAttributes, User processed) {
        return UserPermissionPolicyContext.builder()
            .inputAttributes(inputAttributes)
            .processed(processed)
            .principal(currentPrincipalOutputPort.getPrincipal())
            .build();
    }
}
