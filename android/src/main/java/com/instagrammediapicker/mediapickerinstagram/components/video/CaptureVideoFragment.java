package com.instagrammediapicker.mediapickerinstagram.components.video;

import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.instagrammediapicker.Main2Activity;
import com.instagrammediapicker.R;
import com.instagrammediapicker.mediapickerinstagram.commons.models.Session;
import com.instagrammediapicker.mediapickerinstagram.commons.modules.NavigatorEditorModule;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CaptureVideoFragment extends Fragment implements SurfaceHolder.Callback, Main2Activity.TabSelected {

    private static final String TAG = CaptureVideoFragment.class.getSimpleName();
//    @BindView(R.id.mBtnTakeVideo)
    ImageView mCameraVideoViewButton,switchCameraImage;
//    @BindView(R.id.surface_view)
    SurfaceView mSurfaceView;
//    @BindView(R.id.txtDuration)
    TextView durationText;
    SurfaceHolder mHolder;
    CamcorderProfile camcorderProfile;
    Camera mCamera;


    private Handler customHandler = new Handler();
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private long startTime = 0L;
    private MediaRecorder mMediaRecorder;
    boolean mInitSuccesful = true;
    int switchCamera = 0;

    private Session mSession = Session.getInstance();
    File videoFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.capture_video_view, container, false);
//        ButterKnife.bind(this, v);
        mCameraVideoViewButton = (ImageView) v.findViewById(R.id.mBtnTakeVideo);
        mSurfaceView = (SurfaceView) v.findViewById(R.id.surface_view);
        durationText = (TextView) v.findViewById(R.id.txtDuration);
        switchCameraImage = (ImageView) v.findViewById(R.id.mSwitchCamera);
        initViews();

        mCameraVideoViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecordingVideo();
            }
        });

        switchCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCamera == null) {
                    return;
                }
                mCamera.stopPreview();
                mCamera.release();
                if (switchCamera == 0) {
                    switchCamera = 1;
                } else {
                    switchCamera = 0;
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
//        startCamera();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart: ======> " );
    }

    public static CaptureVideoFragment newInstance() {
        CaptureVideoFragment fragment = new CaptureVideoFragment();
        return fragment;
    }

    private void initViews() {
        camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_LOW);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
    }

    public void startRecordingVideo() {
        if (mInitSuccesful) {
            Log.e(TAG, "startRecordingVideo: start video");
            if(mMediaRecorder == null){
                try {
                    initRecorder();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            mMediaRecorder.start();
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
            mCameraVideoViewButton.setImageResource(R.drawable.btn_photo);
            mInitSuccesful = false;
        } else {
            Log.e(TAG, "startRecordingVideo: stop video");
            mMediaRecorder.stop();
            Log.e(TAG, "startRecordingVideo: ====> " +videoFile);
            mSession.setFileToUpload(videoFile);
            customHandler.removeCallbacks(updateTimerThread);
            durationText.setText("00:00");
            mCameraVideoViewButton.setImageResource(R.drawable.btn_capture_video);
            mCamera.lock();
            mInitSuccesful = true;

            new NavigatorEditorModule(getContext()).goTo(0,false, true);
        }
    }
    private void startCamera() {
        Log.e(TAG, "=====> startCamera video <=====");
        try {
            mCamera = Camera.open(switchCamera);
            mCamera.setDisplayOrientation(90);
        } catch (RuntimeException e) {
            Log.d(TAG, "start_camera: ");
            return;
        }
        Camera.Parameters param;
        param = mCamera.getParameters();
        param.setRotation(90);
        mCamera.setParameters(param);
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "start_camera: ", e);
            return;
        }
        try {
            if (!mInitSuccesful)
                initRecorder();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void stopCamera() {
        Log.e(TAG, "=====> stopCamera video <=====");
        if (mCamera == null)
            return;
        mCamera.stopPreview();
        mCamera.release();
//        shutdown();
    }
    private void initRecorder() throws IOException {
        if (mCamera == null) {
            mCamera = Camera.open();
            mCamera.unlock();
        }
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
        mMediaRecorder.setOrientationHint( switchCamera == 1 ? 270 : 90);
        File outputDir = new File(String.valueOf(Environment.getExternalStorageDirectory() + "/androidModule/video"));
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.THREE_GPP) {
            try {
                videoFile = File.createTempFile("videocaptureIf", ".3gp", outputDir);
                mMediaRecorder.setOutputFile(videoFile.getAbsolutePath());
            } catch (IOException e) {
                Log.v(TAG, "Couldn't create file");
                e.printStackTrace();
            }
        } else if (camcorderProfile.fileFormat == MediaRecorder.OutputFormat.MPEG_4) {
            try {
                videoFile = File.createTempFile("videocapture_else_if", ".mp4", outputDir);
                mMediaRecorder.setOutputFile(videoFile.getAbsolutePath());
            } catch (IOException e) {
                Log.v(TAG, "Couldn't create file");
                e.printStackTrace();
            }
        } else {
            try {
                videoFile = File.createTempFile("videocapture_else", ".mp4", outputDir);
                mMediaRecorder.setOutputFile(videoFile.getAbsolutePath());
            } catch (IOException e) {
                Log.v(TAG, "Couldn't create file");
                e.printStackTrace();
            }

        }
        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initRecorder: " + e.getMessage() );
        }
        mInitSuccesful = true;
    }


    private void shutdown() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
        }
        if (mCamera == null)
            return;
        mCamera.stopPreview();
        mCamera.release();
        switchCamera = 0;
        mMediaRecorder = null;
        mCamera = null;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated() called with: holder = [" + holder + "]");
//        startCamera();
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

    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updatedTime / 1000);
            int mins = secs / 60;
            secs = secs % 60;
            durationText.setText(mins + ":" + secs);
            customHandler.postDelayed(this, 0);
        }

    };


    @Override
    public void tabSelected(int selectedTab) {
        Log.e(TAG, "tabSelected video : " + selectedTab);
        if(selectedTab == 2){
            startCamera();
        } else {
            shutdown();
        }
    }
}