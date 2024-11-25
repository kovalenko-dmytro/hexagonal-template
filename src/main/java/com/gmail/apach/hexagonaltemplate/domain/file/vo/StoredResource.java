package com.gmail.apach.hexagonaltemplate.domain.file.vo;

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
public class StoredResource implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String storageKey;
    private byte[] payload;
}
