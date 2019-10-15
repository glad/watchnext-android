package com.glad.watchnext.platform.provider;

import com.glad.watchnext.domain.provider.NetworkStateProvider;
import com.glad.watchnext.domain.provider.SchedulerProvider;

import android.arch.lifecycle.Lifecycle.Event;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.atomic.AtomicReference;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Gautam Lad
 */
public final class AndroidNetworkStateProvider implements NetworkStateProvider, LifecycleObserver {
    @NonNull private final AppCompatActivity activity;
    @NonNull private final SchedulerProvider schedulerProvider;
    @NonNull private final BroadcastReceiver networkStateReceiver;

    @NonNull private final AtomicReference<NetworkConnectionState> currentState;
    @NonNull private final PublishSubject<NetworkConnectionState> connectionStateSubject = PublishSubject.create();

    public AndroidNetworkStateProvider(
            @NonNull final AppCompatActivity activity,
            @NonNull final SchedulerProvider schedulerProvider) {
        this.activity = activity;
        this.schedulerProvider = schedulerProvider;

        currentState = new AtomicReference<>(NetworkConnectionState.CONNECTED);
        connectionStateSubject.onNext(currentState.get());

        networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(@NonNull final Context context, @NonNull final Intent intent) {
                final NetworkConnectionState newState = getState();
                if (!currentState.getAndSet(newState).equals(newState)) {
                    connectionStateSubject.onNext(newState);
                }
            }
        };

        this.activity.getLifecycle().addObserver(new LifecycleObserver() {
            @OnLifecycleEvent (Event.ON_RESUME)
            public void onResume() {
                activity.registerReceiver(networkStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            }

            @OnLifecycleEvent (Event.ON_PAUSE)
            public void onPause() {
                activity.unregisterReceiver(networkStateReceiver);
            }
        });
    }

    @Override
    public NetworkConnectionType getType() {
        if (isWifiConnected()) {
            return NetworkConnectionType.WIFI;
        } else if (isMobileConnected()) {
            return NetworkConnectionType.MOBILE;
        } else {
            return NetworkConnectionType.UNKNOWN;
        }
    }

    @Override
    public NetworkConnectionState getState() {
        if (isWifiConnected() || isMobileConnected()) {
            return NetworkConnectionState.CONNECTED;
        } else {
            return NetworkConnectionState.DISCONNECTED;
        }
    }

    @Override
    public Observable<NetworkConnectionState> asObservable() {
        return connectionStateSubject
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui());
    }

    //region Helper Methods
    private boolean isWifiConnected() {
        final ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            final NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }

    private boolean isMobileConnected() {
        final ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            final NetworkInfo networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            return networkInfo != null && networkInfo.isConnected();
        }

        return false;
    }
    //endregion Helper Methods
}