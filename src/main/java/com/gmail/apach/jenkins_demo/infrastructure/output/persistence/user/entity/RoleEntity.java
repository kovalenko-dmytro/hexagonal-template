package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user.entity;

import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Table(name = "roles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RoleEntity {

    @Id
    @UuidGenerator
    @Column(name = "role_", length = 36, unique = true, nullable = false)
    private String roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleType role;
}
