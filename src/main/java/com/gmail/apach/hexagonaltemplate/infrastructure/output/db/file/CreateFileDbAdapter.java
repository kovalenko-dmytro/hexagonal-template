package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.CreateFileOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.FileCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.mapper.FileDbMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateFileDbAdapter implements CreateFileOutputPort {

    private final FileRepository fileRepository;
    private final FileDbMapper fileDbMapper;

    @CacheEvict(
        value = FileCacheConstant.LIST_CACHE_NAME,
        allEntries = true
    )
    @Override
    public StoredFile create(StoredFile file) {
        final var fileEntity = fileDbMapper.toFileEntity(file);
        final var savedFile = fileRepository.save(fileEntity);
        return fileDbMapper.toStoredFile(savedFile);
    }
}
