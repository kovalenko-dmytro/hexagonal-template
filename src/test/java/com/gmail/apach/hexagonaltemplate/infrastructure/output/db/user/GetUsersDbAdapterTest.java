package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.domain.user.model.User;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.user.wrapper.GetUsersFilterWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class GetUsersDbAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private GetUsersDbAdapter getUsersDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_withoutFilter() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .page(1)
            .size(5)
            .isAdmin(true)
            .sort(new String[]{"created"})
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(7, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertTrue(actual.getContent().stream().anyMatch(User::isAdmin));
        assertTrue(ArrayUtils.isSorted(
            actual.getContent().toArray(new User[0]), Comparator.comparing(User::getCreated)));
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Manager_withoutFilter() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .page(1)
            .size(6)
            .isAdmin(false)
            .sort(new String[]{"created"})
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(6, actual.getContent().size());
        assertEquals(6, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
        assertTrue(actual.getContent().stream().noneMatch(User::isAdmin));
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsername() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .username("user")
            .page(1)
            .size(5)
            .isAdmin(true)
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(4, actual.getContent().size());
        assertEquals(4, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsernameAndFirsName() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .username("user")
            .firstName("some")
            .page(1)
            .size(5)
            .isAdmin(true)
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(3, actual.getContent().size());
        assertEquals(3, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsernameAndLastName() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .username("user")
            .lastName("some")
            .page(1)
            .size(5)
            .isAdmin(true)
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
        assertEquals(2, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsernameAndLastNameAndEmail() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .username("user")
            .lastName("some")
            .email("gmail")
            .page(1)
            .size(5)
            .isAdmin(true)
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsernameAndEnabled() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .username("user")
            .firstName("some")
            .enabled(false)
            .page(1)
            .size(5)
            .isAdmin(true)
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_searchByDateFrom() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .createdFrom(LocalDate.of(2024, 8, 24))
            .page(1)
            .size(5)
            .isAdmin(true)
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_searchByDateFromAndDateTo() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .createdFrom(LocalDate.of(2024, 8, 20))
            .createdTo(LocalDate.of(2024, 8, 25))
            .page(1)
            .size(5)
            .isAdmin(true)
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(6, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/user/get_users_setup.yml")
    void getUsers_Admin_sortByCreatedDesc() {
        final var filterWrapper = GetUsersFilterWrapper.builder()
            .sort(new String[]{"created desc"})
            .page(1)
            .size(5)
            .isAdmin(true)
            .build();

        final var actual = getUsersDbAdapter.getUsers(filterWrapper);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(7, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertTrue(ArrayUtils.isSorted(
            actual.getContent().toArray(new User[0]), Comparator.comparing(User::getCreated).reversed()));
    }
}