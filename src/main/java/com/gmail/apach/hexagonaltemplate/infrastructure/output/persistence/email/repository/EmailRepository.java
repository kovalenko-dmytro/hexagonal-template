package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository
    extends JpaRepository<EmailEntity, String>, JpaSpecificationExecutor<EmailEntity> {
}
