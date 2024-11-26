package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.CreateUserInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.auth.CurrentPrincipalOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.mq.PublishEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.common.policy.context.UserPermissionPolicyContext;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailType;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.service.UserPermissionPolicyService;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.smpt.wrapper.SendEmailWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateUserUseCase implements CreateUserInputPort {

    private final CreateUserOutputPort createUserOutputPort;
    private final PublishEmailOutputPort publishEmailOutputPort;
    private final CurrentPrincipalOutputPort currentPrincipalOutputPort;

    @Override
    public User create(User user) {
        setUserAttributes(user);
        final var createdUser = createUserOutputPort.create(user);
        final var emailWrapper = prepareEmail(createdUser);
        publishEmailOutputPort.publishSend(emailWrapper);
        return createdUser;
    }

    private void setUserAttributes(User user) {
        final var currentPrincipal = currentPrincipalOutputPort.getPrincipal();
        processRoles(user, currentPrincipal);
        user.setEnabled(true);
        user.setCreated(LocalDateTime.now());
        user.setCreatedBy(currentPrincipal.getUsername());
    }

    private void processRoles(User user, User currentPrincipal) {
        if (user.rolesExist()) {
            UserPermissionPolicyService.checkCreateUserPolicy(preparePolicyContext(user, currentPrincipal));
        } else {
            user.setRoles(Set.of(Role.builder().role(RoleType.USER).build()));
        }
    }

    private UserPermissionPolicyContext preparePolicyContext(User inputAttributes, User principal) {
        return UserPermissionPolicyContext.builder()
            .inputAttributes(inputAttributes)
            .principal(principal)
            .build();
    }

    private SendEmailWrapper prepareEmail(User user) {
        final var properties = new HashMap<String, Object>();
        properties.put(EmailType.Property.RECIPIENT_NAME.getProperty(), user.getUsername());
        properties.put(EmailType.Property.SENDER_NAME.getProperty(), user.getCreatedBy());

        return SendEmailWrapper.builder()
            .sendBy(user.getCreatedBy()).sendTo(user.getEmail())
            .properties(properties)
            .subject("Hello").emailType(EmailType.INVITE)
            .build();
    }
}
