package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.gmail.apach.jenkins_demo.application.output.user.CreateUserOutputPort;
import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.mapper.UserPersistenceMapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.RoleRepository;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateUserPersistenceAdapter implements CreateUserOutputPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserPersistenceMapper userMapper;

    @Override
    public User createUser(User user) {
        final var userEntity = userMapper.toUserEntity(user);
        if (CollectionUtils.isEmpty(userEntity.getRoles())) {
            roleRepository
                .findByRole(RoleType.USER)
                .ifPresent(role ->  userEntity.setRoles(Set.of(role)));
        }
        return userMapper.toUser(userRepository.save(userEntity));
    }
}
