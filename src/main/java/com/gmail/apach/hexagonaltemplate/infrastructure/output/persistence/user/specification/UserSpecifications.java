package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.specification;

import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.entity.UserEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.wrapper.GetUsersFilterWrapper;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserSpecifications {

    public static Specification<UserEntity> specification(GetUsersFilterWrapper wrapper) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(wrapper.getUsername())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + wrapper.getUsername() + "%"));
            }

            if (Objects.nonNull(wrapper.getFirstName())) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + wrapper.getFirstName() + "%"));
            }

            if (Objects.nonNull(wrapper.getLastName())) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + wrapper.getLastName() + "%"));
            }

            if (Objects.nonNull(wrapper.getEmail())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + wrapper.getEmail() + "%"));
            }

            if (Objects.nonNull(wrapper.getCreatedBy())) {
                predicates.add(criteriaBuilder.like(root.get("createdBy"), "%" + wrapper.getCreatedBy() + "%"));
            }

            if (Objects.nonNull(wrapper.getEnabled())) {
                predicates.add(criteriaBuilder.equal(root.get("enabled"), wrapper.getEnabled()));
            }

            if (Objects.nonNull(wrapper.getCreatedFrom())) {
                final var timeFrom = LocalDateTime.of(wrapper.getCreatedFrom(), LocalTime.MIN);
                final var timeTo = Objects.nonNull(wrapper.getCreatedTo())
                    ? LocalDateTime.of(wrapper.getCreatedTo(), LocalTime.MAX)
                    : LocalDateTime.of(wrapper.getCreatedFrom(), LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("created"), timeFrom, timeTo));
            }

            if (!wrapper.isAdmin()) {
                predicates.add(criteriaBuilder.notEqual(root.get("isAdmin"), true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
