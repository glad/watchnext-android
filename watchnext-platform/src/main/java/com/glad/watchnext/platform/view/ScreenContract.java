package com.glad.watchnext.platform.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

/**
 * Contract interfaces required for Screen -> Presenter -> View
 * <p>
 * Created by Gautam Lad
 */
public interface ScreenContract {
    /**
     * Represents a screen which contains a {@link ViewPresenterContract.View}
     */
    interface Screen<V extends ViewGroup & ViewPresenterContract.View> {
        /**
         * Called when the screen is is being created
         *
         * @param context The {@link Context} receiving the call
         *
         * @return A {@link ViewPresenterContract.View} created for the screen
         */
        V onCreate(@NonNull final Context context);

        /**
         * @return An instance of {@link ViewPresenterContract.View} created by the screen in {{@link #onCreate(Context)}}
         */
        V getView();

        /**
         * Called when the screen is being moved to the top of the stack and is being made visible
         */
        void onSubscribe();

        /**
         * Called when the screen is being moved to the back of the stack or when the application is being backgrounded
         */
        void onUnsubscribe();

        /**
         * Called when the screen needs to be destroyed as a result of being removed from the stack
         */
        void onDestroy();

        /**
         * @return The id of the search menu item
         */
        @IdRes
        int getSearchViewMenuItemResourceId();

        /**
         * @return The id of the menu resource to create
         */
        @MenuRes
        int getMenuResourceId();

        /**
         * Called when a menu item is selected
         *
         * @param menu The {@link Menu} the {@link MenuItem} belongs to
         * @param item The {@link MenuItem} that was selected
         */
        boolean onMenuItemSelected(@NonNull final Menu menu, @NonNull final MenuItem item);
    }
}