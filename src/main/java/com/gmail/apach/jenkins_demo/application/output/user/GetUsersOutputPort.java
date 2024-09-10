package com.gmail.apach.jenkins_demo.application.output.user;

import com.gmail.apach.jenkins_demo.common.dto.CurrentUserContext;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersRequestWrapper;
import org.springframework.data.domain.Page;

public interface GetUsersOutputPort {

    Page<User> getUsers(GetUsersRequestWrapper wrapper, CurrentUserContext context);
}
