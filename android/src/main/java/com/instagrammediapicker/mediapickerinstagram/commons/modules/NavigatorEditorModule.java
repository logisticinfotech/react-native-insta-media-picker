package com.instagrammediapicker.mediapickerinstagram.commons.modules;

import android.content.Context;
import android.content.Intent;

import com.instagrammediapicker.mediapickerinstagram.components.editor.EditorActivity;

public class NavigatorEditorModule {

	private Context mContext;

	public NavigatorEditorModule(Context context) {
		this.mContext = context;
	}

	public void goTo(int camera_mode, boolean camera_Screen, boolean video_screen) {
		Intent intent = new Intent(mContext, EditorActivity.class);
		intent.putExtra("camera_mode", camera_mode);
		intent.putExtra("camera_screen", camera_Screen);
		intent.putExtra("video_screen", video_screen);
		mContext.startActivity(intent);
	}
}
