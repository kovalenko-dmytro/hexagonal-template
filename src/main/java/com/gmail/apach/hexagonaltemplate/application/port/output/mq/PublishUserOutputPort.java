package com.gmail.apach.hexagonaltemplate.application.port.output.mq;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

import java.util.List;

public interface PublishUserOutputPort {
    void publishImportUsers(List<User> users);
}
