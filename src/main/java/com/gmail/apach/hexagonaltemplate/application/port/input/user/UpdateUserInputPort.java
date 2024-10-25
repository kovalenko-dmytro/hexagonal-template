package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.UpdateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.user.UpdateUserUseCase;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.UpdateUserPermissionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserInputPort implements UpdateUserUseCase {

    private final GetUserOutputPort getUserOutputPort;
    private final UpdateUserOutputPort updateUserOutputPort;
    private final UpdateUserPermissionPolicy updateUserPermissionPolicy;

    @Override
    public User update(User user) {
        final var requestedUser = getUserOutputPort.getByUserId(user.getUserId());

        updateUserPermissionPolicy.check(requestedUser, user);

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
}
