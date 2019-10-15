package com.glad.watchnext.app.view.splash;

import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
@SuppressWarnings ("FieldCanBeLocal")
public final class SplashPresenter implements SplashContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(SplashPresenter.class);

    @NonNull private final LogService log;

    private SplashContract.View view;

    public SplashPresenter(@NonNull final LogService log) {
        this.log = log;
    }

    //region ViewPresenterContract.Presenter
    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
    }

    @Override
    public void bind(@NonNull final SplashContract.View view, @NonNull final Serializable... args) {
        this.view = view;
    }
    //region ViewPresenterContract.Presenter
}
