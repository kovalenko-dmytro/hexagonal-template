package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.DeleteFileOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.FileCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteFileDbAdapter implements DeleteFileOutputPort {

    private final FileRepository fileRepository;

    @Caching(
        evict = {
            @CacheEvict(
                value = FileCacheConstant.LIST_CACHE_NAME,
                allEntries = true
            ),
            @CacheEvict(
                value = FileCacheConstant.CACHE_NAME,
                key = FileCacheConstant.Key.ID
            )
        }
    )
    @Override
    public void deleteByFileId(String fileId) {
        fileRepository.deleteById(fileId);
    }
}
