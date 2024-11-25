package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email;

import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.EmailsTestData;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.EmailCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.repository.EmailRepository;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.email.wrapper.GetEmailsFilterWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

class EmailCacheTest extends AbstractIntegrationTest {

    private static final String SORT_ORDER = "sendBy";
    private static final int PAGE = 1;
    private static final int SIZE = 5;

    @Autowired
    private GetEmailsDbAdapter getEmailsDbAdapter;
    @Autowired
    private CreateEmailDbAdapter createEmailDbAdapter;
    @Autowired
    private DeleteEmailDbAdapter deleteEmailDbAdapter;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    void createEmail_evictEmailCacheLists_success() {
        emailRepository.saveAll(EmailsTestData.emails());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = GetEmailsFilterWrapper.builder()
            .page(PAGE)
            .size(SIZE)
            .sort(new String[]{SORT_ORDER})
            .build();

        final var emails = getEmailsDbAdapter.get(requestWrapper);

        assertNotNull(emails);
        assertTrue(CollectionUtils.isNotEmpty(emails.getContent()));
        getEmailsCache().ifPresent(cache -> assertFalse(cache.isEmpty()));

        createEmailDbAdapter.create(EmailsTestData.email());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        emailRepository.deleteAll();
    }

    @Test
    void deleteByEmailId_evictEmailCache_success() {
        final var savedEmails = emailRepository.saveAll(EmailsTestData.emails());
        assertFalse(savedEmails.isEmpty());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = GetEmailsFilterWrapper.builder()
            .page(PAGE)
            .size(SIZE)
            .sort(new String[]{SORT_ORDER})
            .build();

        final var emails = getEmailsDbAdapter.get(requestWrapper);

        assertNotNull(emails);
        assertTrue(CollectionUtils.isNotEmpty(emails.getContent()));
        getEmailsCache().ifPresent(cache -> assertFalse(cache.isEmpty()));

        deleteEmailDbAdapter.deleteByEmailId(savedEmails.stream().findFirst().get().getEmailId());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_addEmailListCache_success() {
        emailRepository.saveAll(EmailsTestData.emails());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = GetEmailsFilterWrapper.builder()
            .page(PAGE)
            .size(SIZE)
            .sort(new String[]{SORT_ORDER})
            .build();


        final var emails = getEmailsDbAdapter.get(requestWrapper);

        getEmailsCache().ifPresent(cache -> {
            assertFalse(cache.isEmpty());
            var cachedPage = (Page) cache.get(
                List.of(
                    requestWrapper.getPage(),
                    requestWrapper.getSize()));
            assertEquals(emails.getContent().size(), cachedPage.getContent().size());
        });

        emailRepository.deleteAll();
    }

    private Optional<ConcurrentMap<Object, Object>> getEmailsCache() {
        return Optional
            .ofNullable(cacheManager.getCache(EmailCacheConstant.LIST_CACHE_NAME))
            .map(cache -> ((ConcurrentMapCache) cache).getNativeCache());
    }
}