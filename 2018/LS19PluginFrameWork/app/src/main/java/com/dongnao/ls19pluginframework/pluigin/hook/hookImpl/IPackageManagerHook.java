package com.dongnao.ls19pluginframework.pluigin.hook.hookImpl;

import android.content.Context;
import android.util.Log;

import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseClassHandle;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseProxyHook;
import com.dongnao.ls19pluginframework.pluigin.hook.handleImpl.IPackageManagerClassHandle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2018/4/18.
 */

public class IPackageManagerHook  extends BaseProxyHook{
    public IPackageManagerHook(Context context) {
        super(context);
    }

    @Override
    protected BaseClassHandle createHookHandle() {
        return new IPackageManagerClassHandle();
    }

    @Override
    public void onInstall(ClassLoader classLoader) {

        // 这一步是因为 initializeJavaContextClassLoader 这个方法内部无意中检查了这个包是否在系统安装
        // 如果没有安装, 直接抛出异常, 这里需要临时Hook掉 PMS, 绕过这个检查.

        Class<?> activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

            // 获取ActivityThread里面原始的 sPackageManager
            Field sPackageManagerField = activityThreadClass.getDeclaredField("sPackageManager");
            sPackageManagerField.setAccessible(true);
            Object sPackageManager = sPackageManagerField.get(currentActivityThread);
            setRealObj(sPackageManager);
            Log.i("david", " handleMessage之前发生啦   ");
            // 准备好代理对象, 用来替换原始的对象
            Class<?> iPackageManagerInterface = Class.forName("android.content.pm.IPackageManager");
            Object proxy = Proxy.newProxyInstance(iPackageManagerInterface.getClassLoader()
                    , new Class[]{iPackageManagerInterface},this );
            sPackageManagerField.set(currentActivityThread,proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
