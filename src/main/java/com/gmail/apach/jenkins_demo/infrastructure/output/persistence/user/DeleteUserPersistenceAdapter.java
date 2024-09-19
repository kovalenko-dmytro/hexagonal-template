package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.gmail.apach.jenkins_demo.application.output.user.DeleteUserOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.cache.UserCacheConstant;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserPersistenceAdapter implements DeleteUserOutputPort {

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
