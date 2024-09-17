package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file;

import com.gmail.apach.jenkins_demo.application.output.file.GetFilesOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.cache.FileCacheConstant;
import com.gmail.apach.jenkins_demo.common.util.PageableUtil;
import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import com.gmail.apach.jenkins_demo.domain.file.wrapper.GetFilesRequestWrapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.mapper.FilePersistenceMapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.repository.FileRepository;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.specification.FileSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFilesPersistenceAdapter implements GetFilesOutputPort {

    private final FileRepository fileRepository;
    private final FilePersistenceMapper filePersistenceMapper;
    private final PageableUtil pageableUtil;

    @Cacheable(
        value = FileCacheConstant.LIST_CACHE_NAME,
        key = FileCacheConstant.Key.SEARCH,
        condition = FileCacheConstant.Condition.SEARCH
    )
    @Override
    public Page<StoredFile> getFiles(GetFilesRequestWrapper wrapper) {
        final var pageable = pageableUtil.obtainPageable(wrapper.page(), wrapper.size(), wrapper.sort());
        return fileRepository
            .findAll(FileSpecifications.specification(wrapper), pageable)
            .map(filePersistenceMapper::toStoredFile);
    }
}
