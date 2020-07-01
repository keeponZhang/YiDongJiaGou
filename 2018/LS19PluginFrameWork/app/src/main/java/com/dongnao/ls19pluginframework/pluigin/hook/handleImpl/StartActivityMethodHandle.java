package com.dongnao.ls19pluginframework.pluigin.hook.handleImpl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.RemoteException;
import android.util.Log;

import com.dongnao.ls19pluginframework.pluigin.activity.ProxyActivity;
import com.dongnao.ls19pluginframework.pluigin.core.PluginManager;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseMethodHandle;

import java.lang.reflect.Method;

public class StartActivityMethodHandle extends BaseMethodHandle {
    public StartActivityMethodHandle(Context mHostContext) {
        super(mHostContext);
    }
    private static int findFirstIntentIndexInArgs(Object[] args) {
        if (args != null && args.length > 0) {
            int i = 0;
            for (Object arg : args) {
                if (arg != null && arg instanceof Intent) {
                    return i;
                }
                i++;
            }
        }
        return -1;
    }

    //invoke方法----》beforeInvoke---->afterInvoker
    @Override
    protected boolean beforeInvoke(Object receiver, Method method, Object[] args) {
        Log.e("TAG", "StartActivityMethodHandle beforeInvoke:");
        Intent intent=null;
        int index= findFirstIntentIndexInArgs(args);
        if (args != null && args.length > 1 && index >= 0) {
            intent = (Intent) args[index];
//        包名   ----     ActivityInfo  MainAcitivty
        }
//瞒天过海
//                寻找传进来的intent
//目的  ---载入acgtivity  将它还原  ProxyActivity 永远
        Intent newIntent = new Intent();
//            分配进程
        ComponentName componentName=selectProxyActivity(intent);
//                ComponentName componentName = new ComponentName(mHostContext,ActivityMode.P02.Standard00.class);
//            ActivityMode.P02.Standard00)  process   给你新开进程

        newIntent.setComponent(componentName);
//                真实的意图 被我隐藏到了  键值对
        newIntent.putExtra("oldIntent", intent);
        args[index] = newIntent;
        return false;
    }
    //intent    com.example.MainActivity    -------->    ComponentName (   ActivityMode.P02.Standard00)
    private ComponentName selectProxyActivity(Intent intent) {
        if (intent!= null) {
            ActivityInfo proxyInfo = PluginManager.getInstance().selectProxyActivity(intent);
            if (proxyInfo != null) {
                return new ComponentName(proxyInfo.packageName, proxyInfo.name);
            }
        }
        return null;

    }

    @Override
    protected void afterInvoke(Object receiver, Method method, Object[] args) {
        super.afterInvoke(receiver, method, args);
    }

    public static ActivityInfo resolveActivity(Intent intent) {
        try {
            return  PluginManager.getInstance().resolveActivityInfo( intent,0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }
}