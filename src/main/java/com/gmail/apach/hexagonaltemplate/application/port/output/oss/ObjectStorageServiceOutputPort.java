package com.gmail.apach.hexagonaltemplate.application.port.output.oss;

import com.gmail.apach.hexagonaltemplate.domain.file.vo.StoredResource;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorageServiceOutputPort {
    StoredResource save(MultipartFile file);
    StoredResource get(@NotNull String objectKey);
    void delete(@NotNull String objectKey);
}
