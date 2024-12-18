package com.gmail.apach.hexagonaltemplate.application.port.output.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.GetUsersFilterWrapper;
import org.springframework.data.domain.Page;

public interface GetUsersOutputPort {
    Page<User> get(GetUsersFilterWrapper wrapper);
}
