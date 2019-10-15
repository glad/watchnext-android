package com.glad.watchnext.app.usecase.person;

import com.glad.watchnext.app.usecase.PresentationUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.person.PersonDetailedPresentationModel;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidArgumentsException;
import com.glad.watchnext.domain.exception.InvalidPersonException;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.StorageService;
import com.glad.watchnext.domain.usecase.people.GetPersonDetailUseCase;
import com.glad.watchnext.domain.util.StringHelper;
import com.glad.watchnext.domain.util.ValueHelper;

import android.support.annotation.NonNull;

import io.reactivex.Single;

/**
 * Created by Gautam Lad
 */
public class GetPresentationPersonDetailUseCase implements PresentationUseCase<Single<PersonDetailedPresentationModel>, String> {
    @NonNull private static final String TAG = LogUtil.getTag(GetPresentationPersonDetailUseCase.class);
    @NonNull private static final String CACHE_NAME = "person-detail";

    @NonNull private final GetPersonDetailUseCase useCase;
    @NonNull private final StorageService.Editor cache;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final LogService log;

    public GetPresentationPersonDetailUseCase(
            @NonNull final GetPersonDetailUseCase useCase,
            @NonNull final StorageService cache,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.cache = cache.edit(CACHE_NAME);
        this.modelMapper = modelMapper;
        this.log = log;
    }

    @Override
    public Single<PersonDetailedPresentationModel> execute(final boolean ignoreCache, @NonNull final String... args) {
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
                            .flatMap(serializable -> Single.just((PersonDetailedPresentationModel) serializable));
                })
                .onErrorResumeNext(throwable -> useCase.execute(id)
                        .flatMap(person -> {
                            try {
                                final PersonDetailedPresentationModel model = modelMapper.from(person);
                                return cache.writeSerializable(model.getId(), model)
                                        .onErrorComplete()
                                        .andThen(Single.just(model));
                            } catch (final InvalidPersonException e) {
                                return Single.error(e);
                            }
                        })
                        .doOnError(e -> log.e(TAG, "execute(" + id + "): " + e.getMessage(), e)));
    }
}