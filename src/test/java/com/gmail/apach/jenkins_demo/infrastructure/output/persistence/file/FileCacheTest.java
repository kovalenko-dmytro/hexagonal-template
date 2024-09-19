package com.gmail.apach.jenkins_demo.infrastructure.output.persistence.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.jenkins_demo.AbstractIntegrationTest;
import com.gmail.apach.jenkins_demo.common.constant.cache.FileCacheConstant;
import com.gmail.apach.jenkins_demo.data.FilesTestData;
import com.gmail.apach.jenkins_demo.domain.file.model.StoredFile;
import com.gmail.apach.jenkins_demo.domain.file.wrapper.GetFilesRequestWrapper;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;

class FileCacheTest extends AbstractIntegrationTest {

    private static final String FILE_ID = "file68c8-2f28-4b53-ac5a-2db586512441";

    @Autowired
    private CreateFilePersistenceAdapter createFilePersistenceAdapter;
    @Autowired
    private DeleteFilePersistenceAdapter deleteFilePersistenceAdapter;
    @Autowired
    private GetFilePersistenceAdapter getFilePersistenceAdapter;
    @Autowired
    private GetFilesPersistenceAdapter getFilesPersistenceAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/persistence/file/get_files_setup.yml")
    void createFile_evictFilesCacheLists_success() {
        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = getFilesRequestWrapper();

        final var files = getFilesPersistenceAdapter.getFiles(requestWrapper);

        assertNotNull(files);
        assertTrue(CollectionUtils.isNotEmpty(files.getContent()));
        getFilesCache().ifPresent(cache -> assertFalse(cache.isEmpty()));

        createFilePersistenceAdapter.createFile(FilesTestData.fileWithStorageKey());

        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/file/get_files_setup.yml")
    void deleteFileByFileId_evictFilesCacheListsAndFileCache_success() {
        assertTrue(getCachedFile(FILE_ID).isEmpty());
        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var fileFromDB = getFilePersistenceAdapter.getByFileId(FILE_ID);
        final var cachedFile = getCachedFile(FILE_ID);

        assertFalse(cachedFile.isEmpty());
        assertEquals(fileFromDB, cachedFile.get());

        final var requestWrapper = getFilesRequestWrapper();

        final var files = getFilesPersistenceAdapter.getFiles(requestWrapper);

        assertNotNull(files);
        getFilesCache().ifPresent(cache -> assertFalse(cache.isEmpty()));

        deleteFilePersistenceAdapter.deleteFile(FILE_ID);

        assertTrue(getCachedFile(FILE_ID).isEmpty());
        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/file/get_files_setup.yml")
    void getFileByFileId_addFileCache_success() {
        assertTrue(getCachedFile(FILE_ID).isEmpty());

        final var fileFromDB = getFilePersistenceAdapter.getByFileId(FILE_ID);
        final var cachedFile = getCachedFile(FILE_ID);

        assertFalse(cachedFile.isEmpty());
        assertEquals(fileFromDB, cachedFile.get());
    }

    @Test
    @DataSet("datasets/infrastructure/output/persistence/file/get_files_setup.yml")
    void getFiles_addFilesCache_success() {
        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = getFilesRequestWrapper();

        final var files = getFilesPersistenceAdapter.getFiles(requestWrapper);

        getFilesCache().ifPresent(cache -> {
            assertFalse(cache.isEmpty());
            var cachedPage = (Page) cache.get(
                List.of(
                    requestWrapper.page(),
                    requestWrapper.size()));
            assertEquals(files.getContent().size(), cachedPage.getContent().size());
        });
    }

    private static GetFilesRequestWrapper getFilesRequestWrapper() {
        return GetFilesRequestWrapper.builder()
            .page(1)
            .size(5)
            .sort(new String[]{"created"})
            .build();
    }

    private Optional<ConcurrentMap<Object, Object>> getFilesCache() {
        return Optional
            .ofNullable(cacheManager.getCache(FileCacheConstant.LIST_CACHE_NAME))
            .map(cache -> ((ConcurrentMapCache) cache).getNativeCache());
    }

    private Optional<StoredFile> getCachedFile(String fileId) {
        return Optional.ofNullable(cacheManager.getCache(FileCacheConstant.CACHE_NAME))
            .map(cache -> cache.get(fileId, StoredFile.class));
    }
}