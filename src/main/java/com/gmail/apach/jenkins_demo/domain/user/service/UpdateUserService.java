package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.input.user.UpdateUserInputPort;
import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.application.output.user.UpdateUserOutputPort;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.validator.UpdateUserPermissionsValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UpdateUserService implements UpdateUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final UpdateUserOutputPort updateUserOutputPort;
    private final UpdateUserPermissionsValidator updateUserPermissionsValidator;

    @Override
    public User update(User user) {
        final var requestedUser = getUserOutputPort.getByUserId(user.getUserId());

        updateUserPermissionsValidator.validate(requestedUser, user);

        if (Objects.nonNull(user.getFirstName())) {
            requestedUser.setFirstName(user.getFirstName());
        }
        if (Objects.nonNull(user.getLastName())) {
            requestedUser.setLastName(user.getLastName());
        }
        if (Objects.nonNull(user.getEmail())) {
            requestedUser.setEmail(user.getEmail());
        }
        if (Objects.nonNull(user.getEnabled())) {
            requestedUser.setEnabled(user.getEnabled());
        }
        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            requestedUser.setRoles(user.getRoles());
        }

        return updateUserOutputPort.update(requestedUser);
    }
}
