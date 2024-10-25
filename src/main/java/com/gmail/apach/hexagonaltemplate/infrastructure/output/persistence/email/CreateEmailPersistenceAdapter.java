package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.CreateEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.EmailCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.mapper.EmailPersistenceMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateEmailPersistenceAdapter implements CreateEmailOutputPort {

    private final EmailRepository emailRepository;
    private final EmailPersistenceMapper emailPersistenceMapper;

    @CacheEvict(
        value = EmailCacheConstant.LIST_CACHE_NAME,
        allEntries = true
    )
    @RabbitListener(queues = EmailProcessingConfig.CREATE_EMAIL_QUEUE)
    @Override
    public void createEmail(Email email) {
        final var emailEntity = emailPersistenceMapper.toEmailEntity(email);
        emailRepository.save(emailEntity);
    }
}
