package com.glad.watchnext.app.view.detail.similar.tv.shows;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.tv.show.TvShowSimplifiedPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface SimilarTvShowsContract {
    interface Presenter extends ViewPresenterContract.Presenter<SimilarTvShowsContract.View> {
        void loadData();

        void onClicked(@NonNull final String id);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link TvShowSimplifiedPresentationModel} has loaded
         */
        void onLoaded(@NonNull final TvShowSimplifiedPresentationModel movie);

        /**
         * Called to indicate that loading of content for the view has completed
         */
        void onLoadComplete();

        /**
         * Sets visibility of the view
         *
         * @param isVisible if true show view, hide otherwise
         */
        void setVisibility(final boolean isVisible);
    }
}
