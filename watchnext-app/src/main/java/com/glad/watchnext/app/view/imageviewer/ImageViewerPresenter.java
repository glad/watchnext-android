package com.glad.watchnext.app.view.imageviewer;

import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
public final class ImageViewerPresenter implements ImageViewerContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(ImageViewerPresenter.class);

    private ImageViewerContract.View view;

    @NonNull private final LogService log;

    public ImageViewerPresenter(@NonNull final LogService log) {
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
    public void bind(@NonNull final ImageViewerContract.View view, @NonNull final Serializable... args) {
        this.view = view;
    }
    //endregion ViewPresenterContract.Presenter
}