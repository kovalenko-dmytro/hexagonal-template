package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.gmail.apach.jenkins_demo.application.output.user.CreateUserOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.cache.CacheConstant;
import com.gmail.apach.jenkins_demo.domain.user.model.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.entity.RoleEntity;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.entity.UserEntity;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.mapper.UserPersistenceMapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.RoleRepository;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CreateUserPersistenceAdapter implements CreateUserOutputPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserPersistenceMapper userMapper;

    @Override
    @CacheEvict(
        value = CacheConstant.User.LIST_CACHE_NAME,
        key = CacheConstant.User.Key.ID,
        allEntries = true
    )
    public User createUser(User user) {
        final var userEntity = userMapper.toUserEntity(user);
        setRoles(userEntity);
        return userMapper.toUser(userRepository.save(userEntity));
    }

    private void setRoles(UserEntity userEntity) {
        if (CollectionUtils.isEmpty(userEntity.getRoles())) {
            roleRepository
                .findByRole(RoleType.USER)
                .ifPresent(role ->  userEntity.setRoles(Set.of(role)));
        } else {
            final var roleTypes = userEntity.getRoles().stream()
                .map(RoleEntity::getRole).toList();
            final var roles = roleRepository.findByRoleIn(roleTypes);
            userEntity.setRoles(roles);
        }
    }
}
