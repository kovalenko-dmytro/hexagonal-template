package com.gmail.apach.hexagonaltemplate.application.port.output.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

import java.util.List;

public interface CreateUserOutputPort {
    User create(User user);
    void create(List<User> users);
}
