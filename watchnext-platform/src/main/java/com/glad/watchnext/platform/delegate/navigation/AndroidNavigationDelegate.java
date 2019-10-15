package com.glad.watchnext.platform.delegate.navigation;

import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.util.ValueHelper;
import com.glad.watchnext.platform.R;
import com.glad.watchnext.platform.util.AnimUtil;
import com.glad.watchnext.platform.view.ScreenContract;
import com.glad.watchnext.platform.view.ScreenContract.Screen;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.Stack;

/**
 * Created by Gautam Lad
 */
public class AndroidNavigationDelegate implements NavigationDelegate, LifecycleObserver {
    @NonNull private static final String TAG = AndroidNavigationDelegate.class.getSimpleName();

    @NonNull private final AppCompatActivity activity;
    @IdRes private final int screenContainerId;
    @NonNull private final Stack<Screen> screenStack;
    @NonNull private final LogService log;
    @Nullable private Menu menu;

    @SuppressWarnings ("NullableProblems") @NonNull private ViewGroup screenContainer;

    public AndroidNavigationDelegate(
            @NonNull final AppCompatActivity activity,
            @IdRes final int screenContainerId,
            @NonNull final Stack<ScreenContract.Screen> screenStack,
            @NonNull final LogService log) {
        this.activity = activity;
        this.screenContainerId = screenContainerId;
        this.screenStack = screenStack;
        this.log = log;
        this.activity.getLifecycle().addObserver(this);
    }

    //region NavigationDelegate

    /**
     * Navigates to the specified {@link ScreenContract.Screen}
     *
     * @param screen The {@link ScreenContract.Screen} to navigate to
     */
    @Override
    public void goTo(@NonNull final ScreenContract.Screen screen, final boolean clearHistory) {
        final ScreenContract.Screen oldScreen;
        if (screenStack.isEmpty()) {
            oldScreen = null;
        } else {
            oldScreen = clearHistory ? screenStack.pop() : currentScreen();
            AnimUtil.create(oldScreen.getView(), R.anim.left_out, animation -> hideScreen(oldScreen));
        }

        // Add a new screen and view to the top of the stack and container respectively
        final ScreenContract.Screen newScreen = screenStack.push(screen);
        final View newView = newScreen.onCreate(ValueHelper.requireNonNull(activity));
        screenContainer.addView(newView);
        AnimUtil.create(newView, R.anim.right_in, animation -> {
            showScreen(newScreen);

            if (clearHistory && oldScreen != null) {
                destroyScreen(oldScreen);
            }
        });
    }

    /**
     * @return True if back navigation was successful, false otherwise
     */
    @Override
    public boolean goBack() {
        // If there's no more screenStack beyond this, we're done
        if (screenStack.size() == 1) {
            return false;
        }

        // Destroy the current view on top of stack
        final ScreenContract.Screen oldScreen = screenStack.pop();
        AnimUtil.create(oldScreen.getView(), R.anim.right_out, animation -> destroyScreen(oldScreen));

        final ScreenContract.Screen newScreen = currentScreen();
        AnimUtil.create(newScreen.getView(), R.anim.left_in, animation -> showScreen(newScreen));

        return true;
    }

    /**
     * @return True if back navigation all the way to first screen in stack was successful, false otherwise
     */
    @Override
    public boolean goBackToRoot() {
        // If there's no more screenStack beyond this, we're done
        if (screenStack.size() == 1) {
            return false;
        }

        // Destroy the current view on top of stack
        for (int i = screenStack.size() - 1; i >= 1; i--) {
            final ScreenContract.Screen oldScreen = screenStack.pop();
            destroyScreen(oldScreen);
        }

        final ScreenContract.Screen newScreen = currentScreen();
        AnimUtil.create(newScreen.getView(), R.anim.left_in, animation -> showScreen(newScreen));

        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull final Object menu) {
        final int menuResource = currentScreen().getMenuResourceId();
        if (menuResource != -1) {
            ValueHelper.requireNonNull(activity).getMenuInflater().inflate(menuResource, this.menu = (Menu) menu);
            return true;
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull final Object item) {
        return menu != null && currentScreen().onMenuItemSelected(menu, (MenuItem) item);
    }
    //endregion NavigationDelegate

    //region Life Cycle
    @OnLifecycleEvent (Event.ON_CREATE)
    public void onCreate() {
        log.d(TAG, "onCreate() called");

        screenContainer = activity.findViewById(screenContainerId);
        if (screenContainer == null) {
            throw new RuntimeException("Cannot find screen container id");
        }

        // Create all the views in the (initial) stack
        for (ScreenContract.Screen screen : screenStack) {
            final View view = screen.onCreate(activity);
            view.setVisibility(View.INVISIBLE);
            screenContainer.addView(view);
        }
    }

    @OnLifecycleEvent (Event.ON_RESUME)
    public void onResume() {
        log.d(TAG, "onResume() called");
        resumeScreen(currentScreen());
    }

    @OnLifecycleEvent (Event.ON_PAUSE)
    public void onPause() {
        log.d(TAG, "onPause() called");
        hideScreen(currentScreen());
    }

    @OnLifecycleEvent (Event.ON_DESTROY)
    public void onDestroy() {
        log.d(TAG, "onDestroy() called");
        ValueHelper.requireNonNull(activity).getLifecycle().removeObserver(this);

        // Destroy all screenStack
        for (int i = screenStack.size() - 1; i >= 0; i--) {
            destroyScreen(screenStack.elementAt(i));
        }
    }
    //endregion Life Cycle

    //region Helper Methods

    /**
     * Resumes the screen when the activity is resuming
     *
     * @param screen The {@link ScreenContract.Screen} to resume
     */
    private void resumeScreen(@NonNull final ScreenContract.Screen screen) {
        final View view = screen.getView();
        screen.onSubscribe();
        view.setVisibility(View.VISIBLE);
    }

    /**
     * Shows the screen provided and calls on the activity to invalidate its menu to allow new menu to render
     *
     * @param screen The {@link ScreenContract.Screen} to show
     */
    private void showScreen(@NonNull final ScreenContract.Screen screen) {
        resumeScreen(screen);

        if (screen.getMenuResourceId() != -1) {
            ValueHelper.requireNonNull(activity).invalidateOptionsMenu();
        }
    }

    /**
     * Hides the screen provided
     *
     * @param screen The {@link ScreenContract.Screen} to hide
     */
    private void hideScreen(@NonNull final ScreenContract.Screen screen) {
        final View view = screen.getView();
        screen.onUnsubscribe();
        view.setVisibility(View.INVISIBLE);
    }

    /**
     * Destroys a screen and removes its view from the container
     *
     * @param screen The {@link ScreenContract.Screen} to destroy
     */
    private void destroyScreen(@NonNull final ScreenContract.Screen screen) {
        final View view = screen.getView();
        final ViewGroup viewParent = (ViewGroup) view.getParent();
        viewParent.removeView(view);
        screenContainer.removeView(view);
        screen.onDestroy();
    }

    @NonNull
    private ScreenContract.Screen currentScreen() {
        return screenStack.peek();
    }
    //endregion Helper Methods
}