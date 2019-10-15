package com.glad.watchnext.domain.usecase.tv.show;

import com.glad.watchnext.domain.model.common.Category;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;

import io.reactivex.Observable;

/**
 * Use case for returning categories for TV Shows collection
 * <p>
 * Created by Gautam Lad
 */
public final class GetTvShowCategoriesUseCase implements UseCase<Observable<Category>, Void> {
    private static final String TAG = GetTvShowCategoriesUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetTvShowCategoriesUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<Category> execute(final Void... args) {
        return dataProvider.fetchTvShowCategories()
                .doOnError(e -> log.e(TAG, "fetchTvShowCategories: " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}