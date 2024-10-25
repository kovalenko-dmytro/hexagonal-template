package com.gmail.apach.hexagonaltemplate.application.port.output.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;

public interface UpdateUserOutputPort {
    User update(User user);
}
