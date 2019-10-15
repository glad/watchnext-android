package com.glad.watchnext.domain.usecase.people;

import com.glad.watchnext.domain.model.common.Category;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;

import io.reactivex.Observable;

/**
 * Use case for returning categories for People collection
 * <p>
 * Created by Gautam Lad
 */
public final class GetPeopleCategoriesUseCase implements UseCase<Observable<Category>, Void> {
    private static final String TAG = GetPeopleCategoriesUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetPeopleCategoriesUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<Category> execute(final Void... args) {
        return dataProvider.fetchPeopleCategories()
                .doOnError(e -> log.e(TAG, "fetchPeopleCategories: " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}