package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.input.user.DeleteUserInputPort;
import com.gmail.apach.jenkins_demo.application.output.user.DeleteUserOutputPort;
import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.domain.user.validator.DeleteUserPermissionsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteUserService implements DeleteUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final DeleteUserOutputPort deleteUserOutputPort;
    private final DeleteUserPermissionsValidator deleteUserPermissionsValidator;

    @Override
    public void deleteByUserId(String userId) {
        final var deletedUser = getUserOutputPort.getByUserId(userId);
        deleteUserPermissionsValidator.validate(deletedUser);
        deleteUserOutputPort.deleteByUserId(deletedUser.getUserId());
    }
}
