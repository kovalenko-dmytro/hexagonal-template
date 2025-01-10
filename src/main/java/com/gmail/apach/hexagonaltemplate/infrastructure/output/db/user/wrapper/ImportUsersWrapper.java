package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

import java.util.List;

public record ImportUsersWrapper(
    List<User> users
) {
}
