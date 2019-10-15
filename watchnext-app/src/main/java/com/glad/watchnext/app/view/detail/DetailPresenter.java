package com.glad.watchnext.app.view.detail;

import com.glad.watchnext.app.util.LogUtil;
import com.glad.watchnext.app.view.model.movie.MovieDetailedPresentationModel;
import com.glad.watchnext.app.view.model.person.PersonDetailedPresentationModel;
import com.glad.watchnext.app.view.model.tv.show.TvShowDetailedPresentationModel;
import com.glad.watchnext.domain.service.LogService;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by Gautam Lad
 */
public final class DetailPresenter implements DetailContract.Presenter {
    @NonNull private static final String TAG = LogUtil.getTag(DetailPresenter.class);

    private DetailContract.View view;
    @NonNull private String idToLoad = "";
    private Class typeToLoad;

    @NonNull private final LogService log;

    private boolean isLoaded;

    public DetailPresenter(@NonNull final LogService log) {
        this.log = log;
    }

    //region ViewPresenterContract.Presenter
    @Override
    public void bind(@NonNull final DetailContract.View view, @NonNull final Serializable... args) {
        this.view = view;
        idToLoad = args[0].toString();
        try {
            typeToLoad = Class.forName(args[1].toString());
        } catch (final ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void subscribe() {
        log.d(TAG, "subscribe() called");
        if (isLoaded) {
            log.w(TAG, "subscribe: View already loaded");
            return;
        }
        isLoaded = true;

        if (typeToLoad.equals(MovieDetailedPresentationModel.class)) {
            view.showMovieDetail(idToLoad);
        } else if (typeToLoad.equals(TvShowDetailedPresentationModel.class)) {
            view.showTvShowDetail(idToLoad);
        } else if (typeToLoad.equals(PersonDetailedPresentationModel.class)) {
            view.showPersonDetail(idToLoad);
        } else {
            throw new RuntimeException("Unrecognized type = [" + typeToLoad + "]");
        }
    }

    @Override
    public void unsubscribe() {
        log.d(TAG, "unsubscribe() called");
    }
    //endregion ViewPresenterContract.Presenter
}