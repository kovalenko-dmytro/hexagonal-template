package com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.repository;

import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.persistence.user.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {

    Optional<RoleEntity> findByRole(RoleType roleType);
    Set<RoleEntity> findByRoleIn(List<RoleType> roleTypes);
}
