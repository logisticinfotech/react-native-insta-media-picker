package com.instagrammediapicker.mediapickerinstagram.components.photo;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.instagrammediapicker.Main2Activity;
import com.instagrammediapicker.R;
import com.instagrammediapicker.mediapickerinstagram.commons.models.Session;
import com.instagrammediapicker.mediapickerinstagram.commons.modules.NavigatorEditorModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

public class CapturePhotoFragment extends Fragment implements SurfaceHolder.Callback, Main2Activity.TabSelected {

    private static final String TAG = CapturePhotoFragment.class.getSimpleName();
//    @BindView(R.id.mBtnTakePhoto)
    ImageView mCameraSwitch;
//    @BindView(R.id.mSwitchCamera)
    ImageView mCameraFlash;
//    @BindView(R.id.mFlashPhoto)
    ImageView mTakePhoto;
//    @BindView(R.id.surface_view)
    SurfaceView mSurfaceView;
    SurfaceHolder mHolder;
    CamcorderProfile camcorderProfile;
    Camera mCamera;
    int switchCamera = 0;
    int flashCamera = 0;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;
    private Session mSession = Session.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.capture_photo_view, container, false);
        mTakePhoto = (ImageView) v.findViewById(R.id.mBtnTakePhoto);
        mCameraSwitch = (ImageView) v.findViewById(R.id.mSwitchCamera);
        mCameraFlash = (ImageView) v.findViewById(R.id.mFlashPhoto);
        mSurfaceView = (SurfaceView) v.findViewById(R.id.surface_view);
        initViews();
        mCameraSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
                mCamera.release();
                if (switchCamera == 0) {
                    switchCamera = 1;
                } else {
                    switchCamera = 0;
                }
                startCamera();
            }
        });
        mTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        mCameraFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                }
                mCamera.release();
                if (flashCamera == 0) {
                    flashCamera = 1;
                } else {
                    flashCamera = 0;
                }
                startCamera();
            }
        });
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        startCamera();
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    public static CapturePhotoFragment newInstance() {
        CapturePhotoFragment fragment = new CapturePhotoFragment();
        return fragment;
    }
    private void initViews() {
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        rawCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };
        /** Handles data for jpeg picture */
        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outStream = null;
                File outputDir = new File(Environment.getExternalStorageDirectory() + "/androidModule/photo");
                if (!outputDir.exists()) {
                    outputDir.mkdirs();
                    Log.e("output", "dir create " + outputDir);
                }
                try {
                    String file_name = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + ".jpg";
                    File filePath = new File(outputDir, file_name);
                    outStream = new FileOutputStream(filePath);
                    outStream.write(data);
                    outStream.close();
                    mSession.setFileToUpload(filePath);
                    Log.e(TAG, "onPictureTaken: file path => " + filePath);
                    new NavigatorEditorModule(getContext()).goTo(switchCamera, true, false);
                    Log.d("Log", "onPictureTaken - wrote bytes: " + data.length);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onPictureTaken: " + e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onPictureTaken: " + e.getMessage());
                } finally {
                }
            }
        };
    }

//    @OnClick(R.id.mBtnTakePhoto)
    void onTakePhotoClick() {
        takePhoto();
    }

//    @OnClick(R.id.mSwitchCamera)
    void onSwitchCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        mCamera.release();
        if (switchCamera == 0) {
            switchCamera = 1;
        } else {
            switchCamera = 0;
        }
        startCamera();
    }

//    @OnClick(R.id.mFlashPhoto)
    void onChangeFlashState() {
        Toast.makeText(getContext(), "flash", Toast.LENGTH_SHORT).show();
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        mCamera.release();
        if (flashCamera == 0) {
            flashCamera = 1;
        } else {
            flashCamera = 0;
        }
        startCamera();
    }


    public void takePhoto() {
        mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
    }

    public static int setCameraDisplayOrientation(AppCompatActivity activity,
                                                  int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    private void startCamera() {
        Log.e(TAG, "=====> startCamera photo <=====");
        try {
            mCamera = Camera.open(switchCamera);
            mCamera.setDisplayOrientation(90);
//            setCameraDisplayOrientation(getActivity(),switchCamera,mCamera);
        } catch (RuntimeException e) {
            Log.d(TAG, "start_camera: ");
            return;
        }
        Camera.Parameters param;
        param = mCamera.getParameters();
        param.setRotation(switchCamera == 1 ? 270 : 90);
        param.setFlashMode(flashCamera == 1 ? Camera.Parameters.FLASH_MODE_ON : Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(param);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "start_camera: ", e);
            return;
        }
    }

    private void shutdown() {
        if (mCamera == null)
            return;
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
        switchCamera = 0;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated() called with: holder = [" + holder + "]");
        startCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged() called with: holder = [" + holder + "], format = [" + format + "], width = [" + width + "], height = [" + height + "]");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed() called with: holder = [" + holder + "]");
        shutdown();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, " =====> onDestroyView: video fragment <===== ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, " =====> onDestroy: video fragment <===== ");
    }

    @Override
    public void tabSelected(int selectedTab) {
        Log.e(TAG, "tabSelected photo: " + selectedTab);
        if (selectedTab == 1) {
            startCamera();
        } else {
            shutdown();
        }
    }
}