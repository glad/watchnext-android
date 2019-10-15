package com.glad.watchnext.domain.usecase.movie;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.movie.MovieDetailed;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Single;

/**
 * Use case for returning detailed information about a Movie
 * <p>
 * Created by Gautam Lad
 */
public final class GetMovieDetailUseCase implements UseCase<Single<MovieDetailed>, String> {
    private static final String TAG = GetMovieDetailUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetMovieDetailUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Single<MovieDetailed> execute(final String... args) {
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
        return dataProvider.fetchMovieDetail(id)
                .doOnError(e -> log.e(TAG, ": fetchMovieDetail(" + id + ")" + e.getMessage(), e));
    }
}