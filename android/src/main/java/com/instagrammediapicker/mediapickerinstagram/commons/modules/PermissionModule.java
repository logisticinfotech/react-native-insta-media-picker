package com.instagrammediapicker.mediapickerinstagram.commons.modules;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;

public class PermissionModule {

    private final Context mContext;

    public PermissionModule(Context context) {
        mContext = context;
    }

    public void checkPermissions() {
        ArrayList<String> permissionsNeeded = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }
//        if (!permissionsNeeded.isEmpty()) {
//            requestPermission(permissionsNeeded.toArray(new String[permissionsNeeded.size()]));
//        }
    }

    private void requestPermission(String[] permissions) {
        ActivityCompat.requestPermissions((AppCompatActivity)mContext, permissions, 125);
    }
}
