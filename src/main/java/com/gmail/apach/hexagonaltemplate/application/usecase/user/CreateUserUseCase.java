package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.application.port.input.user.CreateUserInputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.email.PublishEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.vo.EmailType;
import com.gmail.apach.hexagonaltemplate.domain.user.model.AuthPrincipal;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.CreateUserPermissionPolicy;
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
    private final CreateUserPermissionPolicy createUserPermissionPolicy;
    private final PublishEmailOutputPort publishEmailOutputPort;

    @Override
    public User createUser(User user) {
        setUserAttributes(user);
        final var createdUser = createUserOutputPort.createUser(user);
        final var emailWrapper = prepareEmail(createdUser);
        publishEmailOutputPort.publishSendEmail(emailWrapper);
        return createdUser;
    }

    private void setUserAttributes(User user) {
        user.setEnabled(true);
        user.setCreated(LocalDateTime.now());
        user.setCreatedBy(AuthPrincipal.getDetails().getUsername());
        processRoles(user);
    }

    private void processRoles(User user) {
        if (user.rolesExist()) {
            createUserPermissionPolicy.check(user);
        } else {
            user.setRoles(Set.of(Role.builder().role(RoleType.USER).build()));
        }
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
