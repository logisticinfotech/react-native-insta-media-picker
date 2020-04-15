package com.instagrammediapicker;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.instagrammediapicker.mediapickerinstagram.commons.adapters.ViewPagerAdapter;
import com.instagrammediapicker.mediapickerinstagram.commons.bus.RxBusNext;
import com.instagrammediapicker.mediapickerinstagram.commons.models.Session;
import com.instagrammediapicker.mediapickerinstagram.commons.models.enums.SourceType;
import com.instagrammediapicker.mediapickerinstagram.commons.modules.NavigatorEditorModule;
import com.instagrammediapicker.mediapickerinstagram.commons.modules.PermissionModule;
import com.instagrammediapicker.mediapickerinstagram.commons.ui.ToolbarView;
import com.instagrammediapicker.mediapickerinstagram.components.gallery.GalleryPickerFragment;
import com.instagrammediapicker.mediapickerinstagram.components.photo.CapturePhotoFragment;
import com.instagrammediapicker.mediapickerinstagram.components.video.CaptureVideoFragment;


import java.util.ArrayList;
import java.util.HashSet;


public class Main2Activity extends ReactActivity implements ToolbarView.OnClickTitleListener,
        ToolbarView.OnClickNextListener, ToolbarView.OnClickBackListener {

    private static final String TAG = "Main2Activity";
    private Context mContext = null;
    private static int selectedTab = 1;
    private TabSelected tabSelected;
    //    @BindView(R.id.mMainTabLayout)
    TabLayout mMainTabLayout;
    //    @BindView(R.id.mMainViewPager)
    ViewPager mMainViewPager;
    //    @BindView(R.id.mToolbar)
    ToolbarView mToolbar;

    //    @BindString(R.string.tab_gallery)
    String _tabGallery = String.valueOf(R.string.tab_gallery);
    //    @BindString(R.string.tab_photo)
    String _tabPhoto = String.valueOf(R.string.tab_photo);
    //    @BindString(R.string.tab_video)
    String _tabVideo = String.valueOf(R.string.tab_video);
    private Session mSession = Session.getInstance();
    private HashSet<SourceType> mSourceTypeSet = new HashSet<>();
    private RxBusNext mRxBusNext = RxBusNext.getInstance();
    public CaptureVideoFragment captureVideoFragment = new CaptureVideoFragment();
    public CapturePhotoFragment capturePhotoFragment = new CapturePhotoFragment();
//    public Main2Activity(Context context) {
//        this.mContext = context;
//        mMainTabLayout = ( TabLayout ) findViewById(R.id.mMainTabLayout);
//        mMainViewPager = ( ViewPager ) findViewById(R.id.mMainViewPager);
//        mToolbar = ( ToolbarView ) findViewById(R.id.mToolbar);
//
//        mSourceTypeSet.add(SourceType.Gallery);
//        mSourceTypeSet.add(SourceType.Photo);
//        mSourceTypeSet.add(SourceType.Video);
//
//        initViews();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mMainTabLayout = (TabLayout) findViewById(R.id.mMainTabLayout);
        mMainViewPager = (ViewPager) findViewById(R.id.mMainViewPager);
        mToolbar = (ToolbarView) findViewById(R.id.mToolbar);
        mSourceTypeSet.add(SourceType.Gallery);
        mSourceTypeSet.add(SourceType.Photo);
        mSourceTypeSet.add(SourceType.Video);
        initViews();
    }


    private void initViews() {

        PermissionModule permissionModule = new PermissionModule(this);
        permissionModule.checkPermissions();

        mToolbar.setOnClickBackMenuListener(this)
                .setOnClickTitleListener(this)
                .setOnClickNextListener(this);
        final ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getListFragment());
        mMainViewPager.setAdapter(pagerAdapter);
        mMainTabLayout.addOnTabSelectedListener(getViewPagerOnTabSelectedListener());
        mMainViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMainTabLayout));
        mMainViewPager.setOffscreenPageLimit(2);
        mMainViewPager.setCurrentItem(0);
        mMainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                selectedTab = i;
                if (i == 1) {
                    captureVideoFragment.tabSelected(i);
                    capturePhotoFragment.tabSelected(i);
                } else if (i == 2) {
                    capturePhotoFragment.tabSelected(i);
                    captureVideoFragment.tabSelected(i);
                } else {
                    captureVideoFragment.tabSelected(i);
                    capturePhotoFragment.tabSelected(1);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });
    }


    private TabLayout.ViewPagerOnTabSelectedListener getViewPagerOnTabSelectedListener() {
        return new TabLayout.ViewPagerOnTabSelectedListener(mMainViewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                super.onTabSelected(tab);
                displayTitleByTab(tab);
                initNextButtonByTab(tab.getPosition());
            }
        };
    }

    private void displayTitleByTab(TabLayout.Tab tab) {
        if (tab.getText() != null) {
            String title = tab.getText().toString();
            mToolbar.setTitle(title);
        }
    }

    private void initNextButtonByTab(int position) {
        switch (position) {
            case 0:
                mToolbar.showNext();
                break;
            case 1:
                mToolbar.hideNext();
                break;
            case 2:
                mToolbar.hideNext();
                break;
            default:
                mToolbar.hideNext();
                break;
        }
    }


    private ArrayList<Fragment> getListFragment() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        if (mSourceTypeSet.contains(SourceType.Gallery)) {
            fragments.add(GalleryPickerFragment.newInstance());
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(R.string.tab_gallery));
        }

        if (mSourceTypeSet.contains(SourceType.Photo)) {
            fragments.add(capturePhotoFragment);
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(R.string.tab_photo));
        }

        if (mSourceTypeSet.contains(SourceType.Video)) {
            fragments.add(captureVideoFragment);
            mMainTabLayout.addTab(mMainTabLayout.newTab().setText(R.string.tab_video));
        }

        return fragments;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main_view);
//        mMainTabLayout = ( TabLayout ) findViewById(R.id.mMainTabLayout);
//        mMainViewPager = ( ViewPager ) findViewById(R.id.mMainViewPager);
//        mToolbar = ( ToolbarView ) findViewById(R.id.mToolbar);
//
//        mSourceTypeSet.add(SourceType.Gallery);
//        mSourceTypeSet.add(SourceType.Photo);
//        mSourceTypeSet.add(SourceType.Video);
//        initViews();
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

    @Override
    public void onClickBack() {
        Log.e(TAG, "onClickBack: " );
        WritableMap event = Arguments.createMap();
        getReactInstanceManager().getCurrentReactContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onBackPress",event);
    }

    @Override
    public void onClickNext() {
        mRxBusNext.send(true);
        new NavigatorEditorModule(Main2Activity.this).goTo(0, false, false);
    }

    @Override
    public void onClickTitle() {

    }

    public interface TabSelected {
        public void tabSelected(int selectedTab);
    }

}
