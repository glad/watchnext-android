package com.glad.watchnext.platform.delegate.navigation;

import com.glad.watchnext.platform.view.ScreenContract;

import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by Gautam Lad
 */
public interface NavigationDelegate {
    /**
     * @return True if navigating to previous screen was successful, false otherwise
     */
    boolean goBack();

    /**
     * @return True if navigating to screen at the top of stack was successful, false otherwise
     */
    boolean goBackToRoot();

    /**
     * Goes to a the specified screen with the given options
     *
     * @param screen       The {@link ScreenContract.Screen} to go to
     * @param clearHistory If true, the history will be cleared after navigating
     */
    void goTo(@NonNull ScreenContract.Screen screen, final boolean clearHistory);

    /**
     * See {@link android.app.Activity#onCreateOptionsMenu(Menu)}
     */
    boolean onCreateOptionsMenu(final Object menu);

    /**
     * See {@link android.app.Activity#onOptionsItemSelected(MenuItem)}
     */
    boolean onOptionsItemSelected(final Object item);
}
