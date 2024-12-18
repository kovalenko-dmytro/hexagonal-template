package com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file;

import com.github.database.rider.core.api.dataset.DataSet;
import com.gmail.apach.hexagonaltemplate.AbstractIntegrationTest;
import com.gmail.apach.hexagonaltemplate.data.FilesTestData;
import com.gmail.apach.hexagonaltemplate.domain.file.model.StoredFile;
import com.gmail.apach.hexagonaltemplate.infrastructure.common.config.cache.constant.FileCacheConstant;
import com.gmail.apach.hexagonaltemplate.infrastructure.output.db.file.wrapper.GetFilesFilterWrapper;
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
    private CreateFileDbAdapter createFileDbAdapter;
    @Autowired
    private DeleteFileDbAdapter deleteFileDbAdapter;
    @Autowired
    private GetFileDbAdapter getFileDbAdapter;
    @Autowired
    private GetFilesDbAdapter getFilesDbAdapter;

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void createFile_evictFilesCacheLists_success() {
        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = getFilesRequestWrapper();

        final var files = getFilesDbAdapter.get(requestWrapper);

        assertNotNull(files);
        assertTrue(CollectionUtils.isNotEmpty(files.getContent()));
        getFilesCache().ifPresent(cache -> assertFalse(cache.isEmpty()));

        createFileDbAdapter.create(FilesTestData.fileWithStorageKey());

        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void deleteFileByFileId_evictFilesCacheListsAndFileCache_success() {
        assertTrue(getCachedFile(FILE_ID).isEmpty());
        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var fileFromDB = getFileDbAdapter.getByFileId(FILE_ID);
        final var cachedFile = getCachedFile(FILE_ID);

        assertFalse(cachedFile.isEmpty());
        assertEquals(fileFromDB, cachedFile.get());

        final var requestWrapper = getFilesRequestWrapper();

        final var files = getFilesDbAdapter.get(requestWrapper);

        assertNotNull(files);
        getFilesCache().ifPresent(cache -> assertFalse(cache.isEmpty()));

        deleteFileDbAdapter.deleteByFileId(FILE_ID);

        assertTrue(getCachedFile(FILE_ID).isEmpty());
        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void getFileByFileId_addFileCache_success() {
        assertTrue(getCachedFile(FILE_ID).isEmpty());

        final var fileFromDB = getFileDbAdapter.getByFileId(FILE_ID);
        final var cachedFile = getCachedFile(FILE_ID);

        assertFalse(cachedFile.isEmpty());
        assertEquals(fileFromDB, cachedFile.get());
    }

    @Test
    @DataSet("datasets/infrastructure/output/db/file/get_files_setup.yml")
    void getFiles_addFilesCache_success() {
        getFilesCache().ifPresent(cache -> assertTrue(cache.isEmpty()));

        final var requestWrapper = getFilesRequestWrapper();

        final var files = getFilesDbAdapter.get(requestWrapper);

        getFilesCache().ifPresent(cache -> {
            assertFalse(cache.isEmpty());
            var cachedPage = (Page) cache.get(
                List.of(
                    requestWrapper.getPage(),
                    requestWrapper.getSize()));
            assertEquals(files.getContent().size(), cachedPage.getContent().size());
        });
    }

    private static GetFilesFilterWrapper getFilesRequestWrapper() {
        return GetFilesFilterWrapper.builder()
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