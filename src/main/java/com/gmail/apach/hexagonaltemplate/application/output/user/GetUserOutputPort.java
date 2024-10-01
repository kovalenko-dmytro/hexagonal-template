package com.gmail.apach.hexagonaltemplate.application.output.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

public interface GetUserOutputPort {

    User getByUsername(String username);
    User getByUserId(String userId);
}
