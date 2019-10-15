package com.glad.watchnext.domain.usecase.tv.show;

import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.model.tv.show.TvShowDetailed;
import com.glad.watchnext.domain.provider.DataProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.usecase.UseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import io.reactivex.Single;

/**
 * Use case for returning detailed information about a TV Show
 * <p>
 * Created by Gautam Lad
 */
public final class GetTvShowDetailUseCase implements UseCase<Single<TvShowDetailed>, String> {
    private static final String TAG = GetTvShowDetailUseCase.class.getSimpleName();

    private final DataProvider dataProvider;
    private final LogService log;

    public GetTvShowDetailUseCase(final DataProvider dataProvider, final LogService log) {
        this.dataProvider = dataProvider;
        this.log = log;
    }

    @Override
    public Single<TvShowDetailed> execute(final String... args) {
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
        return dataProvider.fetchTvShowDetail(id)
                .doOnError(e -> log.e(TAG, ": fetchTvShowDetail(" + id + ")" + e.getMessage(), e));
    }
}