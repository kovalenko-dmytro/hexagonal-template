package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserPersistenceAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private CreateUserPersistenceAdapter createUserPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/create_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/output/persistence/user/create_user_expected.yml",
        ignoreCols = {"user_", "created"})
    void createUser_success() {
        final var expected = User.builder()
            .username("username")
            .password("password")
            .firstName("first_name")
            .lastName("last_name")
            .created(LocalDateTime.now())
            .createdBy("admin")
            .email("email@email")
            .roles(Set.of(Role.builder().role(RoleType.USER).build()))
            .enabled(true)
            .build();

        final var actual = createUserPersistenceAdapter.createUser(expected);

        assertNotNull(actual);
        assertNotNull(actual.getUserId());
        assertTrue(CollectionUtils.isNotEmpty(actual.getRoles()));
        assertEquals(1, actual.getRoles().size());
        assertTrue(
            actual.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet())
                .contains(RoleType.USER));
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/create_manager_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/output/persistence/user/create_manager_expected.yml",
        ignoreCols = {"user_", "created"})
    void createManager_success() {
        final var expected = User.builder()
            .username("manager")
            .password("password")
            .firstName("first_name")
            .lastName("last_name")
            .created(LocalDateTime.now())
            .createdBy("admin")
            .email("email@email")
            .roles(Set.of(
                Role.builder().role(RoleType.MANAGER).build(),
                Role.builder().role(RoleType.USER).build()))
            .enabled(true)
            .build();

        final var actual = createUserPersistenceAdapter.createUser(expected);

        assertNotNull(actual);
        assertNotNull(actual.getUserId());
        assertTrue(CollectionUtils.isNotEmpty(actual.getRoles()));
        assertTrue(
            actual.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet())
                .containsAll(Set.of(RoleType.USER, RoleType.MANAGER)));
    }
}