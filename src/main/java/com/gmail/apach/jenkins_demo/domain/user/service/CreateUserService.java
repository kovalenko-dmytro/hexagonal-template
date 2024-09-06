package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.input.user.CreateUserInputPort;
import com.gmail.apach.jenkins_demo.application.output.user.CreateUserOutputPort;
import com.gmail.apach.jenkins_demo.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.jenkins_demo.common.util.CurrentUserContextUtil;
import com.gmail.apach.jenkins_demo.domain.email.model.EmailType;
import com.gmail.apach.jenkins_demo.domain.email.wrapper.SendEmailWrapper;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.validator.CreateUserPermissionsValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserInputPort {

    private final PasswordEncoder passwordEncoder;
    private final CreateUserOutputPort createUserOutputPort;
    private final CreateUserPermissionsValidator createUserPermissionsValidator;
    private final CurrentUserContextUtil currentUserContextUtil;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public User createUser(User user) {
       final var createdBy = currentUserContextUtil.getContext().username();

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreated(LocalDateTime.now());
        user.setCreatedBy(createdBy);

        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            createUserPermissionsValidator.validate(user.getRoles());
        } else {
            user.setRoles(Set.of(Role.builder().role(RoleType.USER).build()));
        }

        final var createdUser = createUserOutputPort.createUser(user);

        sendEmail(createdBy, createdUser);

        return createdUser;
    }

    private void sendEmail(String createdBy, User createdUser) {
        final var emailData = SendEmailWrapper.builder()
            .sendBy(createdBy)
            .sendTo(createdUser.getEmail())
            .properties(Map.of(
                EmailType.Property.RECIPIENT_NAME.getProperty(), createdUser.getUsername(),
                EmailType.Property.SENDER_NAME.getProperty(), createdBy
            ))
            .subject("Hello")
            .emailType(EmailType.INVITE)
            .build();

        rabbitTemplate.convertAndSend(
            EmailProcessingConfig.EMAIL_DIRECT_EXCHANGE,
            EmailProcessingConfig.EMAIL_ROUTING_KEY,
            emailData);
    }
}
