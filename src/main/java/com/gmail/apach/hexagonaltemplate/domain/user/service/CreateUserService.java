package com.gmail.apach.hexagonaltemplate.domain.user.service;

import com.gmail.apach.hexagonaltemplate.application.input.user.CreateUserInputPort;
import com.gmail.apach.hexagonaltemplate.application.output.email.SendEmailPublisher;
import com.gmail.apach.hexagonaltemplate.application.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.RoleType;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.validator.CreateUserPermissionsValidator;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.util.CurrentUserContextUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserInputPort {

    private final PasswordEncoder passwordEncoder;
    private final CreateUserOutputPort createUserOutputPort;
    private final CreateUserPermissionsValidator createUserPermissionsValidator;
    private final CurrentUserContextUtil currentUserContextUtil;
    private final SendEmailPublisher sendEmailPublisher;

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

        sendEmailPublisher.publishInviteEmail(createdUser);

        return createdUser;
    }
}
