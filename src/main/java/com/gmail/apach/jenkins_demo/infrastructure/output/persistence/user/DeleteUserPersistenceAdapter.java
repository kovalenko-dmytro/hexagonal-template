package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.gmail.apach.jenkins_demo.application.output.user.DeleteUserOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.cache.UserCacheConstant;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserPersistenceAdapter implements DeleteUserOutputPort {

    private final UserRepository userRepository;

    @Override
    @CacheEvict(
        value = UserCacheConstant.CACHE_NAME,
        key = UserCacheConstant.Key.ID,
        beforeInvocation = true
    )
    public void deleteByUserId(String userId) {
        userRepository.deleteById(userId);
    }
}
