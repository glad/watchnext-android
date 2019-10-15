package com.glad.watchnext.app.view.detail;

import com.glad.watchnext.platform.view.ScreenContract;
import com.glad.watchnext.platform.view.ViewPresenterContract;

import android.support.annotation.NonNull;

/**
 * Created by Gautam Lad
 */
public interface DetailContract {
    interface Screen extends ScreenContract.Screen<DetailView> {
    }

    interface Presenter extends ViewPresenterContract.Presenter<DetailContract.View> {
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Called to indicate that the {@link com.glad.watchnext.app.view.model.movie.MovieDetailedPresentationModel} has loaded
         */
        void showMovieDetail(@NonNull final String id);

        /**
         * Called to indicate that the {@link com.glad.watchnext.app.view.model.tv.show.TvShowDetailedPresentationModel} has loaded
         */
        void showTvShowDetail(@NonNull final String id);

        /**
         * Called to indicate that the {@link com.glad.watchnext.app.view.model.person.PersonDetailedPresentationModel} has loaded
         */
        void showPersonDetail(@NonNull final String id);
    }
}
