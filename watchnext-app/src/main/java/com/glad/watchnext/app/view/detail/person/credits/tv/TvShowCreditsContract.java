package com.glad.watchnext.app.view.detail.person.credits.tv;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.person.TvShowCreditPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface TvShowCreditsContract {
    interface Presenter extends ViewPresenterContract.Presenter<TvShowCreditsContract.View> {
        void loadData();

        void onClicked(@NonNull final String id);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link TvShowCreditPresentationModel} has loaded
         */
        void onLoaded(@NonNull final TvShowCreditPresentationModel credit);

        /**
         * Called to indicate that loading of content for the view has completed
         */
        void onLoadComplete();
    }
}
