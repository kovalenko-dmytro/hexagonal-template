package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.gmail.apach.jenkins_demo.application.output.user.DeleteUserOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.cache.CacheConstant;
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
        value = CacheConstant.User.CACHE_NAME,
        key = CacheConstant.User.Key.ID,
        beforeInvocation = true
    )
    public void deleteByUserId(String userId) {
        userRepository.deleteById(userId);
    }
}
