package com.gmail.apach.hexagonaltemplate.application.input.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

public interface GetUserInputPort {

    User getByUsername(String username);
    User getByUserId(String userId);
}
