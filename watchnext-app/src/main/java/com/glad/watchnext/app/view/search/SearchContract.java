package com.glad.watchnext.app.view.search;

import com.glad.watchnext.platform.view.ScreenContract;
import com.glad.watchnext.platform.view.ViewPresenterContract;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Gautam Lad
 */
public interface SearchContract {
    interface Screen extends ScreenContract.Screen<SearchView> {
    }

    interface Presenter extends ViewPresenterContract.Presenter<SearchContract.View> {
        void queryChanged(@NonNull final String query);

        void deleteHistory(@NonNull final String query);
    }

    interface View extends ViewPresenterContract.View {
        /**
         * @param items A {@link List<String>} of previously searched items
         */
        void onSearchHistoryLoaded(@NonNull final List<String> items);

        /**
         * Called to indicate that search should be performed
         */
        void onPerformSearch(final String query);

        /**
         * Toggles visibility of the results content view
         *
         * @param isVisible show history content view if true, hide otherwise
         */
        void setResultsVisibility(final boolean isVisible);

        /**
         * Toggles visibility of the history content view
         *
         * @param isVisible show history content view if true, hide otherwise
         */
        void setHistoryVisibility(final boolean isVisible);
    }
}