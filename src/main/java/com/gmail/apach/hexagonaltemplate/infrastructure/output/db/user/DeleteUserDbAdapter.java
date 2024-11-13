package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.gmail.apach.hexagonaltemplate.application.port.output.user.DeleteUserOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.UserCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserDbAdapter implements DeleteUserOutputPort {

    private final UserRepository userRepository;

    @Caching(
        evict = {
            @CacheEvict(
                value = UserCacheConstant.LIST_CACHE_NAME,
                allEntries = true
            ),
            @CacheEvict(
                value = UserCacheConstant.CACHE_NAME,
                key = UserCacheConstant.Key.ID
            )
        }
    )
    @Override
    public void deleteByUserId(String userId) {
        userRepository.deleteById(userId);
    }
}
