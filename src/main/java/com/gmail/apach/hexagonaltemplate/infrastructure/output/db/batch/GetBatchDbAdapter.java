package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch;

import com.gmail.apach.hexagonaltemplate.application.port.output.batch.GetBatchOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.batch.model.ExecutedBatch;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.AttributeForModel;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.mapper.BatchDbMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.batch.repository.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetBatchDbAdapter implements GetBatchOutputPort {

    private final BatchRepository batchRepository;
    private final BatchDbMapper batchDbMapper;
    private final MessageSource messageSource;

    @Override
    public ExecutedBatch get(String batchId) {
        final var batchView = batchRepository
            .get(batchId)
            .orElseThrow(() -> new ResourceNotFoundException(buildGetBatchErrorMessage(batchId)));
        return batchDbMapper.toExecutedBatch(batchView);
    }

    private String buildGetBatchErrorMessage(String batchId) {
        return messageSource.getMessage(
            Error.ENTITY_NOT_FOUND.getKey(),
            new Object[]{AttributeForModel.BATCH.getName(), AttributeForModel.Field.ID.getFieldName(), batchId},
            LocaleContextHolder.getLocale()
        );
    }
}
