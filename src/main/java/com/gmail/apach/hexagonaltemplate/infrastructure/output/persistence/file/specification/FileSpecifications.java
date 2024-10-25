package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.file.specification;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.file.entity.FileEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.file.wrapper.GetFilesFilterWrapper;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FileSpecifications {

    public static Specification<FileEntity> specification(GetFilesFilterWrapper wrapper) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(wrapper.getFileName())) {
                predicates.add(criteriaBuilder.like(root.get("fileName"), "%" + wrapper.getFileName() + "%"));
            }

            if (Objects.nonNull(wrapper.getCreatedFrom())) {
                final var timeFrom = LocalDateTime.of(wrapper.getCreatedFrom(), LocalTime.MIN);
                final var timeTo = Objects.nonNull(wrapper.getCreatedTo())
                    ? LocalDateTime.of(wrapper.getCreatedTo(), LocalTime.MAX)
                    : LocalDateTime.of(wrapper.getCreatedFrom(), LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("created"), timeFrom, timeTo));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
