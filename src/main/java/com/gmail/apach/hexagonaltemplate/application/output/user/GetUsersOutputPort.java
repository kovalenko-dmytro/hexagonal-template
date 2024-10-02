package com.gmail.apach.hexagonaltemplate.application.output.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.wrapper.GetUsersRequestWrapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.CurrentUserContext;
import org.springframework.data.domain.Page;

public interface GetUsersOutputPort {

    Page<User> getUsers(GetUsersRequestWrapper wrapper, CurrentUserContext context);
}
