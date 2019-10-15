package com.glad.watchnext.domain.usecase.movie;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.movie.MovieSimplified;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Observable;

/**
 * Use case for returning similar TV Shows for a particular TV Show
 * <p>
 * Created by Gautam Lad
 */
public final class GetSimilarMoviesUseCase implements UseCase<Observable<MovieSimplified>, String> {
    private static final String TAG = GetSimilarMoviesUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetSimilarMoviesUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<MovieSimplified> execute(final String... args) {
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

        return dataProvider.fetchSimilarMovies(id)
                .doOnError(e -> log.e(TAG, "fetchSimilarMovies(" + id + "): " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}