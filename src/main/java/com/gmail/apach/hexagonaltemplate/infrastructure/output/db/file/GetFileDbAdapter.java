package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.gmail.apach.hexagonaltemplate.application.port.output.file.GetFileOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.FileCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.AttributeForModel;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.mapper.FileDbMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetFileDbAdapter implements GetFileOutputPort {

    private final FileRepository fileRepository;
    private final FileDbMapper fileDbMapper;
    private final MessageSource messageSource;

    @Cacheable(
        value = FileCacheConstant.CACHE_NAME,
        key = FileCacheConstant.Key.ID
    )
    @Override
    public StoredFile getByFileId(String fileId) {
        return fileRepository
            .findById(fileId)
            .map(fileDbMapper::toStoredFile)
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
