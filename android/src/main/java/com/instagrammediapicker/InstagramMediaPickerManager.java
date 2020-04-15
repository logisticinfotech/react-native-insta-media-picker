package com.instagrammediapicker;

import android.content.Context;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InstagramMediaPickerManager extends SimpleViewManager<MainActivity> implements ReactPackage {
	private static final String REACT_CLASS = "InstaPicker";
	Context mcontext;
	ThemedReactContext themedReactContext;
	MainActivity mainActivity;

	@Override
	public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
		return Collections.emptyList();
	}

	// @Override
	public List<Class<? extends JavaScriptModule>> createJSModules() {
		return null;
	}

	@Override
	public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
		List<ViewManager> modules = new ArrayList<>();
		// Add native UI components here
		modules.add(new InstagramMediaPickerManager());
		return modules;
	}

	@Override
	public String getName() {
		return REACT_CLASS;
	}

	@Override
	protected MainActivity createViewInstance(ThemedReactContext reactContext) {
		mcontext = reactContext;
		themedReactContext = reactContext;
		mainActivity = new MainActivity(reactContext);
		return mainActivity;
	}
}
