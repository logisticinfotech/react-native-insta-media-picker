package com.instagrammediapicker.mediapickerinstagram.components.editor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.instagrammediapicker.R;
import com.instagrammediapicker.mediapickerinstagram.commons.managers.ThumbnailManager;
import com.instagrammediapicker.mediapickerinstagram.commons.models.Session;
import com.instagrammediapicker.mediapickerinstagram.commons.models.Thumbnail;
import com.instagrammediapicker.mediapickerinstagram.commons.ui.ToolbarView;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;

public class EditorActivity extends ReactActivity implements ToolbarView.OnClickTitleListener,
        ToolbarView.OnClickNextListener, ToolbarView.OnClickBackListener, EffectAdapterListener {

    private static final String TAG = "EditorActivity";

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    // @BindView(R.id.mEditorToolbar)
    ToolbarView mEditorToolbar;
    // @BindView(R.id.mEffectPreview)
    ImageView mEffectPreview;
    // @BindView(R.id.mEffectChooserRecyclerView)
    RecyclerView mEffectChooserRecyclerView;
    // @BindView(R.id.videoView)
    VideoView videoView;

    // @BindString(R.string.toolbar_title_editor)
    // String _toolbarTitleEditor = String.valueOf(R.string.toolbar_title_editor);

    private Session mSession = Session.getInstance();
    private Filter mCurrentFilter = null;
    private Bitmap selectedEffect;

    public int switch_camera = 0;
    public boolean camera_screen = false;
    public boolean video_screen = false;

    private void initViewVideo() {
        MediaController mediaController = new MediaController(this);
        videoView.setVisibility(View.VISIBLE);
        Log.e(TAG, "path mSession===> " + mSession.getFileToUpload().getAbsolutePath());
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.setVideoURI(Uri.parse(mSession.getFileToUpload().getAbsolutePath()));
        videoView.start();
    }

    private void initViews() {
        // mEditorToolbar.setOnClickBackMenuListener(this)
        // .setOnClickTitleListener(this)
        // .setOnClickNextListener(this)
        // .setTitle(R.string.toolbar_title_editor)
        // .showNext();

        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mEffectPreview.getLayoutParams();
        lp.height = getResources().getDisplayMetrics().widthPixels;
        mEffectPreview.setLayoutParams(lp);

        mEffectChooserRecyclerView.setHasFixedSize(true);
        mEffectChooserRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mEffectChooserRecyclerView.setLayoutManager(mLayoutManager);
        final EffectAdapter effectAdapter = new EffectAdapter(this);
        effectAdapter.setListener(this);
        mEffectChooserRecyclerView.setAdapter(effectAdapter);

        // Picasso.with(this).load(Uri.fromFile(mSession.getFileToUpload()))
        // .noFade()
        // .noPlaceholder()
        // .into(mEffectPreview);

        mEffectPreview.setImageBitmap(getBitmapFromFile());
        mEffectPreview.setOnTouchListener(setOnTouchListener());

        effectAdapter.setItems(getFilters());
    }

    private List<Thumbnail> getFilters() {
        Bitmap bitmap = getBitmapFromFile();

        Thumbnail t1 = new Thumbnail();
        Thumbnail t2 = new Thumbnail();
        Thumbnail t3 = new Thumbnail();
        Thumbnail t4 = new Thumbnail();
        Thumbnail t5 = new Thumbnail();
        Thumbnail t6 = new Thumbnail();
        Thumbnail t7 = new Thumbnail();

        t1.image = bitmap;
        t2.image = bitmap;
        t3.image = bitmap;
        t4.image = bitmap;
        t5.image = bitmap;
        t6.image = bitmap;
        t7.image = bitmap;

        ThumbnailManager.clearThumbs();
        t1.name = "None";
        ThumbnailManager.addThumb(t1);

        t2.name = "StarLit";
        t2.filter = SampleFilters.getStarLitFilter();
        ThumbnailManager.addThumb(t2);

        t3.name = "BlueMess";
        t3.filter = SampleFilters.getBlueMessFilter();
        ThumbnailManager.addThumb(t3);

        t4.name = "AweStruckVibe";
        t4.filter = SampleFilters.getAweStruckVibeFilter();
        ThumbnailManager.addThumb(t4);

        t5.name = "Lime";
        t5.filter = SampleFilters.getLimeStutterFilter();
        ThumbnailManager.addThumb(t5);

        t6.name = "B&W";
        t6.filter = new Filter();
        t6.filter.addSubFilter(new SaturationSubFilter(-100f));
        ThumbnailManager.addThumb(t6);

        t7.name = "Sepia";
        t7.filter = new Filter();
        t7.filter.addSubFilter(new SaturationSubFilter(-100f));
        t7.filter.addSubFilter(new ColorOverlaySubFilter(1, 102, 51, 0));
        ThumbnailManager.addThumb(t7);

        return ThumbnailManager.processThumbs(this);
    }

    private Bitmap getBitmapFromFile() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inMutable = true;
        if (!camera_screen) {
            return BitmapFactory.decodeFile(mSession.getFileToUpload().getAbsolutePath(), options);
        } else {
            // return
            // rotateImage(BitmapFactory.decodeFile(mSession.getFileToUpload().getAbsolutePath(),
            // options), switch_camera == 1 ? 270 : 90);
            return rotateImage(BitmapFactory.decodeFile(mSession.getFileToUpload().getAbsolutePath(), options), 90);
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        if (switch_camera == 1)
            matrix.preScale(-1.0f, 1.0f);
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private View.OnTouchListener setOnTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int action = motionEvent.getAction();
                if (mCurrentFilter != null) {
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            mEffectPreview.setImageBitmap(getBitmapFromFile());
                            break;
                        case MotionEvent.ACTION_UP:
                            mEffectPreview.setImageBitmap(mCurrentFilter.processFilter(getBitmapFromFile()));
                            break;
                        default:
                            break;
                    }
                }
                return true;
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editor_view);

        mEditorToolbar = (ToolbarView) findViewById(R.id.mEditorToolbar);
        mEffectPreview = (ImageView) findViewById(R.id.mEffectPreview);
        mEffectChooserRecyclerView = (RecyclerView) findViewById(R.id.mEffectChooserRecyclerView);
        videoView = (VideoView) findViewById(R.id.videoView);

        ButterKnife.bind(this);
        overridePendingTransition(R.anim.slide_in_right, R.anim.zoom_out);
        switch_camera = getIntent().getExtras().getInt("camera_mode");
        camera_screen = getIntent().getExtras().getBoolean("camera_screen");
        video_screen = getIntent().getExtras().getBoolean("video_screen");
        mEditorToolbar.setOnClickBackMenuListener(this).setOnClickTitleListener(this).setOnClickNextListener(this)
                .setTitle(String.valueOf(R.string.toolbar_title_editor)).showNext();
        if (video_screen) {
            initViewVideo();
        } else {
            initViews();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.zoom_in, R.anim.slide_out_right);
    }

    @Override
    public void onClickBack() {
        this.onBackPressed();
    }

    @Override
    public void onClickNext() {
        Log.e(TAG, "onClickNext: ===> " + mSession.getFileToUpload().getAbsolutePath());
        if (selectedEffect != null) {
            File file = SaveImage(selectedEffect);
            if(file != null){
				WritableMap event = Arguments.createMap();
				event.putString("filePath", String.valueOf(file));
				getReactInstanceManager().getCurrentReactContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onNextPress", event);
			}
        } else {
            WritableMap event = Arguments.createMap();
            event.putString("filePath", mSession.getFileToUpload().getAbsolutePath());
            getReactInstanceManager().getCurrentReactContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onNextPress", event);
        }
    }

    @Override
    public void onClickTitle() {
    }

    @Override
    public void applyEffectType(Filter filter) {
        if (filter != mCurrentFilter) {
            mCurrentFilter = filter;
            mEffectPreview.setImageBitmap(filter.processFilter(getBitmapFromFile()));
            selectedEffect = filter.processFilter(getBitmapFromFile());
        }
    }

    private static File SaveImage(Bitmap finalBitmap) {
		Log.e(TAG, "onSaveEffect: ===> ");
		File outputDir = new File(String.valueOf(Environment.getExternalStorageDirectory() + "/androidModule/photo"));
		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}
		String file_name = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + ".jpg";
        File file = new File(outputDir, file_name);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("save image error => ", e.getMessage());
        }
        return null;
    }
}
