package com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.mapper;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.CurrentUserResponse;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.user.dto.CreateUserRequest;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.user.dto.UserResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface UserRESTMapper {

    CurrentUserResponse toCurrentUserResponse(User user);

    @Mapping(source = "roleType", target = "roles", qualifiedByName = "roleTypeToRoles")
    User toUser(CreateUserRequest request);

    UserResponse toUserResponse(User user);

    default Set<RoleType> toRoles(Set<Role> roles) {
        return CollectionUtils.emptyIfNull(roles).stream()
            .map(Role::getRole)
            .collect(Collectors.toSet());
    }

    @Named("roleTypeToRoles")
    default Set<Role> roleTypeToRoles(RoleType roleType) {
        if (Objects.isNull(roleType)) {
            return Collections.emptySet();
        }
        switch (roleType) {
            case MANAGER -> {
                return Set.of(
                    Role.builder().role(RoleType.MANAGER).build(),
                    Role.builder().role(RoleType.USER).build()
                );
            }
            case USER -> {
                return Set.of(Role.builder().role(RoleType.USER).build());
            }
            default -> {
                return Collections.emptySet();
            }
        }
    }
}
