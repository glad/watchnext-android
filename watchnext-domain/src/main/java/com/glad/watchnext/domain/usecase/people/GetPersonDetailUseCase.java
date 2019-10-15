package com.glad.watchnext.domain.usecase.people;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.person.PersonDetailed;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Single;

/**
 * Use case for returning detailed information about a Person
 * <p>
 * Created by Gautam Lad
 */
public final class GetPersonDetailUseCase implements UseCase<Single<PersonDetailed>, String> {
    private static final String TAG = GetPersonDetailUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetPersonDetailUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Single<PersonDetailed> execute(final String... args) {
        try {
            if (args == null || args.length != 1) {
                throw new IllegalArgumentException("Invalid number of arguments" +
                        " :: args = [" + StringHelper.delimited(", ", (Object[]) args) + "]");
            }
            ValueHelper.requireInstance(args[0], String.class);
        } catch (final NullPointerException | IllegalArgumentException e) {
            log.e(TAG, "execute: " + e.getMessage(), e);
            return Single.error(new InvalidArgumentsException(e));
        }

        final String id = args[0];
        return dataProvider.fetchPersonDetail(id)
                .doOnError(e -> log.e(TAG, ": fetchPersonDetail(" + id + ")" + e.getMessage(), e));
    }
}