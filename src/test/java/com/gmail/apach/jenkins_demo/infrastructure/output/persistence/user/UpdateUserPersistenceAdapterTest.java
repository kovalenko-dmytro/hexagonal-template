package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.domain.common.constant.RoleType;
import com.gmail.apach.jenkins_demo.domain.user.model.Role;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class UpdateUserPersistenceAdapterTest extends AbstractIntegrationTest {

    private static final String USER_ID = "5b8d68c8-2f28-4b53-ac5a-2db586512448";

    @Autowired
    private UpdateUserPersistenceAdapter updateUserPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/update_user_setup.yml")
    @ExpectedDataSet(
        value = "datasets/infrastructure/output/persistence/user/update_user_expected.yml",
        ignoreCols = {"created"})
    void update() {
        final var expected = User.builder()
            .userId(USER_ID)
            .username("user")
            .password("password")
            .firstName("first_name_new")
            .lastName("last_name_new")
            .created(LocalDateTime.now())
            .createdBy("admin")
            .email("email_new@email")
            .roles(Set.of(
                Role.builder().role(RoleType.USER).build(),
                Role.builder().role(RoleType.MANAGER).build()))
            .enabled(false)
            .build();

        final var actual = updateUserPersistenceAdapter.update(expected);

        assertNotNull(actual);
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getEnabled(), actual.getEnabled());
        assertFalse(actual.getRoles().isEmpty());
        final var expectedRoles = expected.getRoles().stream().map(Role::getRole).collect(Collectors.toSet());
        final var actualRoles = actual.getRoles().stream().map(Role::getRole).collect(Collectors.toSet());
        assertTrue(expectedRoles.containsAll(actualRoles));
    }
}