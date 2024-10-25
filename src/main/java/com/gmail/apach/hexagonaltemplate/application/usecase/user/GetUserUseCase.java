package com.gmail.apach.hexagonaltemplate.application.usecase.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

public interface GetUserUseCase {
    User getByUsername(String username);
    User getByUserId(String userId);
}
