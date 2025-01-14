package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.GetUserInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserValidationContext;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserUseCase implements GetUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;

    @Override
    public User getByUsername(String username) {
        return getUserOutputPort.getByUsername(username);
    }

    @Override
    public User getByUserId(String userId) {
        final var user = getUserOutputPort.getByUserId(userId);
        UserValidationService.checkGetUserByIdPolicy(preparePolicyContext(user));
        return user;
    }

    private UserValidationContext preparePolicyContext(User processed) {
        return UserValidationContext.builder()
            .processed(processed)
            .principal(currentPrincipalOutputPort.getPrincipal())
            .build();
    }
}
