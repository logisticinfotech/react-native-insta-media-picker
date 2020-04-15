package com.instagrammediapicker;

import android.content.Context;
import android.content.Intent;
import android.widget.RelativeLayout;

import androidx.core.app.ActivityCompat;

class MainActivity extends RelativeLayout {
    private Context mContext;

    public MainActivity(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    private void init() {
        Intent i1 = new Intent(getContext(), Main2Activity.class);
        i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.getContext().startActivity(i1);
    }
}
