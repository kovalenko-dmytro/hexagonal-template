package com.gmail.apach.hexagonaltemplate.application.port.output.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.wrapper.GetUsersFilterWrapper;
import org.springframework.data.domain.Page;

public interface GetUsersOutputPort {
    Page<User> getUsers(GetUsersFilterWrapper wrapper);
}
