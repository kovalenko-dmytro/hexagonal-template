package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email;

import com.gmail.apach.hexagonaltemplate.application.port.output.email.CreateEmailOutputPort;
import com.gmail.apach.hexagonaltemplate.domain.email.model.Email;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.EmailCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.mq.process.EmailProcessingConfig;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.mapper.EmailDbMapper;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateEmailDbAdapter implements CreateEmailOutputPort {

    private final EmailRepository emailRepository;
    private final EmailDbMapper emailDbMapper;

    @CacheEvict(
        value = EmailCacheConstant.LIST_CACHE_NAME,
        allEntries = true
    )
    @RabbitListener(queues = EmailProcessingConfig.SAVE_EMAIL_QUEUE)
    @Override
    public void create(Email email) {
        final var emailEntity = emailDbMapper.toEmailEntity(email);
        emailRepository.save(emailEntity);
    }
}
