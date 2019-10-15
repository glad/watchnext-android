package com.glad.watchnext.app.view.settings;

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
public final class SettingsScreen implements SettingsContract.Screen {
    private SettingsView view;

    @Inject NavigationService navigationService;

    public SettingsScreen(@NonNull final Serializable... args) {
        Injector.INSTANCE.inject(this);
    }

    //region ScreenContract.Screen
    @Override
    public SettingsView onCreate(@NonNull final Context context) {
        view = new SettingsView(context);
        view.onCreated();
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
    public SettingsView getView() {
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
        return R.menu.menu_settings;
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final Menu menu, @NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menuitem_reset:
                view.resetSettings();
                return true;
            case android.R.id.home:
                navigationService.goBack();
                return true;
        }
        return false;
    }
    //endregion Menu
}