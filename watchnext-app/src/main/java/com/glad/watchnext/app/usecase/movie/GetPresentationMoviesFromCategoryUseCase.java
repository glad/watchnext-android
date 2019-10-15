package com.glad.watchnext.app.usecase.movie;

import com.glad.watchnext.app.usecase.PresentationUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.movie.MovieSimplifiedPresentationModel;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.usecase.movie.GetMoviesFromCategoryUseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public class GetPresentationMoviesFromCategoryUseCase implements PresentationUseCase<Observable<MovieSimplifiedPresentationModel>, Object> {
    @NonNull private static final String TAG = LogUtil.getTag(GetPresentationMoviesFromCategoryUseCase.class);
    @NonNull private static final String CACHE_NAME = "movies-from-category";

    @NonNull private final GetMoviesFromCategoryUseCase useCase;
    @NonNull private final StorageService.Editor cache;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final LogService log;

    public GetPresentationMoviesFromCategoryUseCase(
            @NonNull final GetMoviesFromCategoryUseCase useCase,
            @NonNull final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.cache = cache.edit(CACHE_NAME);
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public Observable<MovieSimplifiedPresentationModel> execute(final boolean ignoreCache, @NonNull final Object... args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("Invalid number of arguments.  Expected 2 arguments, found" +
                        " :: args = [" + StringHelper.delimited(", ", args) + "]");
            }
            ValueHelper.requireInstance(args[0], String.class);
            ValueHelper.requireInstance(args[1], Integer.class);
        } catch (final NullPointerException | IllegalArgumentException e) {
            log.e(TAG, "execute: " + e.getMessage(), e);
            return Observable.error(new InvalidArgumentsException(e));
        }

        final String movieId = args[0].toString();
        final Integer pageIndex = (Integer) args[1];
        final String cacheKey = movieId + String.valueOf(pageIndex);

        return Observable.just(ignoreCache)
                .flatMap(notFromCache -> {
                    if (notFromCache) {
                        log.w(TAG, "execute: Bypassing cache");
                        throw new IllegalStateException();
                    }
                    return cache.readSerializable(cacheKey)
                            .flatMapObservable(serializable -> {
                                final ArrayList<MovieSimplifiedPresentationModel> models = new ArrayList<>();
                                for (Object item : (ArrayList<?>) serializable) {
                                    models.add((MovieSimplifiedPresentationModel) item);
                                }
                                if (models.isEmpty()) {
                                    throw new IllegalStateException();
                                }
                                return Observable.fromIterable(models);
                            });
                }).onErrorResumeNext(useCase.execute(args)
                        .flatMap(movie -> {
                            try {
                                return Observable.just(modelMapper.from(movie));
                            } catch (final InvalidMovieException e) {
                                log.w(TAG, "loadData: " + e.getMessage() + "\n" + movie.toString());
                                return Observable.empty();
                            }
                        })
                        .cast(MovieSimplifiedPresentationModel.class).toList()
                        .flatMapObservable(list -> (list.isEmpty() ? Completable.complete() : cache.writeSerializable(cacheKey, (ArrayList) list))
                                .onErrorComplete()
                                .andThen(Observable.fromIterable(list))))
                .doOnError(e -> log.e(TAG, "execute(): " + e.getMessage(), e));
    }
}