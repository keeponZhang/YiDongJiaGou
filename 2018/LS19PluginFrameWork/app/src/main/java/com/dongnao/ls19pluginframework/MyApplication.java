package com.dongnao.ls19pluginframework;

import android.app.Application;
import android.content.Context;

import com.dongnao.ls19pluginframework.pluigin.HookFactory;

/**
 * Created by baby on 2018/4/16.
 */

public class MyApplication extends Application{

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //插件  又会调用一次
        HookFactory.getInstance().installHook(this,this.getClassLoader());
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
