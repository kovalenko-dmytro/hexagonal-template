package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.CreateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.UserCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity.RoleEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity.UserEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.mapper.UserDbMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.repository.RoleRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CreateUserDbAdapter implements CreateUserOutputPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDbMapper userMapper;

    @Override
    @CacheEvict(
        value = UserCacheConstant.LIST_CACHE_NAME,
        allEntries = true
    )
    public User create(User user) {
        final var userEntity = userMapper.toUserEntity(user);
        setRoles(userEntity);
        return userMapper.toUser(userRepository.save(userEntity));
    }

    @CacheEvict(
        value = UserCacheConstant.LIST_CACHE_NAME,
        allEntries = true
    )
    @Override
    public void create(List<User> users) {
        final var userEntities = userMapper.toUserEntity(users);
        final var allRoles = roleRepository.findAll();
        final var userRoles = allRoles.stream()
            .filter(roleEntity -> RoleType.USER.equals(roleEntity.getRole()))
            .collect(Collectors.toSet());
        userEntities.forEach(userEntity -> {
            userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
            userEntity.setRoles(
                CollectionUtils.isEmpty(userEntity.getRoles()) ? userRoles : obtainRoles(userEntity, allRoles));
        });
        userRepository.saveAll(userEntities);
    }

    private Set<RoleEntity> obtainRoles(UserEntity userEntity, List<RoleEntity> allRoles) {
        Set<RoleType> userRoles = userEntity.getRoles().stream()
            .map(RoleEntity::getRole)
            .collect(Collectors.toSet());
        return allRoles.stream()
            .filter(roleEntity -> userRoles.contains(roleEntity.getRole()))
            .collect(Collectors.toSet());
    }

    private void setRoles(UserEntity userEntity) {
        if (CollectionUtils.isEmpty(userEntity.getRoles())) {
            roleRepository
                .findByRole(RoleType.USER)
                .ifPresent(role -> userEntity.setRoles(Set.of(role)));
        } else {
            final var roleTypes = userEntity.getRoles().stream()
                .map(RoleEntity::getRole).toList();
            final var roles = roleRepository.findByRoleIn(roleTypes);
            userEntity.setRoles(roles);
        }
    }
}
