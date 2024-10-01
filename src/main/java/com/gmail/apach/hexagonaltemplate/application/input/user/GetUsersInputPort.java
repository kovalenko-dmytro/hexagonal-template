package com.gmail.apach.hexagonaltemplate.application.input.user;

import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.wrapper.GetUsersRequestWrapper;
import org.springframework.data.domain.Page;

public interface GetUsersInputPort {

    Page<User> getUsers(GetUsersRequestWrapper wrapper);
}
