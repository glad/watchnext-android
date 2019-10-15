package com.glad.watchnext.domain.usecase.search;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.movie.MovieSimplified;
import com.glad.watchnext.domain.provider.SearchDataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Observable;

/**
 * Use case for searching Movies
 * <p>
 * Created by Gautam Lad
 */
public final class GetMoviesSearchUseCase implements UseCase<Observable<MovieSimplified>, Object> {
    private static final String TAG = GetMoviesSearchUseCase.class.getSimpleName();

    private final SearchDataProvider dataProvider;
    private final LogService log;

    public GetMoviesSearchUseCase(final SearchDataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<MovieSimplified> execute(final Object... args) {
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

        final String query = args[0].toString();
        final Integer pageIndex = (Integer) args[1];

        return dataProvider.searchMovies(query, pageIndex)
                .doOnError(e -> log.e(TAG, "searchMovies(" + query + ", " + pageIndex + "): " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}