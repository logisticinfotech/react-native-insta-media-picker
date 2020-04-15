package com.instagrammediapicker.mediapickerinstagram.components.editor;

import android.content.Context;
import android.service.autofill.CustomDescription;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.instagrammediapicker.R;
import com.instagrammediapicker.mediapickerinstagram.commons.models.Thumbnail;
import com.instagrammediapicker.mediapickerinstagram.commons.modules.ReboundModule;
import com.instagrammediapicker.mediapickerinstagram.commons.modules.ReboundModuleDelegate;
import com.instagrammediapicker.mediapickerinstagram.commons.ui.CustomTextView;
import com.zomato.photofilters.imageprocessors.Filter;

import java.lang.ref.WeakReference;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;

public class EffectItemView extends LinearLayout implements ReboundModuleDelegate {

    ImageView mEffectTypeView;
    CustomTextView mEffectName;
    int _darkChocolate;
    int _lightCream;

    private ReboundModule mReboundModule = ReboundModule.getInstance(this);
    private WeakReference<EffectItemViewListener> mWrListener;
    private Filter mFilter;

    void setListener(EffectItemViewListener listener) {
        this.mWrListener = new WeakReference<>(listener);
    }

    public EffectItemView(Context context) {
        super(context);
        View v = View.inflate(context, R.layout.effet_item_view, this);
        mEffectTypeView = (ImageView) v.findViewById(R.id.mEffectTypeView);
        mEffectName = (CustomTextView) v.findViewById(R.id.mEffectName);

        _darkChocolate = R.color.dark_chocolate;
        _lightCream = R.color.light_cream;

        ButterKnife.bind(this, v);
    }

    public void bind(Thumbnail thumbnail) {
        mReboundModule.init(mEffectTypeView);
        mEffectName.setText(thumbnail.name);

        // TODO change text color if isSelected

        mEffectTypeView.setImageBitmap(thumbnail.image);
        mFilter = thumbnail.filter;
    }

    @Override
    public void onTouchActionUp() {
        mWrListener.get().onClickEffectType(mFilter);
    }

}
