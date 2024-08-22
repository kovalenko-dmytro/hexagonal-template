package com.gmail.apach.jenkins_demo.application.output.user;

import com.gmail.apach.jenkins_demo.domain.user.model.User;

public interface UpdateUserOutputPort {

    User update(User user);
}
