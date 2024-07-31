package com.gmail.apach.jenkins_demo.application.output.user;

import com.gmail.apach.jenkins_demo.domain.user.model.User;

import java.util.Optional;

public interface GetUserOutputPort {

    Optional<User> getByUsername(String username);
}
