package com.glad.watchnext.domain.usecase.common;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.common.CastCredit;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Observable;

/**
 * Use case for returning cast credit information for a Movie or TV Show
 * <p>
 * Created by Gautam Lad
 */
public final class GetCastCreditsUseCase implements UseCase<Observable<CastCredit>, String> {
    private static final String TAG = GetCastCreditsUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetCastCreditsUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<CastCredit> execute(final String... args) {
        try {
            if (args == null || args.length != 2) {
                throw new IllegalArgumentException("Invalid number of arguments.  Expected 2 arguments, found" +
                        " :: args = [" + StringHelper.delimited(", ", (Object[]) args) + "]");
            }
            ValueHelper.requireInstance(args[0], String.class);
            ValueHelper.requireInstance(args[1], String.class);
        } catch (final NullPointerException | IllegalArgumentException e) {
            log.e(TAG, "execute: " + e.getMessage(), e);
            return Observable.error(new InvalidArgumentsException(e));
        }

        final String type = args[0];
        final String id = args[1];

        return dataProvider.fetchCastCredits(type, id)
                .doOnError(e -> log.e(TAG, "fetchCastCredits(" + type + ", " + id + "): " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}