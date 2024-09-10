package com.gmail.apach.jenkins_demo.application.output.user;

import com.gmail.apach.jenkins_demo.domain.user.model.User;

public interface GetUserOutputPort {

    User getByUsername(String username);
    User getByUserId(String userId);
}
