package com.glad.watchnext.app.view.widget.networkstate;

import com.glad.watchnext.platform.view.ViewPresenterContract;

/**
 * Created by Gautam Lad
 */
public interface NetworkStateWidgetContract {
    interface Presenter extends ViewPresenterContract.Presenter<NetworkStateWidgetContract.View> {
    }

    interface View extends ViewPresenterContract.View {
        void showNetworkRestored();

        void showNetworkDisconnected();
    }
}
