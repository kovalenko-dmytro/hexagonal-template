package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.specification;

import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersSearchSortPageWrapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.entity.UserEntity;
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

    public static Specification<UserEntity> specification(GetUsersSearchSortPageWrapper wrapper, boolean isAdmin) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final var predicates = new ArrayList<Predicate>();

            if (Objects.nonNull(wrapper.username())) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + wrapper.username() + "%"));
            }

            if (Objects.nonNull(wrapper.firstName())) {
                predicates.add(criteriaBuilder.like(root.get("firstName"), "%" + wrapper.firstName() + "%"));
            }

            if (Objects.nonNull(wrapper.lastName())) {
                predicates.add(criteriaBuilder.like(root.get("lastName"), "%" + wrapper.lastName() + "%"));
            }

            if (Objects.nonNull(wrapper.email())) {
                predicates.add(criteriaBuilder.like(root.get("email"), "%" + wrapper.email() + "%"));
            }

            if (Objects.nonNull(wrapper.createdBy())) {
                predicates.add(criteriaBuilder.like(root.get("createdBy"), "%" + wrapper.createdBy() + "%"));
            }

            if (Objects.nonNull(wrapper.enabled())) {
                predicates.add(criteriaBuilder.equal(root.get("enabled"), wrapper.enabled()));
            }

            if (Objects.nonNull(wrapper.createdFrom())) {
                final var timeFrom = LocalDateTime.of(wrapper.createdFrom(), LocalTime.MIN);
                final var timeTo = Objects.nonNull(wrapper.createdTo())
                    ? LocalDateTime.of(wrapper.createdTo(), LocalTime.MAX)
                    : LocalDateTime.of(wrapper.createdFrom(), LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("created"), timeFrom, timeTo));
            }

            if (!isAdmin) {
                predicates.add(criteriaBuilder.notEqual(root.get("isAdmin"), true));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
