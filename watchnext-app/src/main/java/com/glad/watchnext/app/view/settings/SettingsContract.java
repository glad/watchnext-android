package com.glad.watchnext.app.view.settings;

import com.glad.watchnext.platform.view.ScreenContract;
import com.glad.watchnext.platform.view.ViewPresenterContract;

/**
 * Created by Gautam Lad
 */
public interface SettingsContract {
    interface Screen extends ScreenContract.Screen<SettingsView> {
    }

    interface Presenter extends ViewPresenterContract.Presenter<SettingsContract.View> {
        void resetSettings();
    }

    interface View extends ViewPresenterContract.View {
        void onSettingsReset();
    }
}
