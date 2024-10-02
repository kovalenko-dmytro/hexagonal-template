package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email;

import com.gmail.apach.hexagonaltemplate.application.output.email.GetEmailsOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.domain.email.wrapper.GetEmailsWrapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.EmailCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.util.PageableUtil;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.mapper.EmailPersistenceMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository.EmailRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.specification.EmailSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetEmailsPersistenceAdapter implements GetEmailsOutputPort {

    private final EmailRepository emailRepository;
    private final EmailPersistenceMapper emailPersistenceMapper;
    private final PageableUtil pageableUtil;

    @Cacheable(
        value = EmailCacheConstant.LIST_CACHE_NAME,
        key = EmailCacheConstant.Key.SEARCH,
        condition = EmailCacheConstant.Condition.SEARCH
    )
    @Override
    public Page<Email> getEmails(GetEmailsWrapper wrapper) {
        final var pageable = pageableUtil.obtainPageable(wrapper.page(), wrapper.size(), wrapper.sort());
        return emailRepository
            .findAll(EmailSpecifications.specification(wrapper), pageable)
            .map(emailPersistenceMapper::toEmail);
    }
}
