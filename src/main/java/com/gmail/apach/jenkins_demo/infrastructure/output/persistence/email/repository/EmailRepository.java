package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.repository;

import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.entity.EmailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailEntity, String> {
}
