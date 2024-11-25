package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.GetUserInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.GetUserByIdPermissionPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserUseCase implements GetUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final GetUserByIdPermissionPolicy getUserByIdPermissionPolicy;

    @Override
    public User getByUsername(String username) {
        return getUserOutputPort.getByUsername(username);
    }

    @Override
    public User getByUserId(String userId) {
        final var user = getUserOutputPort.getByUserId(userId);
        getUserByIdPermissionPolicy.check(user);
        return user;
    }
}
