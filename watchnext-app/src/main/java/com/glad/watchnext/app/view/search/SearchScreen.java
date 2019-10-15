package com.glad.watchnext.app.view.search;

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
public final class SearchScreen implements SearchContract.Screen {
    private SearchView view;

    @NonNull private final Serializable[] args;

    @Inject NavigationService navigationService;
    @Inject @Named ("isListView") ReplaySubject<Boolean> isListViewSubject;

    public SearchScreen(@NonNull final Serializable... args) {
        this.args = args;
        Injector.INSTANCE.inject(this);
    }

    //region ScreenContract.Screen
    @Override
    public SearchView onCreate(@NonNull final Context context) {
        view = new SearchView(context);
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
    public SearchView getView() {
        return view;
    }
    //endregion Search

    //endregion ScreenContract.Screen

    //region Menu
    @IdRes
    @Override
    public int getSearchViewMenuItemResourceId() {
        return -1; // R.id.menuitem_search;
    }

    @MenuRes
    @Override
    public int getMenuResourceId() {
        return -1; // R.menu.menu_search;
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final Menu menu, @NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navigationService.goBack();
                return true;
        }
        return false;
    }
    //endregion Menu
}