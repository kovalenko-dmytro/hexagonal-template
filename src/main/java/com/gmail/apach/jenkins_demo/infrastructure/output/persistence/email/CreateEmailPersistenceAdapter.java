package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email;

import com.gmail.apach.jenkins_demo.application.output.email.CreateEmailOutputPort;
import com.gmail.apach.jenkins_demo.domain.email.model.Email;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.mapper.EmailPersistenceMapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateEmailPersistenceAdapter implements CreateEmailOutputPort {

    private final EmailRepository emailRepository;
    private final EmailPersistenceMapper emailPersistenceMapper;

    @Override
    public Email createEmail(Email email) {
        final var emailEntity = emailPersistenceMapper.toEmailEntity(email);
        final var savedEmail = emailRepository.save(emailEntity);
        return emailPersistenceMapper.toEmail(savedEmail);
    }
}
