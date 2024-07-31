package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.input.user.CreateUserInputPort;
import com.gmail.apach.jenkins_demo.application.output.user.CreateUserOutputPort;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserService implements CreateUserInputPort {

    private final CreateUserOutputPort createUserOutputPort;

    @Override
    public User createUser(User user) {
        user.setEnabled(true);
        return createUserOutputPort.createUser(user);
    }
}
