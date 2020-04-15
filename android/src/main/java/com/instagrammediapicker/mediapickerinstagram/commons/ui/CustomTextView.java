package com.instagrammediapicker.mediapickerinstagram.commons.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.instagrammediapicker.R;


public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);

        final int N = a.getIndexCount();
        for (int i = 0; i < N; ++i) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.CustomTextView_customFont) {
                String customFontName = a.getString(attr);
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/" + customFontName + ".ttf");
                setTypeface(tf);
            }
        }
        a.recycle();
    }

}