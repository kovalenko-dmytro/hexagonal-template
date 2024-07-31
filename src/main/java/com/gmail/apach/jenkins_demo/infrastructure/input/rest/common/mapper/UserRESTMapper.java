package com.gmail.apach.jenkins_demo.infrastructure.input.rest.common.mapper;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.infrastructure.input.rest.security.dto.CurrentUserResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
public interface UserRESTMapper {

    CurrentUserResponse toCurrentUserResponse(User user);

    default Set<RoleType> toRoles(Set<Role> roles) {
        return CollectionUtils.emptyIfNull(roles).stream()
            .map(Role::getRole)
            .collect(Collectors.toSet());
    }
}
