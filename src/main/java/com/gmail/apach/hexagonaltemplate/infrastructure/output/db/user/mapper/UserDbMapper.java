package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.mapper;

import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity.RoleEntity;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity.UserEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;


@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserDbMapper {
    UserEntity toUserEntity(User user);
    User toUser(UserEntity userEntity);
    RoleEntity toRoleEntity(Role role);
    Role toRole(RoleEntity roleEntity);
}
