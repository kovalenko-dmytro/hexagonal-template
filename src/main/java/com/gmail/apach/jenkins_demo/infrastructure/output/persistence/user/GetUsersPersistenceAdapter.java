package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.gmail.apach.jenkins_demo.application.output.user.GetUsersOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.CommonConstant;
import com.gmail.apach.jenkins_demo.common.dto.CurrentUserContext;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersRequestWrapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.mapper.UserPersistenceMapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.UserRepository;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.specification.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GetUsersPersistenceAdapter implements GetUsersOutputPort {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;

    @Override
    public Page<User> getUsers(GetUsersRequestWrapper wrapper, CurrentUserContext context) {
        final var pageable = Objects.isNull(wrapper.sort()) || wrapper.sort().length == 0
            ? PageRequest.of(wrapper.page() - 1, wrapper.size())
            : PageRequest.of(wrapper.page() - 1, wrapper.size(), Sort.by(defineOrders(wrapper.sort())));

        return userRepository
            .findAll(UserSpecifications.specification(wrapper, context), pageable)
            .map(userMapper::toUser);
    }

    private List<Sort.Order> defineOrders(String[] sort) {
        return Arrays.stream(sort)
            .map(sortOrder -> {
                final var _sort = Arrays.asList(sortOrder.split(CommonConstant.SPACE.getValue()));
                if (CollectionUtils.isEmpty(_sort)) {
                    return null;
                }
                if (_sort.size() == 1) {
                    return new Sort.Order(Sort.Direction.ASC, _sort.get(0));
                }
                final var order = _sort.get(1).equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
                return new Sort.Order(order, _sort.get(0));
            })
            .filter(Objects::nonNull)
            .toList();
    }
}
