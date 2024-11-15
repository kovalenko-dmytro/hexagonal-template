package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.GetEmailsOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.EmailCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.mapper.EmailDbMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.repository.EmailRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.specification.EmailSpecifications;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.wrapper.GetEmailsFilterWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetEmailsDbAdapter implements GetEmailsOutputPort {

    private final EmailRepository emailRepository;
    private final EmailDbMapper emailDbMapper;

    @Cacheable(
        value = EmailCacheConstant.LIST_CACHE_NAME,
        key = EmailCacheConstant.Key.SEARCH,
        condition = EmailCacheConstant.Condition.SEARCH
    )
    @Override
    public Page<Email> getEmails(GetEmailsFilterWrapper wrapper) {
        return emailRepository
            .findAll(EmailSpecifications.specification(wrapper), wrapper.toPageable())
            .map(emailDbMapper::toEmail);
    }
}
