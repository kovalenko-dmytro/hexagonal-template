package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user;

import com.gmail.apach.hexagonaltemplate.application.output.user.UpdateUserOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.UserCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.entity.RoleEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.entity.UserEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.mapper.UserPersistenceMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.repository.RoleRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateUserPersistenceAdapter implements UpdateUserOutputPort {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserPersistenceMapper userMapper;

    @Override
    @Caching(
        evict = {
            @CacheEvict(
                value = UserCacheConstant.LIST_CACHE_NAME,
                allEntries = true
            )
        },
        put = {
            @CachePut(
                value = UserCacheConstant.CACHE_NAME,
                key = UserCacheConstant.Key.USER__ID
            )
        }
    )
    public User update(User user) {
        final var userEntity = userMapper.toUserEntity(user);
        setRoles(userEntity);
        return userMapper.toUser(userRepository.save(userEntity));
    }

    private void setRoles(UserEntity userEntity) {
        final var roleTypes = userEntity.getRoles().stream()
            .map(RoleEntity::getRole).toList();
        final var roles = roleRepository.findByRoleIn(roleTypes);
        userEntity.setRoles(roles);
    }
}
