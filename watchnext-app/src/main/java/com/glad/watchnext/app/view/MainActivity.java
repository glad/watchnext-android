package com.glad.watchnext.app.view;

import com.glad.watchnext.R;
import com.glad.watchnext.app.di.Injector;
import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.widget.networkstate.NetworkStateWidgetView;
import com.glad.watchnext.domain.provider.SchedulerProvider;
import com.glad.watchnext.domain.provider.SettingsProvider;
import com.glad.watchnext.domain.service.LogService;
import com.glad.watchnext.domain.service.NavigationService;
import com.glad.watchnext.platform.delegate.navigation.NavigationDelegate;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gautam Lad
 */
public final class MainActivity extends AppCompatActivity {
    @NonNull private static final String TAG = LogUtil.getTag(MainActivity.class);
    @Inject NavigationService navigationService;
    @Inject NavigationDelegate navigationDelegate;
    @Inject SchedulerProvider schedulerProvider;
    @Inject SettingsProvider settingsProvider;
    @Inject LogService log;

    @BindView (R.id.network_state_container) NetworkStateWidgetView networkStateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Injector.INSTANCE.inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        settingsProvider.init()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe();

        networkStateView.onCreated();
    }

    @Override
    protected void onDestroy() {
        log.d(TAG, "onDestroy() called");
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        log.d(TAG, "onPause() called");
        networkStateView.onUnsubscribe();
        super.onPause();
    }

    @Override
    protected void onResume() {
        log.d(TAG, "onResume() called");
        super.onResume();
        networkStateView.onSubscribe();
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        return navigationDelegate.onCreateOptionsMenu(menu) || super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        return navigationDelegate.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!navigationService.goBack()) {
            super.onBackPressed();
        }
    }
}