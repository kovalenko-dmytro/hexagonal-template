package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.repository;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository
    extends JpaRepository<FileEntity, String>, JpaSpecificationExecutor<FileEntity> {
}
