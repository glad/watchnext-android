package com.glad.watchnext.app.view.detail.movie;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.movie.MovieDetailedPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface MovieDetailContract {
    interface Presenter extends ViewPresenterContract.Presenter<MovieDetailContract.View> {
        void loadData(final boolean ignoreCache);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link MovieDetailedPresentationModel} has loaded
         */
        void onLoadComplete(@NonNull final MovieDetailedPresentationModel movie);

        /**
         * Toggles visibility of the empty content view
         *
         * @param isVisible show empty content view if true, hide otherwise
         */
        void setEmptyContentVisibility(final boolean isVisible);
    }
}
