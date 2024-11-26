package com.gmail.apach.hexagonaltemplate.domain.user.model;

import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.constant.CommonConstant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 1234567L;

    private String userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean enabled;
    private LocalDateTime created;
    private String createdBy;
    private Set<Role> roles;

    public boolean isSelf(User currentPrincipal) {
        if (Objects.isNull(username)) {
            throw new IllegalStateException();
        }
        return username.contentEquals(currentPrincipal.getUsername());
    }

    public boolean isAdmin() {
        return CollectionUtils.isNotEmpty(roles)
            && roles.stream().anyMatch(role -> role.getRole().equals(RoleType.ADMIN));
    }

    public boolean isManager() {
        return CollectionUtils.isNotEmpty(roles)
            && roles.stream().noneMatch(role -> role.getRole().equals(RoleType.ADMIN))
            && roles.stream().anyMatch(role -> role.getRole().equals(RoleType.MANAGER));
    }

    public boolean isUser() {
        return !isAdmin() && !isManager();
    }

    public boolean rolesExist() {
        return CollectionUtils.isNotEmpty(roles);
    }

    public boolean firstNameNonBlank() {
        return StringUtils.isNoneBlank(firstName);
    }

    public boolean lastNameNonBlank() {
        return StringUtils.isNoneBlank(lastName);
    }

    public boolean emailNonBlank() {
        return StringUtils.isNoneBlank(email);
    }

    public boolean enabledDefined() {
        return Objects.nonNull(enabled);
    }

    public Set<RoleType> getRoleTypes() {
        if (!rolesExist()) {
            throw new IllegalStateException();
        }
        return roles.stream().map(Role::getRole).collect(Collectors.toSet());
    }

    public String rolesJoiningToString() {
        return getRoleTypes().stream()
            .map(RoleType::name)
            .collect(Collectors.joining(CommonConstant.COMMA.getValue()));
    }
}
