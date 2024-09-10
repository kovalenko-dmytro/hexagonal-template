package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email;

import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.common.constant.cache.EmailCacheConstant;
import com.gmail.apach.jenkins_demo.data.EmailsTestData;
import com.gmail.apach.jenkins_demo.domain.email.model.Email;
import com.gmail.apach.jenkins_demo.domain.email.wrapper.GetEmailsWrapper;
import com.gmail.apach.jenkins_demo.infrastructure.output.persistence.email.repository.EmailRepository;
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
    private GetEmailsPersistenceAdapter getEmailsPersistenceAdapter;
    @Autowired
    private CreateEmailPersistenceAdapter createEmailPersistenceAdapter;
    @Autowired
    private DeleteEmailPersistenceAdapter deleteEmailPersistenceAdapter;
    @Autowired
    private EmailRepository emailRepository;

    @Test
    void createEmail_evictEmailCacheLists_success() {
        emailRepository.saveAll(EmailsTestData.emails());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = GetEmailsWrapper.builder()
            .page(PAGE)
            .size(SIZE)
            .sort(new String[]{SORT_ORDER})
            .build();

        final var emails = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(emails);
        assertTrue(CollectionUtils.isNotEmpty(emails.getContent()));
        getEmailsCache().ifPresent(cache -> assertFalse(cache.isEmpty()));

        createEmailPersistenceAdapter.createEmail(EmailsTestData.email());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        emailRepository.deleteAll();
    }

    @Test
    void deleteByEmailId_evictEmailCache_success() {
        final var savedEmails = emailRepository.saveAll(EmailsTestData.emails());
        assertFalse(savedEmails.isEmpty());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = GetEmailsWrapper.builder()
            .page(PAGE)
            .size(SIZE)
            .sort(new String[]{SORT_ORDER})
            .build();

        final var emails = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        assertNotNull(emails);
        assertTrue(CollectionUtils.isNotEmpty(emails.getContent()));
        getEmailsCache().ifPresent(cache -> assertFalse(cache.isEmpty()));

        deleteEmailPersistenceAdapter.deleteByEmailId(savedEmails.stream().findFirst().get().getEmailId());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        emailRepository.deleteAll();
    }

    @Test
    void getEmails_addEmailListCache_success() {
        emailRepository.saveAll(EmailsTestData.emails());

        getEmailsCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = GetEmailsWrapper.builder()
            .page(PAGE)
            .size(SIZE)
            .sort(new String[]{SORT_ORDER})
            .build();


        final var emails = getEmailsPersistenceAdapter.getEmails(requestWrapper);

        getEmailsCache().ifPresent(cache -> {
            assertFalse(cache.isEmpty());
            var cachedPage = (Page) cache.get(
                List.of(
                    requestWrapper.page(),
                    requestWrapper.size()));
            assertEquals(emails.getContent().size(), cachedPage.getContent().size());
        });

        emailRepository.deleteAll();
    }

    private Optional<ConcurrentMap<Object, Object>> getEmailsCache() {
        return Optional
            .ofNullable(cacheManager.getCache(EmailCacheConstant.LIST_CACHE_NAME))
            .map(cache -> ((ConcurrentMapCache) cache).getNativeCache());
    }

    private Optional<Email> getCachedEmail(String emailId) {
        return Optional.ofNullable(cacheManager.getCache(EmailCacheConstant.CACHE_NAME))
            .map(cache -> cache.get(emailId, Email.class));
    }
}