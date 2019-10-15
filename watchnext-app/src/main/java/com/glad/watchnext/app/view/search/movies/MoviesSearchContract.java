package com.glad.watchnext.app.view.search.movies;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.movie.MovieSimplifiedPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface MoviesSearchContract {
    interface Presenter extends ViewPresenterContract.Presenter<MoviesSearchContract.View> {
        void loadData(final boolean ignoreCache);

        void onClicked(@NonNull final String id);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link MovieSimplifiedPresentationModel} has loaded
         */
        void onLoaded(@NonNull final MovieSimplifiedPresentationModel movie);

        /**
         * Called to indicate that loading of content for the view has completed
         */
        void onLoadComplete();

        /**
         * Toggles visibility of the empty content view
         *
         * @param isVisible show empty content view if true, hide otherwise
         */
        void setEmptyContentVisibility(final boolean isVisible);
    }
}