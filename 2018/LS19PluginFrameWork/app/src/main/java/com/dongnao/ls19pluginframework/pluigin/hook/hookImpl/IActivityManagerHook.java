package com.dongnao.ls19pluginframework.pluigin.hook.hookImpl;

import android.view.View;

import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseClassHandle;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseProxyHook;
import com.dongnao.ls19pluginframework.pluigin.hook.handleImpl.IActivityManagerClassHandle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by baby on 2018/4/16.
 */

public class IActivityManagerHook  extends BaseProxyHook {
    @Override
    protected BaseClassHandle createHookHandle() {
        return new IActivityManagerClassHandle();
    }

    @Override
    public void onInit(ClassLoader classLoader) {
            Class<?> ActivityManagerNativecls = null;
            try {
                ActivityManagerNativecls = Class.forName("android.app.ActivityManagerNative");
                Field gDefault = ActivityManagerNativecls.getDeclaredField("gDefault");
                gDefault.setAccessible(true);
//            因为是静态变量  所以获取的到的是系统值  hook   伪hook
                Object defaltValue = gDefault.get(null);
                //mInstance对象
                Class<?> SingletonClass = Class.forName("android.util.Singleton");

                Field mInstance = SingletonClass.getDeclaredField("mInstance");
//        还原 IactivityManager对象  系统对象
                mInstance.setAccessible(true);
                Object iActivityManagerObject = mInstance.get(defaltValue);
                setRealObj(iActivityManagerObject);
//
                Class<?> IActivityManagerIntercept = Class.forName("android.app.IActivityManager");
//            第二参数  是即将返回的对象 需要实现那些接口
                Object oldIactivityManager = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                        , new Class[]{IActivityManagerIntercept}
                        , this);
//            将系统的iActivityManager  替换成    自己通过动态代理实现的对象   oldIactivityManager对象  实现了 IActivityManager这个接口的所有方法
                mInstance.set(defaltValue, oldIactivityManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }


}
