package com.glad.watchnext.app.view.detail.tv.show;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.tv.show.TvShowDetailedPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface TvShowDetailContract {
    interface Presenter extends ViewPresenterContract.Presenter<TvShowDetailContract.View> {
        void loadData(final boolean ignorCache);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link TvShowDetailedPresentationModel} has loaded
         */
        void onLoadComplete(@NonNull final TvShowDetailedPresentationModel movie);

        /**
         * Toggles visibility of the empty content view
         *
         * @param isVisible show empty content view if true, hide otherwise
         */
        void setEmptyContentVisibility(final boolean isVisible);
    }
}
