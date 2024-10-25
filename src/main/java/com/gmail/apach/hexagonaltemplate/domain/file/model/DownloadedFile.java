package com.gmail.apach.hexagonaltemplate.domain.file.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DownloadedFile implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String fileName;
    private byte[] payload;
}
