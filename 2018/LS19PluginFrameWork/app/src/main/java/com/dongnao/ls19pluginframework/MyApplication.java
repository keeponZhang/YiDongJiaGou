package com.dongnao.ls19pluginframework;

import android.app.Application;

import com.dongnao.ls19pluginframework.pluigin.HookFactory;

/**
 * Created by baby on 2018/4/16.
 */

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //插件  又会调用一次
        HookFactory.getInstance().installHook(this,this.getClassLoader());
    }
}
