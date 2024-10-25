package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.specification;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.entity.EmailEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.email.wrapper.GetEmailsFilterWrapper;
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

    public static Specification<EmailEntity> specification(GetEmailsFilterWrapper wrapper) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(wrapper.getSendBy())) {
                predicates.add(criteriaBuilder.like(root.get("sendBy"), "%" + wrapper.getSendBy() + "%"));
            }

            if (Objects.nonNull(wrapper.getSendTo())) {
                predicates.add(criteriaBuilder.like(root.get("sendTo"), "%" + wrapper.getSendTo() + "%"));
            }

            if (Objects.nonNull(wrapper.getEmailType())) {
                predicates.add(criteriaBuilder.like(root.get("emailType"), "%" + wrapper.getEmailType() + "%"));
            }

            if (Objects.nonNull(wrapper.getEmailStatus())) {
                predicates.add(criteriaBuilder.like(root.get("emailStatus"), "%" + wrapper.getEmailStatus() + "%"));
            }

            if (Objects.nonNull(wrapper.getDateSendFrom())) {
                final var timeFrom = LocalDateTime.of(wrapper.getDateSendFrom(), LocalTime.MIN);
                final var timeTo = Objects.nonNull(wrapper.getDateSendTo())
                    ? LocalDateTime.of(wrapper.getDateSendTo(), LocalTime.MAX)
                    : LocalDateTime.of(wrapper.getDateSendFrom(), LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("sendTime"), timeFrom, timeTo));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
