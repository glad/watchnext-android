package com.glad.watchnext.platform.view;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * View and Presenter contacts for {@link android.app.Activity} and {@link android.app.Fragment}
 * <p>
 * Created by Gautam Lad
 */
public interface ViewPresenterContract {
    interface Presenter<V extends ViewPresenterContract.View> {
        /**
         * Bind a {@link ViewPresenterContract.View} to the {@link ViewPresenterContract.Presenter} on creation
         *
         * @param view The {@link ViewPresenterContract.View} bound to this presenter
         * @param args A list of user-defined arguments to pass to the presenter
         */
        void bind(@NonNull final V view, @NonNull final Serializable... args);

        /**
         * Called when view is available for interaction
         */
        void subscribe();

        /**
         * Called when view is no longer available for interaction
         */
        void unsubscribe();
    }

    /**
     * Represents a view which is what gets rendered on screen
     */
    interface View {
        /**
         * Called after the view is created
         *
         * @param args A list of user-defined arguments to pass to the presenter
         */
        void onCreated(@NonNull final Serializable... args);

        /**
         * Called when view is being shown
         */
        void onSubscribe();

        /**
         * Called when the view is being hidden
         */
        void onUnsubscribe();

        /**
         * Called when the view is being destroyed as a result of its parent {@link ScreenContract.Screen}
         * being destroyed or if the application encounters a configuration change (e.g. orientation change)
         */
        void onDestroy();

        /**
         * Called to handle errors
         *
         * @param throwable The {@link Throwable} thrown as a result of error
         */
        void onError(@NonNull final Throwable throwable);
    }
}