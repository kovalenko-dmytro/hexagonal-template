package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.input.user.CreateUserInputPort;
import com.gmail.apach.jenkins_demo.application.output.user.CreateUserOutputPort;
import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.validator.CreateUserPermissionsValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Override
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreated(LocalDateTime.now());
        user.setCreatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        if (CollectionUtils.isNotEmpty(user.getRoles())) {
            createUserPermissionsValidator.validate(user.getRoles());
        } else {
            user.setRoles(Set.of(Role.builder().role(RoleType.USER).build()));
        }
        return createUserOutputPort.createUser(user);
    }
}
