package com.glad.watchnext.app.usecase.search;

import com.glad.watchnext.app.usecase.PresentationUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.person.PersonSimplifiedPresentationModel;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.usecase.search.GetPeopleSearchUseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public class GetPresentationPeopleSearchUseCase implements PresentationUseCase<Observable<PersonSimplifiedPresentationModel>, Object> {
    @NonNull private static final String TAG = LogUtil.getTag(GetPresentationPeopleSearchUseCase.class);
    @NonNull private static final String CACHE_NAME = "people-search";

    @NonNull private final GetPeopleSearchUseCase useCase;
    @NonNull private final StorageService.Editor cache;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final LogService log;

    public GetPresentationPeopleSearchUseCase(
            @NonNull final GetPeopleSearchUseCase useCase,
            @NonNull final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.cache = cache.edit(CACHE_NAME);
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public Observable<PersonSimplifiedPresentationModel> execute(final boolean ignoreCache, @NonNull final Object... args) {
        try {
            if (args.length != 2) {
                throw new IllegalArgumentException("Invalid number of arguments.  Expected 2 arguments, found" +
                        " :: args = [" + StringHelper.delimited(", ", args) + "]");
            }
            ValueHelper.requireValue(ValueHelper.requireInstance(args[0], String.class), "Query cannot be empty");
            ValueHelper.requireInstance(args[1], Integer.class);
        } catch (final NullPointerException | IllegalArgumentException e) {
            log.e(TAG, "execute: " + e.getMessage(), e);
            return Observable.error(new InvalidArgumentsException(e));
        }

        final String query = args[0].toString();
        final Integer pageIndex = (Integer) args[1];
        final String cacheKey = query + String.valueOf(pageIndex);

        return Observable.just(ignoreCache)
                .flatMap(notFromCache -> {
                    if (notFromCache) {
                        log.w(TAG, "execute: Bypassing cache");
                        throw new IllegalStateException();
                    }
                    return cache.readSerializable(cacheKey)
                            .flatMapObservable(serializable -> {
                                final ArrayList<PersonSimplifiedPresentationModel> models = new ArrayList<>();
                                for (Object item : (ArrayList<?>) serializable) {
                                    models.add((PersonSimplifiedPresentationModel) item);
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
                            } catch (final InvalidPersonException e) {
                                log.w(TAG, "loadData: " + e.getMessage() + "\n" + movie.toString());
                                return Observable.empty();
                            }
                        })
                        .cast(PersonSimplifiedPresentationModel.class).toList()
                        .flatMapObservable(list -> (list.isEmpty() ? Completable.complete() : cache.writeSerializable(cacheKey, (ArrayList) list))
                                .onErrorComplete()
                                .andThen(Observable.fromIterable(list))))
                .doOnError(e -> log.e(TAG, "execute(): " + e.getMessage(), e));
    }
}