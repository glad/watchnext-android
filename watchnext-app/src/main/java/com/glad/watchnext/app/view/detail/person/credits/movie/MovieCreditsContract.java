package com.glad.watchnext.app.view.detail.person.credits.movie;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.person.MovieCreditPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface MovieCreditsContract {
    interface Presenter extends ViewPresenterContract.Presenter<MovieCreditsContract.View> {
        void loadData();

        void onClicked(@NonNull final String id);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link MovieCreditPresentationModel} has loaded
         */
        void onLoaded(@NonNull final MovieCreditPresentationModel credit);

        /**
         * Called to indicate that loading of content for the view has completed
         */
        void onLoadComplete();
    }
}
