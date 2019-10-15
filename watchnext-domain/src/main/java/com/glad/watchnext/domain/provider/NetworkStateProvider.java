package com.glad.watchnext.domain.provider;

import io.reactivex.Observable;

/**
 * Created by Gautam Lad
 */
public interface NetworkStateProvider {
    /**
     * Connection state
     */
    enum NetworkConnectionState {
        CONNECTED,
        DISCONNECTED
    }

    /**
     * Connection type
     */
    enum NetworkConnectionType {
        UNKNOWN,
        WIFI,
        MOBILE
    }

    /**
     * @return The current connection type
     */
    NetworkConnectionType getType();

    /**
     * @return The current connection state
     */
    NetworkConnectionState getState();

    /**
     * @return An observable for the current connection state
     */
    Observable<NetworkConnectionState> asObservable();
}
