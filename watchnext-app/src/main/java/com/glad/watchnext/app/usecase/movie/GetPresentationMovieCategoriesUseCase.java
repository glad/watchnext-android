package com.glad.watchnext.app.usecase.movie;

import com.glad.watchnext.app.usecase.PresentationUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.common.CollectionPresentationModel;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidCategoryException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.usecase.movie.GetMovieCategoriesUseCase;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public class GetPresentationMovieCategoriesUseCase implements PresentationUseCase<Observable<CollectionPresentationModel>, Void> {
    @NonNull private static final String TAG = LogUtil.getTag(GetPresentationMovieCategoriesUseCase.class);
    @NonNull private static final String CACHE_NAME = "movie-categories";

    @NonNull private final GetMovieCategoriesUseCase useCase;
    @NonNull private final StorageService.Editor cache;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final LogService log;

    public GetPresentationMovieCategoriesUseCase(
            @NonNull final GetMovieCategoriesUseCase useCase,
            @NonNull final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.cache = cache.edit(CACHE_NAME);
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public Observable<CollectionPresentationModel> execute(final boolean ignoreCache, @NonNull final Void... args) {
        return Observable.just(ignoreCache)
                .flatMap(notFromCache -> {
                    if (notFromCache) {
                        log.w(TAG, "execute: Bypassing cache");
                        throw new IllegalStateException();
                    }
                    return cache.readSerializable(CACHE_NAME)
                            .flatMapObservable(serializable -> {
                                final ArrayList<CollectionPresentationModel> models = new ArrayList<>();
                                for (Object item : (ArrayList<?>) serializable) {
                                    models.add((CollectionPresentationModel) item);
                                }
                                return Observable.fromIterable(models);
                            });
                }).onErrorResumeNext(throwable -> {
                    return useCase.execute()
                            .flatMap(category -> {
                                try {
                                    return Observable.just(modelMapper.from(category));
                                } catch (final InvalidCategoryException e) {
                                    log.w(TAG, "loadData: " + e.getMessage() + "\n" + category.toString());
                                    return Observable.empty();
                                }
                            })
                            .cast(CollectionPresentationModel.class)
                            .toList()
                            .flatMapObservable(list -> cache.writeSerializable(CACHE_NAME, (ArrayList) list)
                                    .onErrorComplete()
                                    .andThen(Observable.fromIterable(list)));
                })
                .doOnError(e -> log.e(TAG, "execute(): " + e.getMessage(), e));
    }
}