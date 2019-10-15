package com.glad.watchnext.domain.usecase.people;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.person.PersonSimplified;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Observable;

/**
 * Use case for returning People from a particular category
 * <p>
 * Created by Gautam Lad
 */
public final class GetPeopleFromCategoryUseCase implements UseCase<Observable<PersonSimplified>, Object> {
    private static final String TAG = GetPeopleFromCategoryUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetPeopleFromCategoryUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<PersonSimplified> execute(final Object... args) {
        try {
            if (args == null || args.length != 2) {
                throw new IllegalArgumentException("Invalid number of arguments.  Expected 2 arguments, found" +
                        " :: args = [" + StringHelper.delimited(", ", args) + "]");
            }
            ValueHelper.requireInstance(args[0], String.class);
            ValueHelper.requireInstance(args[1], Integer.class);
        } catch (final NullPointerException | IllegalArgumentException e) {
            log.e(TAG, "execute: " + e.getMessage(), e);
            return Observable.error(new InvalidArgumentsException(e));
        }

        final String categoryId = args[0].toString();
        final Integer pageIndex = (Integer) args[1];

        return dataProvider.fetchPeopleFromCategoryId(categoryId, pageIndex)
                .doOnError(e -> log.e(TAG, "fetchPeopleFromCategoryId(" + categoryId + ", " + pageIndex + "): " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}