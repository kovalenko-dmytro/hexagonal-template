package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.specification;

import com.gmail.apach.jenkins_demo.domain.email.wrapper.GetEmailsWrapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.entity.EmailEntity;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailSpecifications {

    public static Specification<EmailEntity> specification(GetEmailsWrapper wrapper) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(wrapper.sendBy())) {
                predicates.add(criteriaBuilder.like(root.get("sendBy"), "%" + wrapper.sendBy() + "%"));
            }

            if (Objects.nonNull(wrapper.sendTo())) {
                predicates.add(criteriaBuilder.like(root.get("sendTo"), "%" + wrapper.sendTo() + "%"));
            }

            if (Objects.nonNull(wrapper.emailType())) {
                predicates.add(criteriaBuilder.like(root.get("emailType"), "%" + wrapper.emailType() + "%"));
            }

            if (Objects.nonNull(wrapper.emailStatus())) {
                predicates.add(criteriaBuilder.like(root.get("emailStatus"), "%" + wrapper.emailStatus() + "%"));
            }

            if (Objects.nonNull(wrapper.dateSendFrom())) {
                final var timeFrom = LocalDateTime.of(wrapper.dateSendFrom(), LocalTime.MIN);
                final var timeTo = Objects.nonNull(wrapper.dateSendTo())
                    ? LocalDateTime.of(wrapper.dateSendTo(), LocalTime.MAX)
                    : LocalDateTime.of(wrapper.dateSendFrom(), LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("sendTime"), timeFrom, timeTo));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
