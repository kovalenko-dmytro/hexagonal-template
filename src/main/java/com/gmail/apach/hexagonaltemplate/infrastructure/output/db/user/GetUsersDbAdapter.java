package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.GetUsersOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.UserCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.mapper.UserDbMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.repository.UserRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.specification.UserSpecifications;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.GetUsersFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUsersDbAdapter implements GetUsersOutputPort {

    private final UserRepository userRepository;
    private final UserDbMapper userMapper;

    @Override
    @Cacheable(
        value = UserCacheConstant.LIST_CACHE_NAME,
        key = UserCacheConstant.Key.SEARCH,
        condition = UserCacheConstant.Condition.SEARCH
    )
    public Page<User> getUsers(GetUsersFilterWrapper wrapper) {
        return userRepository
            .findAll(UserSpecifications.specification(wrapper), wrapper.toPageable())
            .map(userMapper::toUser);
    }
}
