package com.glad.watchnext.domain.implementation.service;

import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.SearchHistoryService;
import com.glad.watchnext.domain.service.StorageService;

import java.util.AbstractMap.SimpleEntry;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Gautam Lad
 */
public class DefaultSearchHistoryService implements SearchHistoryService {
    private static final String TAG = DefaultSearchHistoryService.class.getSimpleName();
    private static final String STORAGE_NAME = "default-search-history";

    private final StorageService.Editor editor;
    private final LogService log;

    public DefaultSearchHistoryService(
            final StorageService historyStorage,
            final LogService log) {
        this.editor = historyStorage.edit(STORAGE_NAME);
        this.log = log;
    }

    public Completable add(final String query) {
        log.d(TAG, "add() called with: query = [" + query + "]");
        return editor.exists(query)
                .flatMapCompletable(exists -> {
                    if (exists) {
                        return Completable.complete();
                    }

                    return editor.writeString(query, query)
                            .onErrorComplete();
                });
    }

    @Override
    public Completable delete(final String query) {
        return editor.delete(query)
                .onErrorComplete();
    }

    @Override
    public Completable clear() {
        return editor.clear()
                .onErrorComplete();
    }

    @Override
    public Single<List<String>> getHistory() {
        return editor.readAll()
                .map(SimpleEntry::getValue)
                .cast(String.class)
                .toList();
    }
}
