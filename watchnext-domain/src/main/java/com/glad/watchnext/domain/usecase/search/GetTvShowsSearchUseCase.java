package com.glad.watchnext.domain.usecase.search;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.tv.show.TvShowSimplified;
import com.glad.watchnext.domain.provider.SearchDataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Observable;

/**
 * Use case for searching Tv Shows
 * <p>
 * Created by Gautam Lad
 */
public final class GetTvShowsSearchUseCase implements UseCase<Observable<TvShowSimplified>, Object> {
    private static final String TAG = GetTvShowsSearchUseCase.class.getSimpleName();

    private final SearchDataProvider dataProvider;
    private final LogService log;

    public GetTvShowsSearchUseCase(final SearchDataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Observable<TvShowSimplified> execute(final Object... args) {
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

        return dataProvider.searchTvShows(query, pageIndex)
                .doOnError(e -> log.e(TAG, "searchTvShows(" + query + ", " + pageIndex + "): " + e.getMessage(), e))
                .onErrorResumeNext(Observable.empty());
    }
}