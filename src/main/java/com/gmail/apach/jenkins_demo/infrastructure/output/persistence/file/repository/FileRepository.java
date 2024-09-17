package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.repository;

import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository
    extends JpaRepository<FileEntity, String>, JpaSpecificationExecutor<FileEntity> {
}
