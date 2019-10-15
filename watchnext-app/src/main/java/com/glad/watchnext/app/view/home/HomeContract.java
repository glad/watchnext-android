package com.glad.watchnext.app.view.home;

import com.glad.watchnext.platform.view.ScreenContract;
import com.glad.watchnext.platform.view.ViewPresenterContract;

/**
 * Created by Gautam Lad
 */
public interface HomeContract {
    interface Screen extends ScreenContract.Screen<HomeView> {
    }

    interface Presenter extends ViewPresenterContract.Presenter<HomeContract.View> {
        /**
         * Called when Movies button is clicked on bottom navigation
         */
        void onMoviesClicked();

        /**
         * Called when Tv Shows button is clicked on bottom navigation
         */
        void onTvShowsClicked();

        /**
         * Called when People button is clicked on bottom navigation
         */
        void onPeopleClicked();
    }

    interface View extends ViewPresenterContract.View {
        /**
         * Show the Movies view
         */
        void showMovies();

        /**
         * Show the Tv Shows view
         */
        void showTvShows();

        /**
         * Show the People view
         */
        void showPeople();
    }
}