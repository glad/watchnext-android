package com.glad.watchnext.app.widget;

import com.glad.watchnext.R;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by Gautam Lad
 */
public class SearchBarWidget extends ConstraintLayout {
    @BindView (R.id.btn_search) ImageView searchButton;
    @BindView (R.id.btn_close) ImageView closeButton;
    @BindView (R.id.txt_query) SearchBarEditText queryTextView;

    public SearchBarWidget(final Context context) {
        this(context, null);
    }

    public SearchBarWidget(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBarWidget(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.layout_search_bar, this);
        ButterKnife.bind(this);

        SearchBarEditText.queryChangeSubject.doOnNext(s -> {
            closeButton.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            searchButton.setImageDrawable(s.length() > 0 ?
                    getResources().getDrawable(R.drawable.ic_back, null) :
                    getResources().getDrawable(R.drawable.ic_search, null));
        });
        closeButton.setOnClickListener(v -> queryTextView.setText(""));
        searchButton.setOnClickListener(v -> queryTextView.setText(""));
    }

    @NonNull
    public Observable<String> getQueryChangeObservable() {
        return SearchBarEditText.queryChangeSubject;
    }

    /**
     * Sets the text
     *
     * @param text The text to set
     */
    public void setText(@NonNull final String text) {
        queryTextView.setText(text);
    }

    public static class SearchBarEditText extends AppCompatEditText {
        @NonNull private static final PublishSubject<String> queryChangeSubject = PublishSubject.create();

        public SearchBarEditText(final Context context) {
            this(context, null);
        }

        public SearchBarEditText(final Context context, final AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public SearchBarEditText(final Context context, final AttributeSet attrs, final int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            setHint(R.string.search_hint);
        }

        @Override
        protected void onTextChanged(final CharSequence text, final int start, final int lengthBefore, final int lengthAfter) {
            super.onTextChanged(text, start, lengthBefore, lengthAfter);
            queryChangeSubject.onNext(text.toString());
        }
    }
}
