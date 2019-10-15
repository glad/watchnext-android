package com.glad.watchnext.app.view.widget.networkstate;

import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
public final class NetworkStateWidgetPresenter implements NetworkStateWidgetContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(NetworkStateWidgetPresenter.class);

    private NetworkStateWidgetContract.View view;

    @NonNull private final NetworkStateProvider networkStateProvider;
    @NonNull private final LogService log;

    public NetworkStateWidgetPresenter(
            @NonNull final NetworkStateProvider networkStateProvider,
            @NonNull final LogService log) {
        this.networkStateProvider = networkStateProvider;
        this.log = log;
    }

    //region ViewPresenterContract.Presenter
    @Override
    public void bind(@NonNull final NetworkStateWidgetContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        networkStateProvider.asObservable()
                .subscribe(state -> {
                    switch (state) {
                        case CONNECTED:
                            this.view.showNetworkRestored();
                            break;
                        case DISCONNECTED:
                            this.view.showNetworkDisconnected();
                            break;
                    }
                });
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
    }
    //region ViewPresenterContract.Presenter
}