package com.dongnao.ls19pluginframework.pluigin.hook.handleImpl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.RemoteException;

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

        int intentOfArgIndex = findFirstIntentIndexInArgs(args);
        if (args != null && args.length > 1 && intentOfArgIndex >= 0) {
            Intent intent = (Intent) args[intentOfArgIndex];
            ActivityInfo activityInfo = resolveActivity(intent);
//        包名   ----     ActivityInfo  MainAcitivty
        }
//瞒天过海
//                寻找传进来的intent
        Intent intent = null;
        int index = 0;

//目的  ---载入activity  将它还原
        Intent newIntent = new Intent();
        ComponentName componentName = new ComponentName(mHostContext, ProxyActivity.class);
        newIntent.setComponent(componentName);
//                真实的意图 被我隐藏到了  键值对
        newIntent.putExtra("oldIntent", intent);
        args[index] = newIntent;

        return false;
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