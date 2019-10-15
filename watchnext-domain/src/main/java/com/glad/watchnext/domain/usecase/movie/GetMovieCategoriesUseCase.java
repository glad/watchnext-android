package com.glad.watchnext.domain.usecase.movie;

import com.glad.watchnext.domain.model.common.Category;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;

import io.reactivex.Observable;

/**
 * Use case for returning categories for Movies collection
 * <p>
 * Created by Gautam Lad
 */
public final class GetMovieCategoriesUseCase implements UseCase<Observable<Category>, Void> {
    private static final String TAG = GetMovieCategoriesUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetMovieCategoriesUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<Category> execute(final Void... args) {
        return dataProvider.fetchMovieCategories()
                .doOnError(e -> log.e(TAG, "fetchMovieCategories: " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}