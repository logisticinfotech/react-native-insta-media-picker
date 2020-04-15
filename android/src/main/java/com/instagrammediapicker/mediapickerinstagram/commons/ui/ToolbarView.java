package com.instagrammediapicker.mediapickerinstagram.commons.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.instagrammediapicker.R;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;

public class ToolbarView extends RelativeLayout {

    CustomTextView mTitle;
    //    @BindView(R.id.mIconBack)
    CustomTextView mIconBack;
    //    @BindView(R.id.mIconNext)
    CustomTextView mIconNext;

    private WeakReference<OnClickBackListener> mWrBackMenuListener;
    private WeakReference<OnClickNextListener> mWrNextListener;
    private WeakReference<OnClickTitleListener> mWrTitleListener;

//    @OnClick(R.id.mIconBack)
//    void onClickBack() {
//        mWrBackMenuListener.get().onClickBack();
//    }
//
//    @OnClick(R.id.mTitle)
//    void onClickTitle() {
//        mWrTitleListener.get().onClickTitle();
//    }
//
//    @OnClick(R.id.mIconNext)
//    void onClickNext() {
//        mWrNextListener.get().onClickNext();
//    }


    private void init(Context context) {
        View view = View.inflate(context, R.layout.toolbar_view, this);

        mTitle = (CustomTextView) view.findViewById(R.id.mTitle);
//    @BindView(R.id.mIconBack)
        mIconBack = (CustomTextView) view.findViewById(R.id.mIconBack);
//    @BindView(R.id.mIconNext)
        mIconNext = (CustomTextView) view.findViewById(R.id.mIconNext);

//        ButterKnife.bind(this, view);
        mTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWrTitleListener.get().onClickTitle();
            }
        });

        mIconBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWrBackMenuListener.get().onClickBack();
            }
        });

        mIconNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mWrNextListener.get().onClickNext();
            }
        });
    }

    public ToolbarView(Context context) {
        super(context);
        init(context);
    }

    public ToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ToolbarView setOnClickBackMenuListener(OnClickBackListener listener) {
        this.mWrBackMenuListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setOnClickTitleListener(OnClickTitleListener listener) {
        this.mWrTitleListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setOnClickNextListener(OnClickNextListener listener) {
        this.mWrNextListener = new WeakReference<>(listener);
        return this;
    }

    public ToolbarView setTitle(String title) {
        mTitle.setText(title);
        return this;
    }

    public ToolbarView hideNext() {
        mIconNext.setVisibility(GONE);
        return this;
    }

    public ToolbarView showNext() {
        mIconNext.setVisibility(VISIBLE);
        return this;
    }

    public interface OnClickBackListener {
        void onClickBack();
    }

    public interface OnClickNextListener {
        void onClickNext();
    }

    public interface OnClickTitleListener {
        void onClickTitle();
    }

}
