package com.glad.watchnext.app.view.search.adapter;

import com.glad.watchnext.R;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Gautam Lad
 */
public class HistoryViewAdapter extends RecyclerView.Adapter<HistoryViewAdapter.ViewHolder> {
    @NonNull private final List<String> data = new ArrayList<>();
    @NonNull private final PublishSubject<String> clickObservable = PublishSubject.create();
    @NonNull private final PublishSubject<String> deleteObservable = PublishSubject.create();

    public HistoryViewAdapter() {
    }

    @NonNull
    @Override
    public HistoryViewAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        return new HistoryViewAdapter.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_history_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final HistoryViewAdapter.ViewHolder holder, final int position) {
        final String value = data.get(position);
        holder.bind(value);
        holder.deleteImageView.setOnClickListener(i -> {
            delete(position);
            deleteObservable.onNext(value);
        });
        holder.itemView.setOnClickListener(i -> clickObservable.onNext(value));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    public Observable<String> getClickObservable() {
        return clickObservable;
    }

    @NonNull
    public Observable<String> getDeleteObservable() {
        return deleteObservable;
    }

    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    public void addAll(@NonNull final List<String> items) {
        data.addAll(items);
        notifyDataSetChanged();
    }

    private void delete(final int index) {
        data.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, getItemCount() - index);
    }

    //region ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView (R.id.txt_text) TextView textView;
        @BindView (R.id.img_delete) ImageView deleteImageView;

        ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(@NonNull final String value) {
            textView.setText(value);
        }
    }
    //endregion ViewHolder
}