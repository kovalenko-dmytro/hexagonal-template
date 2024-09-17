package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.specification;

import com.gmail.apach.jenkins_demo.domain.file.wrapper.GetFilesRequestWrapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file.entity.FileEntity;
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

    public static Specification<FileEntity> specification(GetFilesRequestWrapper wrapper) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(wrapper.fileName())) {
                predicates.add(criteriaBuilder.like(root.get("fileName"), "%" + wrapper.fileName() + "%"));
            }

            if (Objects.nonNull(wrapper.createdFrom())) {
                final var timeFrom = LocalDateTime.of(wrapper.createdFrom(), LocalTime.MIN);
                final var timeTo = Objects.nonNull(wrapper.createdTo())
                    ? LocalDateTime.of(wrapper.createdTo(), LocalTime.MAX)
                    : LocalDateTime.of(wrapper.createdFrom(), LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("created"), timeFrom, timeTo));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
