package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.repository;

import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByUsername(String username);
}
