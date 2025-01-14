package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.DeleteUserInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.DeleteUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserValidationContext;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.service.UserValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserUseCase implements DeleteUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final DeleteUserOutputPort deleteUserOutputPort;
    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;

    @Override
    public void deleteByUserId(String userId) {
        final var deletedUser = getUserOutputPort.getByUserId(userId);
        UserValidationService.checkDeleteUserPolicy(preparePolicyContext(deletedUser));
        deleteUserOutputPort.deleteByUserId(deletedUser.getUserId());
    }

    private UserValidationContext preparePolicyContext(User processed) {
        return UserValidationContext.builder()
            .processed(processed)
            .principal(currentPrincipalOutputPort.getPrincipal())
            .build();
    }
}
