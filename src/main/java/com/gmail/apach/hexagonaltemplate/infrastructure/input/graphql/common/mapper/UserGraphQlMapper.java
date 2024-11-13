package com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.common.mapper;

import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.wrapper.AuthTokenDetails;
import com.gmail.apach.hexagonaltemplate.infrastructure.input.graphql.user.dto.*;
import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.*;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserGraphQlMapper {

    CurrentUserOutputType toCurrentUserOutputType(User user);

    @Mapping(source = "roleType", target = "roles", qualifiedByName = "roleTypeToRoles")
    User toUser(CreateUserInputType input);

    UserOutputType toUserOutputType(User user);

    @Mapping(source = "userId", target = "userId")
    @Mapping(source = "input.firstName", target = "firstName")
    @Mapping(source = "input.lastName", target = "lastName")
    @Mapping(source = "input.email", target = "email")
    @Mapping(source = "input.enabled", target = "enabled")
    @Mapping(source = "input.roleType", target = "roles", qualifiedByName = "roleTypeToRoles")
    User toUser(String userId, UpdateUserInputType input);

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

    SignInOutputType toSignInOutputType(AuthTokenDetails authTokenDetails);
}
