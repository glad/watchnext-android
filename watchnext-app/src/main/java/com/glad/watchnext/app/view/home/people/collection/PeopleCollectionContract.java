package com.glad.watchnext.app.view.home.people.collection;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.person.PersonSimplifiedPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface PeopleCollectionContract {
    interface Presenter extends ViewPresenterContract.Presenter<PeopleCollectionContract.View> {
        void loadData(final boolean ignoreCache);

        void onClicked(@NonNull final String id);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link PersonSimplifiedPresentationModel} has loaded
         */
        void onLoaded(@NonNull final PersonSimplifiedPresentationModel person);

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