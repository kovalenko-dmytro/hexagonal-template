package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "files")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class FileEntity {

    @Id
    @UuidGenerator
    @Column(name = "file_", length = 36, unique = true, nullable = false)
    private String fileId;

    @Column(name = "storage_key", nullable = false)
    private String storageKey;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private long size;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;
}
