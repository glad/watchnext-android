package com.glad.watchnext.app.view.settings;

import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
public final class SettingsPresenter implements SettingsContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(SettingsPresenter.class);

    private SettingsContract.View view;

    @NonNull private final SettingsProvider settingsProvider;
    @NonNull private final LogService log;

    public SettingsPresenter(
            @NonNull final SettingsProvider settingsProvider,
            @NonNull final LogService log) {
        this.settingsProvider = settingsProvider;
        this.log = log;
    }

    //region ScreenContract.Presenter
    @Override
    public void bind(@NonNull final SettingsContract.View view, @NonNull final Serializable... args) {
        this.view = view;
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
    }
    //endregion ScreenContract.Presenter

    //region SettingsContract.Presenter

    @Override
    public void resetSettings() {
        log.d(TAG, "resetSettings() called");
        settingsProvider.reset()
                .subscribe(view::onSettingsReset);
    }
    //endregion SettingsContract.Presenter
}