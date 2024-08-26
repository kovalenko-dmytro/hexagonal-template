package com.gmail.apach.jenkins_demo.application.input.user;

import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersSearchSortPageWrapper;
import org.springframework.data.domain.Page;

public interface GetUsersInputPort {

    Page<User> getUsers(GetUsersSearchSortPageWrapper wrapper);
}
