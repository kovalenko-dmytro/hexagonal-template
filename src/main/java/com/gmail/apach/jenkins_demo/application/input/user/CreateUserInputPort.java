package com.gmail.apach.jenkins_demo.application.input.user;

import com.gmail.apach.jenkins_demo.domain.user.model.User;

public interface CreateUserInputPort {

    User createUser(User user);
}
