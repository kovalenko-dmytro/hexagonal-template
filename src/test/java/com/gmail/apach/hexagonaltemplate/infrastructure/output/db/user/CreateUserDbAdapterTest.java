package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.UsersTestData;
import com.gmail.apach.hexagonaltemplate.domain.user.model.Role;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.domain.user.vo.RoleType;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserDbAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private CreateUserDbAdapter createUserDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/user/create_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/output/db/user/create_user_expected.yml",
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

        final var actual = createUserDbAdapter.create(expected);

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
    @DataSet("datasets/infrastructure/output/db/user/create_manager_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/output/db/user/create_manager_expected.yml",
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

        final var actual = createUserDbAdapter.create(expected);

        assertNotNull(actual);
        assertNotNull(actual.getUserId());
        assertTrue(CollectionUtils.isNotEmpty(actual.getRoles()));
        assertTrue(
            actual.getRoles().stream()
                .map(Role::getRole)
                .collect(Collectors.toSet())
                .containsAll(Set.of(RoleType.USER, RoleType.MANAGER)));
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/create_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/output/db/user/create_users_expected.yml",
        ignoreCols = {"user_", "created", "password"},
        orderBy = "email")
    void createUsers_success() {
        final var users = UsersTestData.usersForInsert();
        createUserDbAdapter.create(users);
    }
}