package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.cache.UserCacheConstant;
import com.gmail.apach.jenkins_demo.common.constant.message.AttributeForModel;
import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.exception.ResourceNotFoundException;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.mapper.UserPersistenceMapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserPersistenceAdapter implements GetUserOutputPort {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;
    private final MessageSource messageSource;

    @Override
    public User getByUsername(String username) {
        return userRepository
            .findByUsername(username)
            .map(userMapper::toUser)
            .orElseThrow(() -> new ResourceNotFoundException(getByUsernameErrorMessage(username)));
    }

    @Override
    @Cacheable(
        value = UserCacheConstant.CACHE_NAME,
        key = UserCacheConstant.Key.ID
    )
    public User getByUserId(String userId) {
        return userRepository
            .findById(userId)
            .map(userMapper::toUser)
            .orElseThrow(() -> new ResourceNotFoundException(getByUserIdErrorMessage(userId)));
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
