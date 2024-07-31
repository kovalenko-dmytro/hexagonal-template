package com.gmail.apach.jenkins_demo.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Objects;
import java.util.UUID;

@Converter(autoApply = true)
public class UUIDAttributeConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(UUID uuidId) {
        return Objects.isNull(uuidId) ? null : uuidId.toString();
    }

    @Override
    public UUID convertToEntityAttribute(String id) {
        return Objects.isNull(id) ? null : UUID.fromString(id);
    }
}
