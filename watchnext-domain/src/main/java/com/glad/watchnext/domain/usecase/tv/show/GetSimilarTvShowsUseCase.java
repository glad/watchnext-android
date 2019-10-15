package com.glad.watchnext.domain.usecase.tv.show;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.tv.show.TvShowSimplified;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Observable;

/**
 * Use case for returning similar Movies for a particular Movie
 * <p>
 * Created by Gautam Lad
 */
public final class GetSimilarTvShowsUseCase implements UseCase<Observable<TvShowSimplified>, String> {
    private static final String TAG = GetSimilarTvShowsUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetSimilarTvShowsUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<TvShowSimplified> execute(final String... args) {
        try {
            if (args == null || args.length != 1) {
                throw new IllegalArgumentException("Invalid number of arguments.  Expected 1 argument, found" +
                        " :: args = [" + StringHelper.delimited(", ", (Object[]) args) + "]");
            }
            ValueHelper.requireInstance(args[0], String.class);
        } catch (final NullPointerException | IllegalArgumentException e) {
            log.e(TAG, "execute: " + e.getMessage(), e);
            return Observable.error(new InvalidArgumentsException(e));
        }

        final String id = args[0];

        return dataProvider.fetchSimilarTvShows(id)
                .doOnError(e -> log.e(TAG, "fetchSimilarTvShows(" + id + "): " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}