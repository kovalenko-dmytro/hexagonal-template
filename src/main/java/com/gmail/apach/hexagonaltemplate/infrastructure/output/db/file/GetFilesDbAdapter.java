package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFilesOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.FileCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.mapper.FilePersistenceMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.repository.FileRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.specification.FileSpecifications;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.wrapper.GetFilesFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFilesDbAdapter implements GetFilesOutputPort {

    private final FileRepository fileRepository;
    private final FilePersistenceMapper filePersistenceMapper;

    @Cacheable(
        value = FileCacheConstant.LIST_CACHE_NAME,
        key = FileCacheConstant.Key.SEARCH,
        condition = FileCacheConstant.Condition.SEARCH
    )
    @Override
    public Page<StoredFile> getFiles(GetFilesFilterWrapper wrapper) {
        return fileRepository
            .findAll(FileSpecifications.specification(wrapper), wrapper.toPageable())
            .map(filePersistenceMapper::toStoredFile);
    }
}
