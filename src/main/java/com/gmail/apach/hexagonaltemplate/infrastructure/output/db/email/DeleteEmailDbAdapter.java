package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.DeleteEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.EmailCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteEmailDbAdapter implements DeleteEmailOutputPort {

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
