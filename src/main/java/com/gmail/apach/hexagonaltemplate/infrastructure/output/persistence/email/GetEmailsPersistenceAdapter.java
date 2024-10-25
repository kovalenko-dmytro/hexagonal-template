package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.GetEmailsOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.EmailCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.mapper.EmailPersistenceMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository.EmailRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.specification.EmailSpecifications;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.wrapper.GetEmailsFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetEmailsPersistenceAdapter implements GetEmailsOutputPort {

    private final EmailRepository emailRepository;
    private final EmailPersistenceMapper emailPersistenceMapper;

    @Cacheable(
        value = EmailCacheConstant.LIST_CACHE_NAME,
        key = EmailCacheConstant.Key.SEARCH,
        condition = EmailCacheConstant.Condition.SEARCH
    )
    @Override
    public Page<Email> getEmails(GetEmailsFilterWrapper wrapper) {
        return emailRepository
            .findAll(EmailSpecifications.specification(wrapper), wrapper.toPageable())
            .map(emailPersistenceMapper::toEmail);
    }
}
