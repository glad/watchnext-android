package com.glad.watchnext.app.view.home;

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
import javax.inject.Named;

import io.reactivex.subjects.ReplaySubject;

/**
 * Created by Gautam Lad
 */
public final class HomeScreen implements HomeContract.Screen {
    private HomeView view;

    @NonNull private final Serializable[] args;

    @Inject NavigationService navigationService;
    @Inject @Named ("isListView") ReplaySubject<Boolean> isListViewSubject;

    public HomeScreen(@NonNull final Serializable... args) {
        this.args = args;
        Injector.INSTANCE.inject(this);
    }

    //region ScreenContract.Screen
    @Override
    public HomeView onCreate(@NonNull final Context context) {
        view = new HomeView(context);
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
    public HomeView getView() {
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
        return R.menu.menu_home;
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final Menu menu, @NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                view.openDrawer();
                return true;

            case R.id.home_menuitem_search:
                navigationService.navigateToSearch();
                return true;

            case R.id.home_menuitem_grid_list_view_toggle:
                final boolean isChecked = !item.isChecked();
                item.setChecked(isChecked);
                isListViewSubject.onNext(isChecked);
                if (isChecked) {
                    item.setIcon(R.drawable.ic_action_gridview);
                } else {
                    item.setIcon(R.drawable.ic_action_listview);
                }
                return true;
        }
        return false;
    }
    //endregion Menu
}