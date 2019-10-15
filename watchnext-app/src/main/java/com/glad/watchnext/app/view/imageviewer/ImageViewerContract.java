package com.glad.watchnext.app.view.imageviewer;

import com.glad.watchnext.platform.view.ScreenContract;
import com.glad.watchnext.platform.view.ViewPresenterContract;

/**
 * Created by Gautam Lad
 */
public interface ImageViewerContract {
    interface Screen extends ScreenContract.Screen<ImageViewerView> {
    }

    interface Presenter extends ViewPresenterContract.Presenter<ImageViewerContract.View> {
    }

    interface View extends ViewPresenterContract.View {
    }
}
