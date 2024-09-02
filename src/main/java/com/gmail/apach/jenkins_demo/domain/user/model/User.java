package com.gmail.apach.jenkins_demo.domain.user.model;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

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
}
