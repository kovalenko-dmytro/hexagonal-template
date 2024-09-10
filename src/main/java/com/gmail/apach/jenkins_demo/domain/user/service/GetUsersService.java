package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.input.user.GetUsersInputPort;
import com.gmail.apach.jenkins_demo.application.output.user.GetUsersOutputPort;
import com.gmail.apach.jenkins_demo.common.util.CurrentUserContextUtil;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.validator.GetUsersPermissionsValidator;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersRequestWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUsersService implements GetUsersInputPort {

    private final CurrentUserContextUtil currentUserContextUtil;
    private final GetUsersPermissionsValidator getUsersPermissionsValidator;
    private final GetUsersOutputPort getUsersOutputPort;

    @Override
    public Page<User> getUsers(GetUsersRequestWrapper wrapper) {
        final var currentUserContext = currentUserContextUtil.getContext();
        getUsersPermissionsValidator.validate(currentUserContext);
        return getUsersOutputPort.getUsers(wrapper, currentUserContext);
    }
}
