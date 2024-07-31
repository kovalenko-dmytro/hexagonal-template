package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.input.user.GetUserInputPort;
import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.message.AttributeForModel;
import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.exception.EntityNotFoundException;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final MessageSource messageSource;

    @Override
    public User getByUsername(String username) {
        return getUserOutputPort
            .getByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(getUserNotFoundMessage(username)));
    }

    private String getUserNotFoundMessage(String username) {
        return messageSource.getMessage(
            Error.ENTITY_NOT_FOUND.getKey(),
            getEntityNoFoundErrorArgs(username),
            LocaleContextHolder.getLocale());
    }

    private Object[] getEntityNoFoundErrorArgs(String username) {
        return new Object[]{
            AttributeForModel.USER.getName(),
            AttributeForModel.Field.USER_NAME.getFieldName(),
            username
        };
    }
}
