package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user;

import com.gmail.apach.hexagonaltemplate.application.output.user.GetUsersOutputPort;
import com.gmail.apach.hexagonaltemplate.common.constant.cache.UserCacheConstant;
import com.gmail.apach.hexagonaltemplate.common.dto.CurrentUserContext;
import com.gmail.apach.hexagonaltemplate.common.util.PageableUtil;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.wrapper.GetUsersRequestWrapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.mapper.UserPersistenceMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.repository.UserRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.specification.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetUsersPersistenceAdapter implements GetUsersOutputPort {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;
    private final PageableUtil pageableUtil;

    @Override
    @Cacheable(
        value = UserCacheConstant.LIST_CACHE_NAME,
        key = UserCacheConstant.Key.SEARCH,
        condition = UserCacheConstant.Condition.SEARCH
    )
    public Page<User> getUsers(GetUsersRequestWrapper wrapper, CurrentUserContext context) {
        final var pageable = pageableUtil.obtainPageable(wrapper.page(), wrapper.size(), wrapper.sort());
        return userRepository
            .findAll(UserSpecifications.specification(wrapper, context), pageable)
            .map(userMapper::toUser);
    }
}