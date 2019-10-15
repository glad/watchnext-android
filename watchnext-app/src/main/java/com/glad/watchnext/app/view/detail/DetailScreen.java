package com.glad.watchnext.app.view.detail;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.domain.service.NavigationService;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * Created by Gautam Lad
 */
public final class DetailScreen implements DetailContract.Screen {
    private DetailView view;

    @Inject NavigationService navigationService;
    @NonNull private final Serializable[] args;

    public DetailScreen(@NonNull final Serializable... args) {
        this.args = args;
        Injector.INSTANCE.inject(this);
    }

    //region ScreenContract.Screen
    @Override
    public DetailView onCreate(@NonNull final Context context) {
        view = new DetailView(context);
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
    public DetailView getView() {
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
        return R.menu.menu_detail_screen;
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final Menu menu, @NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.detail_menuitem_home:
                return navigationService.goBackToRoot();
            case android.R.id.home:
                return navigationService.goBack();
        }
        return false;
    }
    //endregion Menu
}
