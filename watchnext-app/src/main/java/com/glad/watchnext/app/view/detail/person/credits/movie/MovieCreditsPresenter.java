package com.glad.watchnext.app.view.detail.person.credits.movie;

import com.glad.watchnext.app.usecase.person.GetPresentationMovieCreditsUseCase;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.NetworkStateProvider.NetworkConnectionState;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;

import android.support.annotation.NonNull;

import java.io.Serializable;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Gautam Lad
 */
public final class MovieCreditsPresenter implements MovieCreditsContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(MovieCreditsPresenter.class);

    private MovieCreditsContract.View view;
    @NonNull private String personId = "";

    @NonNull private final GetPresentationMovieCreditsUseCase useCase;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final NavigationService navigationService;
    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    @NonNull private final CompositeDisposable disposables = new CompositeDisposable();
    private int loadedCount = 0;

    public MovieCreditsPresenter(
            @NonNull final GetPresentationMovieCreditsUseCase useCase,
            @NonNull final SchedulerProvider schedulerProvider,
            @NonNull final NavigationService navigationService,
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        this.useCase = useCase;
        this.schedulerProvider = schedulerProvider;
        this.navigationService = navigationService;
        this.networkStateProvider = networkStateProvider;
        this.log = log;
    }

    //region ViewPresenterContract.Presenter

    @Override
    public void bind(@NonNull final MovieCreditsContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        personId = args[0].toString();
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

    //region MovieCreditsContract.Presenter
    @Override
    public void loadData() {
        log.d(TAG, "loadData() called");
        final boolean networkConnected = networkStateProvider.getState() == NetworkConnectionState.CONNECTED;
        disposables.add(useCase.execute(networkConnected, personId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnSubscribe(ignore -> {
                    loadedCount = 0;
                    view.onLoadStarted();
                })
                .doOnNext(ignore -> ++loadedCount)
                .subscribe(view::onLoaded, view::onError, () -> {
                    view.onLoadComplete();
                    if (loadedCount == 0) {
                        view.onUnsubscribe();
                    }
                }));
    }

    @Override
    public void onClicked(@NonNull final String id) {
        log.d(TAG, "onClicked() called with: id = [" + id + "]");
        navigationService.navigateToMovieDetail(id);
    }
    //endregion MovieCreditsContract.Presenter
}