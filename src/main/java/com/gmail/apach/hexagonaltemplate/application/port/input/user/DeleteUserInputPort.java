package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.DeleteUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.user.DeleteUserUseCase;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.DeleteUserPermissionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserInputPort implements DeleteUserUseCase {

    private final GetUserOutputPort getUserOutputPort;
    private final DeleteUserOutputPort deleteUserOutputPort;
    private final DeleteUserPermissionPolicy deleteUserPermissionPolicy;

    @Override
    public void deleteByUserId(String userId) {
        final var deletedUser = getUserOutputPort.getByUserId(userId);
        deleteUserPermissionPolicy.check(deletedUser);
        deleteUserOutputPort.deleteByUserId(deletedUser.getUserId());
    }
}
