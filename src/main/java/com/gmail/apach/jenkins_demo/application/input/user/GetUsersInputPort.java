package com.gmail.apach.jenkins_demo.application.input.user;

import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersRequestWrapper;
import org.springframework.data.domain.Page;

public interface GetUsersInputPort {

    Page<User> getUsers(GetUsersRequestWrapper wrapper);
}
