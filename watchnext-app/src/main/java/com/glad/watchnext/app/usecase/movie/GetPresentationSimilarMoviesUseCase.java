package com.glad.watchnext.app.usecase.movie;

import com.glad.watchnext.app.usecase.PresentationUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.movie.MovieSimplifiedPresentationModel;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidMovieException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.usecase.movie.GetSimilarMoviesUseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public class GetPresentationSimilarMoviesUseCase implements PresentationUseCase<Observable<MovieSimplifiedPresentationModel>, String> {
    @NonNull private static final String TAG = LogUtil.getTag(GetPresentationSimilarMoviesUseCase.class);
    @NonNull private static final String CACHE_NAME = "similar-movies";

    @NonNull private final GetSimilarMoviesUseCase useCase;
    @NonNull private final StorageService.Editor cache;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final LogService log;

    public GetPresentationSimilarMoviesUseCase(
            @NonNull final GetSimilarMoviesUseCase useCase,
            @NonNull final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.cache = cache.edit(CACHE_NAME);
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public Observable<MovieSimplifiedPresentationModel> execute(final boolean ignoreCache, @NonNull final String... args) {
        try {
            if (args.length != 1) {
                throw new IllegalArgumentException("Invalid number of arguments.  Expected 1 arguments, found" +
                        " :: args = [" + StringHelper.delimited(", ", (Object[]) args) + "]");
            }
            ValueHelper.requireInstance(args[0], String.class);
        } catch (final NullPointerException | IllegalArgumentException e) {
            log.e(TAG, "execute: " + e.getMessage(), e);
            return Observable.error(new InvalidArgumentsException(e));
        }

        final String id = args[0];
        return Observable.just(ignoreCache)
                .flatMap(notFromCache -> {
                    if (notFromCache) {
                        log.w(TAG, "execute: Bypassing cache");
                        throw new IllegalStateException();
                    }
                    return cache.readSerializable(id)
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
                        .flatMapObservable(list -> (list.isEmpty() ? Completable.complete() : cache.writeSerializable(id, (ArrayList) list))
                                .onErrorComplete()
                                .andThen(Observable.fromIterable(list))))
                .doOnError(e -> log.e(TAG, "execute(): " + e.getMessage(), e));
    }
}