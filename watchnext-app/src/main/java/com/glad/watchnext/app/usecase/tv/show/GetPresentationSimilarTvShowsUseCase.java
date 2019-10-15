package com.glad.watchnext.app.usecase.tv.show;

import com.glad.watchnext.app.usecase.PresentationUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.tv.show.TvShowSimplifiedPresentationModel;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.usecase.tv.show.GetSimilarTvShowsUseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public class GetPresentationSimilarTvShowsUseCase implements PresentationUseCase<Observable<TvShowSimplifiedPresentationModel>, String> {
    @NonNull private static final String TAG = LogUtil.getTag(GetPresentationSimilarTvShowsUseCase.class);
    @NonNull private static final String CACHE_NAME = "similar-tvshows";

    @NonNull private final GetSimilarTvShowsUseCase useCase;
    @NonNull private final StorageService.Editor cache;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final LogService log;

    public GetPresentationSimilarTvShowsUseCase(
            @NonNull final GetSimilarTvShowsUseCase useCase,
            @NonNull final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.cache = cache.edit(CACHE_NAME);
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public Observable<TvShowSimplifiedPresentationModel> execute(final boolean ignoreCache, @NonNull final String... args) {
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
                                final ArrayList<TvShowSimplifiedPresentationModel> models = new ArrayList<>();
                                for (Object item : (ArrayList<?>) serializable) {
                                    models.add((TvShowSimplifiedPresentationModel) item);
                                }
                                if (models.isEmpty()) {
                                    throw new IllegalStateException();
                                }
                                return Observable.fromIterable(models);
                            });
                }).onErrorResumeNext(useCase.execute(args)
                        .flatMap(tvshow -> {
                            try {
                                return Observable.just(modelMapper.from(tvshow));
                            } catch (final InvalidTvShowException e) {
                                log.w(TAG, "loadData: " + e.getMessage() + "\n" + tvshow.toString());
                                return Observable.empty();
                            }
                        })
                        .cast(TvShowSimplifiedPresentationModel.class).toList()
                        .flatMapObservable(list -> (list.isEmpty() ? Completable.complete() : cache.writeSerializable(id, (ArrayList) list))
                                .onErrorComplete()
                                .andThen(Observable.fromIterable(list))))
                .doOnError(e -> log.e(TAG, "execute(): " + e.getMessage(), e));
    }
}