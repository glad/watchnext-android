package com.glad.watchnext.app.usecase.tv.show;

import com.glad.watchnext.app.usecase.PresentationUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.tv.show.TvShowSimplifiedPresentationModel;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidTvShowException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.usecase.tv.show.GetTvShowsFromCategoryUseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public class GetPresentationTvShowsFromCategoryUseCase implements PresentationUseCase<Observable<TvShowSimplifiedPresentationModel>, Object> {
    @NonNull private static final String TAG = LogUtil.getTag(GetPresentationTvShowsFromCategoryUseCase.class);
    @NonNull private static final String CACHE_NAME = "tvshows-from-category";

    @NonNull private final GetTvShowsFromCategoryUseCase useCase;
    @NonNull private final StorageService.Editor cache;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final LogService log;

    public GetPresentationTvShowsFromCategoryUseCase(
            @NonNull final GetTvShowsFromCategoryUseCase useCase,
            @NonNull final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.cache = cache.edit(CACHE_NAME);
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public Observable<TvShowSimplifiedPresentationModel> execute(final boolean ignoreCache, @NonNull final Object... args) {
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

        final String tvshowId = args[0].toString();
        final Integer pageIndex = (Integer) args[1];
        final String cacheKey = tvshowId + String.valueOf(pageIndex);

        return Observable.just(ignoreCache)
                .flatMap(notFromCache -> {
                    if (notFromCache) {
                        log.w(TAG, "execute: Bypassing cache");
                        throw new IllegalStateException();
                    }
                    return cache.readSerializable(cacheKey)
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
                        .flatMapObservable(list -> (list.isEmpty() ? Completable.complete() : cache.writeSerializable(cacheKey, (ArrayList) list))
                                .onErrorComplete()
                                .andThen(Observable.fromIterable(list))))
                .doOnError(e -> log.e(TAG, "execute(): " + e.getMessage(), e));
    }
}