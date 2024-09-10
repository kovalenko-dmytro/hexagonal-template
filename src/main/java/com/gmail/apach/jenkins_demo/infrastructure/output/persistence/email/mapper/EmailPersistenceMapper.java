package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.mapper;

import com.gmail.apach.jenkins_demo.domain.email.model.Email;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.entity.EmailEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface EmailPersistenceMapper {

    Email toEmail(EmailEntity emailEntity);

    EmailEntity toEmailEntity(Email email);
}
