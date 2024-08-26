package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersSearchSortPageWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.*;

class GetUsersPersistenceAdapterTest extends AbstractIntegrationTest {

    @Autowired
    private GetUsersPersistenceAdapter getUsersPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_withoutFilter() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder().page(1).size(5).build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

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
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Manager_withoutFilter() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder().page(1).size(6).build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, false);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(6, actual.getContent().size());
        assertEquals(6, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
        assertTrue(actual.getContent().stream().noneMatch(User::isAdmin));
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsername() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder()
            .username("user")
            .page(1)
            .size(5)
            .build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(4, actual.getContent().size());
        assertEquals(4, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsernameAndFirsName() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder()
            .username("user")
            .firstName("some")
            .page(1)
            .size(5)
            .build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(3, actual.getContent().size());
        assertEquals(3, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsernameAndLastName() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder()
            .username("user")
            .lastName("some")
            .page(1)
            .size(5)
            .build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(2, actual.getContent().size());
        assertEquals(2, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsernameAndLastNameAndEmail() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder()
            .username("user")
            .lastName("some")
            .email("gmail")
            .page(1)
            .size(5)
            .build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_searchByUsernameAndEnabled() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder()
            .username("user")
            .firstName("some")
            .enabled(false)
            .page(1)
            .size(5)
            .build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_searchByDateFrom() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder()
            .createdFrom(LocalDate.of(2024, 8, 24))
            .page(1)
            .size(5)
            .build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(1, actual.getContent().size());
        assertEquals(1, actual.getTotalElements());
        assertEquals(1, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_searchByDateFromAndDateTo() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder()
            .createdFrom(LocalDate.of(2024, 8, 20))
            .createdTo(LocalDate.of(2024, 8, 25))
            .page(1)
            .size(5)
            .build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(6, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_Admin_sortByCreatedDesc() {
        final var requestWrapper = GetUsersSearchSortPageWrapper.builder()
            .sort(new String[]{"created desc"})
            .page(1)
            .size(5)
            .build();

        final var actual = getUsersPersistenceAdapter.getUsers(requestWrapper, true);

        assertNotNull(actual);
        assertTrue(CollectionUtils.isNotEmpty(actual.getContent()));
        assertEquals(5, actual.getContent().size());
        assertEquals(7, actual.getTotalElements());
        assertEquals(2, actual.getTotalPages());
        assertTrue(ArrayUtils.isSorted(
            actual.getContent().toArray(new User[0]), Comparator.comparing(User::getCreated).reversed()));
    }
}