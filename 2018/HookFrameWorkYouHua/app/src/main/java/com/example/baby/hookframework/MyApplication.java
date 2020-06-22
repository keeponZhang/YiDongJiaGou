package com.example.baby.hookframework;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by baby on 2018/4/2.
 */

public class MyApplication extends Application {
    public static boolean isPlugin=false;
    private static Context  instance;
    private static final String TAG = "david";
    private AssetManager assetManager;
    private Resources newResource;
    private Resources.Theme mTheme;

    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        HookUtil hookUtil = new HookUtil();
        hookUtil.hookStartActivity(this);
        hookUtil.hookHookMh(this);
        String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk";
        hookUtil.putLoadedApk(apkPath);
//        hookUtil.injectPluginClass();  抛弃了的
//        重构



        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(assetManager, apkPath);
//        手动实例化
            Method ensureStringBlocks = AssetManager.class.getDeclaredMethod("ensureStringBlocks");
            ensureStringBlocks.setAccessible(true);
            ensureStringBlocks.invoke(assetManager);
//            插件的StringBloac被实例化了
            Resources supResource = getResources();
            newResource = new Resources(assetManager, supResource.getDisplayMetrics(), supResource.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public AssetManager getAssetManager() {
        return assetManager==null?super.getAssets():assetManager;
    }

    @Override
    public Resources getResources() {
        return newResource==null?super.getResources():newResource;
    }
    //    resource
}
