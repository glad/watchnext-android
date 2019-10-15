package com.glad.watchnext.app.view.detail.credits;

import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.util.PresentationModelMapper;
import com.glad.watchnext.domain.exception.InvalidCastException;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.NetworkStateProvider.NetworkConnectionState;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;
import com.glad.watchnext.domain.usecase.common.GetCastCreditsUseCase;

import android.support.annotation.NonNull;

import java.io.Serializable;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Gautam Lad
 */
public final class CastCreditsPresenter implements CastCreditsContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(CastCreditsPresenter.class);

    private CastCreditsContract.View view;
    @NonNull private String typeToLoad = "";
    @NonNull private String idToLoad = "";

    @NonNull private final GetCastCreditsUseCase useCase;
    @NonNull private final PresentationModelMapper modelMapper;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NavigationService navigationService;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    private int loadedCount = 0;

    public CastCreditsPresenter(
            @NonNull final GetCastCreditsUseCase useCase,
            @NonNull final PresentationModelMapper modelMapper,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.modelMapper = modelMapper;
        this.schedulerProvider = schedulerProvider;
        this.navigationService = navigationService;
        this.networkStateProvider = networkStateProvider;
        this.log = log;
    }

    //region ViewPresenterContract.Presenter

    @Override
    public void bind(@NonNull final CastCreditsContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        typeToLoad = args[0].toString();
        idToLoad = args[1].toString();
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
        disposables.add(networkStateProvider.asObservable()
                .filter(state -> (state == NetworkConnectionState.CONNECTED && loadedCount == 0))
                .subscribe(ignore -> loadData()));

        if (loadedCount > 0) {
            log.w(TAG, "subscribe: Data already loaded");
            return;
        }

        loadData();
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
        disposables.clear();
    }
    //region ViewPresenterContract.Presenter

    //region NetworkStateWidgetContract.Presenter
    @Override
    public void loadData() {
        log.d(TAG, "loadData() called :: idToLoad = [" + idToLoad + "]");
        disposables.add(useCase.execute(typeToLoad, idToLoad)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .flatMap(cast -> {
                    try {
                        return Observable.just(modelMapper.from(cast));
                    } catch (final InvalidCastException e) {
                        return Observable.error(e);
                    }
                })
                .doOnSubscribe(ignore -> {
                    loadedCount = 0;
                    view.onLoadStarted();
                })
                .doOnNext(ignore -> ++loadedCount)
                .subscribe(view::onLoaded, view::onError, () -> {
                    view.onLoadComplete();
                    view.setVisibility(loadedCount > 0);
                }));
    }

    @Override
    public void onClicked(@NonNull final String id) {
        log.d(TAG, "onClicked() called with: id = [" + id + "]");
        navigationService.navigateToPersonDetail(id);
    }
    //endregion NetworkStateWidgetContract.Presenter
}