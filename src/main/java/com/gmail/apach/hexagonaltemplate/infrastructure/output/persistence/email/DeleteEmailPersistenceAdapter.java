package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email;

import com.gmail.apach.hexagonaltemplate.application.output.email.DeleteEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.common.constant.cache.EmailCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteEmailPersistenceAdapter implements DeleteEmailOutputPort {

    private final EmailRepository emailRepository;

    @CacheEvict(
        value = EmailCacheConstant.LIST_CACHE_NAME,
        allEntries = true
    )
    @Override
    public void deleteByEmailId(String emailId) {
        emailRepository.deleteById(emailId);
    }
}
