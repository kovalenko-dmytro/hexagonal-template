package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.entity;

import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
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
