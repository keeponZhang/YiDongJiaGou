package com.dongnao.ls19pluginframework.pluigin;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dongnao.ls19pluginframework.pluigin.core.PluginManager;
import com.dongnao.ls19pluginframework.pluigin.hook.base.BaseHook;
import com.dongnao.ls19pluginframework.pluigin.hook.hookImpl.IActivityManagerHook;
import com.dongnao.ls19pluginframework.pluigin.hook.hookImpl.IActivityThreadHandlerHook;
import com.dongnao.ls19pluginframework.pluigin.hook.hookImpl.IPackageManagerHook;

import java.io.File;
import java.lang.reflect.Method;

/**
 * Created by baby on 2018/4/16.
 */

public class HookFactory {
    private static final HookFactory ourInstance = new HookFactory();

    public static HookFactory getInstance() {
        return ourInstance;
    }
    private HookFactory() {
    }


    //这里用BaseHook当方法参数，实际上调用的是具体类的onInit方法
    private void installHook(BaseHook baseHook, ClassLoader classLoader) {
        try {
            baseHook.onInit(classLoader);
        } catch (Exception e) {

        }

    }
//    宿主   application  调用
    public final void installHook(Context context, ClassLoader classLoader) {
//    hook类非常多  宿主   打电话 服务 不需要  hook  判断当前进程
//         boolean isSuZhu = isSuZhu(context);
//         // pluginService = false;
//         if(isSuZhu){
// //            宿主进程
//             installHook(new IActivityManagerHook(context), classLoader);
//             installHook(new IPackageManagerHook(context), classLoader);
//             installHook(new IActivityThreadHandlerHook(context), classLoader);
//             Log.e("TAG",
//                     "HookFactory installHook(安装hook点)--------------:"+ getProcessName(context));
//         }else if(isPluginService(context)) {
// //            插件进程
//             PluginManager.getInstance().init(context);
//             PluginManager.getInstance().connectToService();
//             // File file = new File(Environment.getExternalStorageDirectory(), "plugin-debug.apk");
//             // PluginManager.getInstance().installPackage(file.getAbsolutePath(), 0);
//             Log.e("TAG", "HookFactory installHook 插件进程绑定服务:" );
//         }else{
//             Log.e("TAG", "HookFactory installHook 插件进程(此时没有hook):"+getProcessName(context));
//         }
        //全部hook是不行的
        installHook(new IActivityManagerHook(context), classLoader);
        installHook(new IPackageManagerHook(context), classLoader);
        installHook(new IActivityThreadHandlerHook(context), classLoader);
    }

    public boolean isPluginService(Context context) {
        String processName = getProcessName(context);
        if (processName != null) {
            return processName.contains(":Plugin");
        }
        return false;
    }
    public boolean isSuZhu(Context context) {
        String processName = getProcessName(context);
        if (processName != null) {
            return processName.equals("com.dongnao.ls19pluginframework");
        }
        return false;
    }
    public static String getProcessName(Context context) {

        String processName = null;
        Class<?> ActivityThread = null;
        try {
            ActivityThread = Class.forName("android.app.ActivityThread");
            Method currentActivityThread = ActivityThread.getDeclaredMethod("currentActivityThread");
            currentActivityThread.setAccessible(true);
            Object am = currentActivityThread.invoke(null);
            Method getProcessName = ActivityThread.getDeclaredMethod("getProcessName");
            getProcessName.setAccessible(true);
            processName = (String) getProcessName.invoke(am);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return processName;
    }



}
