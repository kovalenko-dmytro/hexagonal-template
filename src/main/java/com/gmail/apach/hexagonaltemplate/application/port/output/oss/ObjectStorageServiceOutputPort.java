package com.gmail.apach.hexagonaltemplate.application.port.output.oss;

import io.awspring.cloud.s3.S3Resource;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageServiceOutputPort {
    S3Resource save(MultipartFile file);
    S3Resource get(@NotNull String objectKey);
    void delete(@NotNull String objectKey);
}
