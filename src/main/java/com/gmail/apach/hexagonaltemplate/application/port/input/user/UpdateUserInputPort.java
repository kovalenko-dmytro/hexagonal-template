package com.gmail.apach.hexagonaltemplate.application.port.input.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

public interface UpdateUserInputPort {
    User update(User user);
}
