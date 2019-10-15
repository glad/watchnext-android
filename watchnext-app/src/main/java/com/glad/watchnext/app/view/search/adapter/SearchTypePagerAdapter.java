package com.glad.watchnext.app.view.search.adapter;

import com.glad.watchnext.platform.view.ViewPresenterContract;

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
public class SearchTypePagerAdapter<V extends View> extends PagerAdapter {
    @NonNull private final List<SimpleEntry<String, V>> data = new ArrayList<>();

    public void add(@NonNull final String type, @NonNull final V item) {
        data.add(new SimpleEntry<>(type, item));
        notifyDataSetChanged();
    }

    public void clear() {
        for (final SimpleEntry<String, V> entry : data) {
            ((ViewPresenterContract.View) entry.getValue()).onDestroy();
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
        ((ViewPresenterContract.View) view).onSubscribe();
        return view;
    }

    @NonNull
    @Override
    public CharSequence getPageTitle(final int position) {
        return data.get(position).getKey();
    }

    @Override
    public void destroyItem(@NonNull final ViewGroup container, int position, @NonNull final Object object) {
        @SuppressWarnings ("unchecked") final V view = (V) object;
        ((ViewPresenterContract.View) view).onUnsubscribe();
        container.removeView(view);
    }
}