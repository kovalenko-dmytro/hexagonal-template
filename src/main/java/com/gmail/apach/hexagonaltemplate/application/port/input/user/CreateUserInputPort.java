package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.PublishEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.application.port.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.application.usecase.user.CreateUserUseCase;
import com.gmail.apach.hexagonaltemplate.domain.user.model.AuthPrincipal;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.policy.CreateUserPermissionPolicy;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateUserInputPort implements CreateUserUseCase {

    private final PasswordEncoder passwordEncoder;
    private final CreateUserOutputPort createUserOutputPort;
    private final CreateUserPermissionPolicy createUserPermissionPolicy;
    private final PublishEmailOutputPort publishEmailOutputPort;

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreated(LocalDateTime.now());
        user.setCreatedBy(AuthPrincipal.getDetails().getUsername());

        if (user.rolesExist()) {
            createUserPermissionPolicy.check(user);
        } else {
            user.setRoles(Set.of(Role.builder().role(RoleType.USER).build()));
        }

        final var createdUser = createUserOutputPort.createUser(user);
        publishEmailOutputPort.publishSendInviteEmail(createdUser);
        return createdUser;
    }
}
