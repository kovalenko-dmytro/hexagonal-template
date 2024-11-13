package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.repository;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailRepository
    extends JpaRepository<EmailEntity, String>, JpaSpecificationExecutor<EmailEntity> {

    Optional<EmailEntity> findBySendBy(String sendBy);
}
