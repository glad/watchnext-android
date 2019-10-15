package com.glad.watchnext.app.usecase.movie;

import com.glad.watchnext.app.usecase.PresentationUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.movie.MovieDetailedPresentationModel;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.usecase.movie.GetMovieDetailUseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.support.annotation.NonNull;

import io.reactivex.Single;

/**
 * Created by Gautam Lad
 */
public class GetPresentationMovieDetailUseCase implements PresentationUseCase<Single<MovieDetailedPresentationModel>, String> {
    @NonNull private static final String TAG = LogUtil.getTag(GetPresentationMovieDetailUseCase.class);
    @NonNull private static final String CACHE_NAME = "movie-detail";

    @NonNull private final GetMovieDetailUseCase useCase;
    @NonNull private final StorageService.Editor cache;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final LogService log;

    public GetPresentationMovieDetailUseCase(
            @NonNull final GetMovieDetailUseCase useCase,
            @NonNull final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.cache = cache.edit(CACHE_NAME);
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public Single<MovieDetailedPresentationModel> execute(final boolean ignoreCache, @NonNull final String... args) {
        try {
            if (args.length != 1) {
                throw new IllegalArgumentException("Invalid number of arguments" +
                        " :: args = [" + StringHelper.delimited(", ", (Object[]) args) + "]");
            }
            ValueHelper.requireInstance(args[0], String.class);
        } catch (final NullPointerException | IllegalArgumentException e) {
            return Single.error(new InvalidArgumentsException(e));
        }

        final String id = args[0];
        return Single.just(ignoreCache)
                .flatMap(notFromCache -> {
                    if (notFromCache) {
                        log.w(TAG, "execute: Bypassing cache");
                        throw new IllegalStateException();
                    }
                    return cache.readSerializable(id)
                            .flatMap(serializable -> Single.just((MovieDetailedPresentationModel) serializable));
                })
                .onErrorResumeNext(throwable -> useCase.execute(id)
                        .flatMap(movie -> {
                            try {
                                final MovieDetailedPresentationModel model = modelMapper.from(movie);
                                return cache.writeSerializable(model.getId(), model)
                                        .onErrorComplete()
                                        .andThen(Single.just(model));
                            } catch (final InvalidMovieException e) {
                                return Single.error(e);
                            }
                        })
                        .doOnError(e -> log.e(TAG, "execute(" + id + "): " + e.getMessage(), e)));
    }
}
