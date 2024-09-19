package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file;

import com.gmail.apach.jenkins_demo.application.output.file.GetFileOutputPort;
import com.gmail.apach.jenkins_demo.common.constant.cache.FileCacheConstant;
import com.gmail.apach.jenkins_demo.common.constant.message.AttributeForModel;
import com.gmail.apach.jenkins_demo.common.constant.message.Error;
import com.gmail.apach.jenkins_demo.common.exception.ResourceNotFoundException;
import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.mapper.FilePersistenceMapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFilePersistenceAdapter implements GetFileOutputPort {

    private final FileRepository fileRepository;
    private final FilePersistenceMapper filePersistenceMapper;
    private final MessageSource messageSource;

    @Cacheable(
        value = FileCacheConstant.CACHE_NAME,
        key = FileCacheConstant.Key.ID
    )
    @Override
    public StoredFile getByFileId(String fileId) {
        return fileRepository
            .findById(fileId)
            .map(filePersistenceMapper::toStoredFile)
            .orElseThrow(() -> new ResourceNotFoundException(getByFileIdErrorMessage(fileId)));
    }

    private String getByFileIdErrorMessage(String userId) {
        return messageSource.getMessage(
            Error.ENTITY_NOT_FOUND.getKey(),
            getByFileIdErrorArgs(userId),
            LocaleContextHolder.getLocale());
    }

    private Object[] getByFileIdErrorArgs(String userId) {
        return new Object[]{
            AttributeForModel.STORED_FILE.getName(),
            AttributeForModel.Field.ID.getFieldName(),
            userId
        };
    }
}
