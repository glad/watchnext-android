package com.glad.watchnext.app.view.imageviewer;

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
public final class ImageViewerScreen implements ImageViewerContract.Screen {
    private ImageViewerView view;

    @Inject NavigationService navigationService;
    @NonNull private final Serializable[] args;

    public ImageViewerScreen(@NonNull final Serializable... args) {
        this.args = args;
        Injector.INSTANCE.inject(this);
    }

    //region ScreenContract.Screen
    @Override
    public ImageViewerView onCreate(@NonNull final Context context) {
        view = new ImageViewerView(context);
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
    public ImageViewerView getView() {
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
        return R.menu.menu_imageviewer_screen;
    }

    @Override
    public boolean onMenuItemSelected(@NonNull final Menu menu, @NonNull final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.imageviewer_menuitem_home:
                navigationService.goBackToRoot();
                break;
            case android.R.id.home:
                navigationService.goBack();
                return true;
        }
        return false;
    }
    //endregion Menu
}
