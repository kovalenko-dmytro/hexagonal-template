package com.gmail.apach.jenkins_demo.application.output.user;

import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersSearchSortPageWrapper;
import org.springframework.data.domain.Page;

public interface GetUsersOutputPort {

    Page<User> getUsers(GetUsersSearchSortPageWrapper wrapper, boolean isAdmin);
}
