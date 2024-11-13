package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.GetEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.AttributeForModel;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.message.constant.Error;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.exception.ResourceNotFoundException;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.mapper.EmailPersistenceMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetEmailDbAdapter implements GetEmailOutputPort {

    private final EmailRepository emailRepository;
    private final EmailPersistenceMapper emailPersistenceMapper;
    private final MessageSource messageSource;

    @Override
    public Email getByEmailId(String emailId) {
        return emailRepository
            .findById(emailId)
            .map(emailPersistenceMapper::toEmail)
            .orElseThrow(() -> new ResourceNotFoundException(getByEmailIdErrorMessage(emailId)));
    }

    private String getByEmailIdErrorMessage(String userId) {
        return messageSource.getMessage(
            Error.ENTITY_NOT_FOUND.getKey(),
            getByEmailIdErrorArgs(userId),
            LocaleContextHolder.getLocale());
    }

    private Object[] getByEmailIdErrorArgs(String userId) {
        return new Object[]{
            AttributeForModel.EMAIL.getName(),
            AttributeForModel.Field.ID.getFieldName(),
            userId
        };
    }
}
