package com.instagrammediapicker.mediapickerinstagram;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.instagrammediapicker.R;
import com.instagrammediapicker.mediapickerinstagram.commons.ui.ToolbarView;

import butterknife.BindString;
import butterknife.BindView;

public class EditVideo extends AppCompatActivity implements  ToolbarView.OnClickTitleListener,
        ToolbarView.OnClickNextListener, ToolbarView.OnClickBackListener
{

//    @BindView(R.id.mEditorToolbarVideo)
    ToolbarView mEditorToolbar;

//    @BindString(R.string.toolbar_title_editor)
    String _toolbarTitleEditor = String.valueOf(R.string.toolbar_title_editor);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        mEditorToolbar.setOnClickBackMenuListener(this)
                .setOnClickTitleListener(this)
                .setOnClickNextListener(this)
                .setTitle(_toolbarTitleEditor)
                .showNext();

    }

    @Override
    public void onClickBack() {

    }

    @Override
    public void onClickNext() {

    }

    @Override
    public void onClickTitle() {

    }
}
