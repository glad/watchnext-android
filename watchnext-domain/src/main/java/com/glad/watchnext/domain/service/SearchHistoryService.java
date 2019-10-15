package com.glad.watchnext.domain.service;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by Gautam Lad
 */
public interface SearchHistoryService {
    /**
     * Adds a query to the search history
     */
    Completable add(final String query);

    /**
     * Deletes the query from the search history
     */
    Completable delete(final String query);

    /**
     * Clears the search history
     */
    Completable clear();

    /**
     * @return A list of all queries in the search history
     */
    Single<List<String>> getHistory();
}
