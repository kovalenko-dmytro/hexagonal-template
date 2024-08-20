package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.gmail.apach.jenkins_demo.application.output.user.GetUserOutputPort;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.mapper.UserPersistenceMapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GetUserPersistenceAdapter implements GetUserOutputPort {

    private final UserRepository userRepository;
    private final UserPersistenceMapper userMapper;

    @Override
    public Optional<User> getByUsername(String username) {
        return userRepository.findByUsername(username).map(userMapper::toUser);
    }

    @Override
    public Optional<User> getByUserId(String userId) {
        return userRepository.findById(userId).map(userMapper::toUser);
    }
}
