package com.example.baby.hookframework;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.util.Log;

import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

/**
 * Created by baby on 2018/4/2.
 */

public class MyApplication extends Application {
    private static Context sContext;
    private static final String TAG = "david";
    private AssetManager assetManager;
    private Resources newResource;
    private Resources.Theme mTheme;

    @Override
    public void onCreate() {
        super.onCreate();
        HookUtil hookUtil = new HookUtil();
        hookUtil.hookStartActivity(this);
        hookUtil.hookHookMh(this);
        hookUtil.injectPluginClass();
//        重构
        String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/plugin-debug.apk";


        try {
            assetManager = AssetManager.class.newInstance();
            Method addAssetPathMethod = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class);
            addAssetPathMethod.setAccessible(true);
            addAssetPathMethod.invoke(assetManager, apkPath);
//        手动实例化(这个也很重要)
            Method ensureStringBlocks = AssetManager.class.getDeclaredMethod("ensureStringBlocks");
            ensureStringBlocks.setAccessible(true);
            ensureStringBlocks.invoke(assetManager);
//            插件的StringBloac被实例化了
            Resources supResource = getResources();
            //这一行注释掉会崩溃
            newResource = new Resources(assetManager, supResource.getDisplayMetrics(), supResource.getConfiguration());
            Log.e("TAG", "MyApplication onCreate:" );
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "MyApplication onCreate Exception:"+e );
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
