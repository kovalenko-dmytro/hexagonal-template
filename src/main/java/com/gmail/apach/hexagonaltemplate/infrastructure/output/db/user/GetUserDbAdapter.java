package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.UserCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.AttributeForModel;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.mapper.UserDbMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUserDbAdapter implements GetUserOutputPort {

    private final UserRepository userRepository;
    private final UserDbMapper userMapper;
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
