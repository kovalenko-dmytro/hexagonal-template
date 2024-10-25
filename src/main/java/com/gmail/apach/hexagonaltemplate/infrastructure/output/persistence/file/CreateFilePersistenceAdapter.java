package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.CreateFileOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.FileCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.file.mapper.FilePersistenceMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateFilePersistenceAdapter implements CreateFileOutputPort {

    private final FileRepository fileRepository;
    private final FilePersistenceMapper filePersistenceMapper;

    @CacheEvict(
        value = FileCacheConstant.LIST_CACHE_NAME,
        allEntries = true
    )
    @Override
    public StoredFile createFile(StoredFile file) {
        final var fileEntity = filePersistenceMapper.toFileEntity(file);
        final var savedFile = fileRepository.save(fileEntity);
        return filePersistenceMapper.toStoredFile(savedFile);
    }
}
