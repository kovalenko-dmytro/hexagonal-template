package com.gmail.apach.jenkins_demo.domain.user.service;

import com.gmail.apach.jenkins_demo.application.input.user.GetUserInputPort;
import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.message.AttributeForModel;
import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.exception.EntityNotFoundException;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.validator.GetUserByIdPermissionsValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserService implements GetUserInputPort {

    private final GetUserOutputPort getUserOutputPort;
    private final GetUserByIdPermissionsValidator getUserByIdPermissionsValidator;
    private final MessageSource messageSource;

    @Override
    public User getByUsername(String username) {
        return getUserOutputPort
            .getByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException(getByUsernameErrorMessage(username)));
    }

    @Override
    public User getByUserId(String userId) {
        final var user = getUserOutputPort
            .getByUserId(userId)
            .orElseThrow(() -> new EntityNotFoundException(getByUserIdErrorMessage(userId)));
        getUserByIdPermissionsValidator.validate(user);
        return user;
    }

    private String getByUserIdErrorMessage(String userId) {
        return messageSource.getMessage(
            Error.ENTITY_NOT_FOUND.getKey(),
            getByUserIdErrorArgs(userId),
            LocaleContextHolder.getLocale());
    }

    private String getByUsernameErrorMessage(String username) {
        return messageSource.getMessage(
            Error.ENTITY_NOT_FOUND.getKey(),
            getByUsernameErrorArgs(username),
            LocaleContextHolder.getLocale());
    }

    private Object[] getByUserIdErrorArgs(String userId) {
        return new Object[]{
            AttributeForModel.USER.getName(),
            AttributeForModel.Field.ID.getFieldName(),
            userId
        };
    }

    private Object[] getByUsernameErrorArgs(String username) {
        return new Object[]{
            AttributeForModel.USER.getName(),
            AttributeForModel.Field.USER_NAME.getFieldName(),
            username
        };
    }
}
