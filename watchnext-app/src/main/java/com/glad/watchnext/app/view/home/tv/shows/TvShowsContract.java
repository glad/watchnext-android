package com.glad.watchnext.app.view.home.tv.shows;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.common.CollectionPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface TvShowsContract {
    interface Presenter extends ViewPresenterContract.Presenter<TvShowsContract.View> {
        void loadData();
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link CollectionPresentationModel} has loaded
         */
        void onCollectionLoaded(@NonNull final CollectionPresentationModel collection);

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