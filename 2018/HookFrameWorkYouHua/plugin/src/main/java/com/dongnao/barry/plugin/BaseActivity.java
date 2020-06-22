package com.dongnao.barry.plugin;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

/**
 * Created by Administrator on 2018/4/9.
 */

public class BaseActivity extends Activity{

    @Override
    public Resources getResources() {
        Log.d("TAG", "BaseActivity getResources:" );
        if (getApplication() != null && getApplication().getResources() != null) {
            Log.d("TAG", "BaseActivity getResources:" +getApplication().getResources());
            return getApplication().getResources();
        }
        return super.getResources();
    }

    @Override
    public AssetManager getAssets() {
        if (getApplication() != null && getApplication().getAssets() != null) {
            return getApplication().getAssets();
        }
        return super.getAssets();
    }
//    @Override
//    public Resources.Theme getTheme() {
//        if(getApplication() != null && getApplication().getTheme() != null){
//            return getApplication().getTheme();
//        }
//        return super.getTheme();
//    }
}
