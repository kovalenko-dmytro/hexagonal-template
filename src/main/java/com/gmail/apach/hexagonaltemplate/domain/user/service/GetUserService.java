package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.application.input.user.GetUserInputPort;
import com.gmail.apach.hexagonaltemplate.application.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.validator.GetUserByIdPermissionsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final GetUserByIdPermissionsValidator getUserByIdPermissionsValidator;

    @Override
    public User getByUsername(String username) {
        return getUserOutputPort.getByUsername(username);
    }

    @Override
    public User getByUserId(String userId) {
        final var user = getUserOutputPort.getByUserId(userId);
        getUserByIdPermissionsValidator.validate(user);
        return user;
    }
}
