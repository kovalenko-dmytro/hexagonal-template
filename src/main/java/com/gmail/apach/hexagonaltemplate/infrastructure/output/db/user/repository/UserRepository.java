package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.repository;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository
    extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    Optional<UserEntity> findByUsername(String username);
}
