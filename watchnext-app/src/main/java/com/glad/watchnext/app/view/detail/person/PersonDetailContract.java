package com.glad.watchnext.app.view.detail.person;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.person.PersonDetailedPresentationModel;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface PersonDetailContract {
    interface Presenter extends ViewPresenterContract.Presenter<PersonDetailContract.View> {
        void loadData(final boolean ignoreCache);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that loading content for the view has started
         */
        void onLoadStarted();

        /**
         * Called to indicate that the {@link PersonDetailedPresentationModel} has loaded
         */
        void onLoadComplete(@NonNull final PersonDetailedPresentationModel movie);

        /**
         * Toggles visibility of the empty content view
         *
         * @param isVisible show empty content view if true, hide otherwise
         */
        void setEmptyContentVisibility(final boolean isVisible);
    }
}
