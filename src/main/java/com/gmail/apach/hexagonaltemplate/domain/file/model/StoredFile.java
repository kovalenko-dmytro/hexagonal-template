package com.gmail.apach.hexagonaltemplate.domain.file.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class StoredFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String fileId;
    private String storageKey;
    private String fileName;
    private String contentType;
    private long size;
    private LocalDateTime created;
}
