package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.user;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.common.constant.cache.UserCacheConstant;
import com.gmail.apach.jenkins_demo.common.dto.CurrentUserContext;
import com.gmail.apach.jenkins_demo.data.AuthoritiesTestData;
import com.gmail.apach.jenkins_demo.data.UsersTestData;
import com.gmail.apach.jenkins_demo.domain.user.model.User;
import com.gmail.apach.jenkins_demo.domain.user.wrapper.GetUsersRequestWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

class UserCacheTest extends AbstractIntegrationTest {

    private static final String USER_ID = "5a8d68c8-2f28-4b53-ac5a-2db586512440";

    @Autowired
    private CreateUserPersistenceAdapter createUserPersistenceAdapter;
    @Autowired
    private DeleteUserPersistenceAdapter deleteUserPersistenceAdapter;
    @Autowired
    private GetUserPersistenceAdapter getUserPersistenceAdapter;
    @Autowired
    private GetUsersPersistenceAdapter getUsersPersistenceAdapter;
    @Autowired
    private UpdateUserPersistenceAdapter updateUserPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void createUser_evictUsersCacheLists_success() {
        getUsersCache().ifPresent(usersCache -> assertTrue(usersCache.isEmpty()));

        final var requestWrapper = getUsersRequestWrapper();
        final var currentUserContext = getContext();

        final var users = getUsersPersistenceAdapter.getUsers(requestWrapper, currentUserContext);

        assertNotNull(users);
        assertTrue(CollectionUtils.isNotEmpty(users.getContent()));
        getUsersCache().ifPresent(usersCache -> assertFalse(usersCache.isEmpty()));

        createUserPersistenceAdapter.createUser(UsersTestData.userCreatedByAdmin());

        getUsersCache().ifPresent(usersCache -> assertTrue(usersCache.isEmpty()));
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_user.yml")
    void deleteByUserId_evictUsersCacheListsAndUserCache_success() {
        assertTrue(getCachedUser(USER_ID).isEmpty());
        getUsersCache().ifPresent(usersCache -> assertTrue(usersCache.isEmpty()));

        final var userFromDB = getUserPersistenceAdapter.getByUserId(USER_ID);
        final var cachedUser = getCachedUser(USER_ID);

        assertFalse(cachedUser.isEmpty());
        assertEquals(userFromDB, cachedUser.get());

        final var requestWrapper = getUsersRequestWrapper();
        final var currentUserContext = getContext();

        final var users = getUsersPersistenceAdapter.getUsers(requestWrapper, currentUserContext);

        assertNotNull(users);
        getUsersCache().ifPresent(usersCache -> assertFalse(usersCache.isEmpty()));

        deleteUserPersistenceAdapter.deleteByUserId(USER_ID);

        assertTrue(getCachedUser(USER_ID).isEmpty());
        getUsersCache().ifPresent(usersCache -> assertTrue(usersCache.isEmpty()));
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_user.yml")
    void getByUserId_addUserCache_success() {
        assertTrue(getCachedUser(USER_ID).isEmpty());

        final var userFromDB = getUserPersistenceAdapter.getByUserId(USER_ID);
        final var cachedUser = getCachedUser(USER_ID);

        assertFalse(cachedUser.isEmpty());
        assertEquals(userFromDB, cachedUser.get());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void getUsers_addUsersCache_success() {
        getUsersCache().ifPresent(usersCache -> assertTrue(usersCache.isEmpty()));

        final var requestWrapper = getUsersRequestWrapper();
        final var currentUserContext = CurrentUserContext.builder()
            .username("admin")
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();

        final var users = getUsersPersistenceAdapter.getUsers(requestWrapper, currentUserContext);

        getUsersCache().ifPresent(usersCache -> {
            assertFalse(usersCache.isEmpty());
            var cachedPage = (Page) usersCache.get(
                List.of(
                    requestWrapper.page(),
                    requestWrapper.size(),
                    currentUserContext.username()));
            assertEquals(users.getContent().size(), cachedPage.getContent().size());
        });
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/user/get_users_setup.yml")
    void updateUser_evictUsersCacheListsAndPutUserCache_success() {
        final var userId = "5b8d68c8-2f28-4b53-ac5a-2db586512448";

        assertTrue(getCachedUser(USER_ID).isEmpty());
        getUsersCache().ifPresent(usersCache -> assertTrue(usersCache.isEmpty()));

        final var requestWrapper = getUsersRequestWrapper();
        final var currentUserContext = getContext();

        getUsersPersistenceAdapter.getUsers(requestWrapper, currentUserContext);
        getUsersCache().ifPresent(usersCache -> assertFalse(usersCache.isEmpty()));

        final var userFromDB = getUserPersistenceAdapter.getByUserId(userId);
        final var cachedUser = getCachedUser(userId);
        assertFalse(cachedUser.isEmpty());
        assertEquals(userFromDB, cachedUser.get());

        userFromDB.setFirstName("new_firstName");
        userFromDB.setEnabled(false);

        final var updatedUser = updateUserPersistenceAdapter.update(userFromDB);

        assertNotNull(updatedUser);
        getUsersCache().ifPresent(usersCache -> assertTrue(usersCache.isEmpty()));

        final var updatedCachedUser = getCachedUser(userId);

        assertFalse(updatedCachedUser.isEmpty());
        assertEquals(updatedUser, updatedCachedUser.get());
    }

    private static GetUsersRequestWrapper getUsersRequestWrapper() {
        return GetUsersRequestWrapper.builder()
            .page(1)
            .size(5)
            .sort(new String[]{"created"})
            .build();
    }

    private static CurrentUserContext getContext() {
        return CurrentUserContext.builder()
            .authorities(AuthoritiesTestData.adminAuthorities())
            .build();
    }

    private Optional<ConcurrentMap<Object, Object>> getUsersCache() {
        return Optional
            .ofNullable(cacheManager.getCache(UserCacheConstant.LIST_CACHE_NAME))
            .map(cache -> ((ConcurrentMapCache) cache).getNativeCache());
    }

    private Optional<User> getCachedUser(String userId) {
        return Optional.ofNullable(cacheManager.getCache(UserCacheConstant.CACHE_NAME))
            .map(cache -> cache.get(userId, User.class));
    }
}