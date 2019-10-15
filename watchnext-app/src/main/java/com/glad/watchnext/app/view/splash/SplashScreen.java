package com.glad.watchnext.app.view.splash;

import com.glad.watchnext.app.di.Injector;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
public final class SplashScreen implements SplashContract.Screen {
    private SplashView view;

    @NonNull private final Serializable[] args;

    public SplashScreen(@NonNull final Serializable... args) {
        this.args = args;
        Injector.INSTANCE.inject(this);
    }

    //region ScreenContract.Screen
    @Override
    public SplashView onCreate(@NonNull final Context context) {
        view = new SplashView(context);
        view.onCreated(args);
        return view;
    }

    @Override
    public void onUnsubscribe() {
        view.onUnsubscribe();
    }

    @Override
    public void onSubscribe() {
        view.onSubscribe();
    }

    @Override
    public void onDestroy() {
        view.onDestroy();
    }

    @NonNull
    @Override
    public SplashView getView() {
        return view;
    }
    //endregion ScreenContract.Screen

    //region Menu
    @IdRes
    @Override
    public int getSearchViewMenuItemResourceId() {
        return -1;
    }

    @MenuRes
    @Override
    public int getMenuResourceId() {
        return -1;
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final Menu menu, @NonNull final MenuItem item) {
        return false;
    }
    //endregion Menu
}
