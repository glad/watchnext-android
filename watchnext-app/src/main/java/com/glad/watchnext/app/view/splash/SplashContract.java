package com.glad.watchnext.app.view.splash;

import com.glad.watchnext.platform.view.ScreenContract;
import com.glad.watchnext.platform.view.ViewPresenterContract;

/**
 * Created by Gautam Lad
 */
public interface SplashContract {
    interface Screen extends ScreenContract.Screen<SplashView> {
    }

    interface Presenter extends ViewPresenterContract.Presenter<SplashContract.View> {
    }

    interface View extends ViewPresenterContract.View {
    }
}
