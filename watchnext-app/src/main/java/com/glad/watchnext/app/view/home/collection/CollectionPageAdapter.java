package com.glad.watchnext.app.view.home.collection;

import com.glad.watchnext.platform.view.ViewPresenterContract;
import com.glad.watchnext.app.view.model.common.CollectionPresentationModel;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gautam Lad
 */
public final class CollectionPageAdapter<V extends ViewGroup & ViewPresenterContract.View> extends PagerAdapter {
    @NonNull private final List<SimpleEntry<CollectionPresentationModel, V>> data = new ArrayList<>();

    public void add(@NonNull final CollectionPresentationModel collection, @NonNull final V item) {
        data.add(new SimpleEntry<>(collection, item));
        notifyDataSetChanged();
    }

    public void clear() {
        for (final SimpleEntry<CollectionPresentationModel, V> entry : data) {
            entry.getValue().onDestroy();
        }
        data.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull final View view, @NonNull final Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, final int position) {
        final V view = data.get(position).getValue();
        container.addView(view);
        view.onSubscribe();
        return view;
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(final int position) {
        return data.get(position).getKey().getName();
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup container, int position, @NonNull final Object object) {
        @SuppressWarnings ("unchecked") final V view = (V) object;
        view.onUnsubscribe();
        container.removeView(view);
    }
}