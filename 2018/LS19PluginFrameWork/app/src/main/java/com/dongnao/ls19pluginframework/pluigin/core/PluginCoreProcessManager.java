package com.dongnao.ls19pluginframework.pluigin.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ComponentInfo;
import android.util.Log;

import com.dongnao.ls19pluginframework.pluigin.utils.reflect.PluginDirHelper;
import com.dongnao.ls19pluginframework.pluigin.utils.reflect.Utils;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by Administrator on 2018/4/18.
 */

public class PluginCoreProcessManager {
    private static Map<String, Object> sPluginLoadedApkCache = new WeakHashMap<String, Object>();

    public static void preLoadApk(Context hostContext, ComponentName component){

        try {

            Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
//            先还原activityThread对象
            Method currentActivityThreadMethod = activityThreadClass.getDeclaredMethod("currentActivityThread");
            currentActivityThreadMethod.setAccessible(true);
            Object currentActivityThread = currentActivityThreadMethod.invoke(null);

//    再来还原  mPackages  对象
            // 获取到 mPackages 这个静态成员变量, 这里缓存了apk包的信息
            Field mPackagesField = activityThreadClass.getDeclaredField("mPackages");
            mPackagesField.setAccessible(true);
            Map mPackages = (Map) mPackagesField.get(currentActivityThread);

//z找到  getPackageInfoNoCheck   method 方法
            Class<?> compatibilityInfoClass = Class.forName("android.content.res.CompatibilityInfo");
            Method getPackageInfoNoCheckMethod = activityThreadClass.getDeclaredMethod(
                    "getPackageInfoNoCheck", ApplicationInfo.class, compatibilityInfoClass);

//        得到 CompatibilityInfo  里面的  静态成员变量       DEFAULT_COMPATIBILITY_INFO  类型  CompatibilityInfo
            Field defaultCompatibilityInfoField = compatibilityInfoClass.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
            Object defaultCompatibilityInfo = defaultCompatibilityInfoField.get(null);
//        application  apk文件  PMS
            ApplicationInfo applicationInfo=PluginManager.getInstance().getApplicationInfo(component, 0);
//            一个问题  传参 ApplicationInfo ai 一定是与插件相关    ApplicationInfo----》插件apk文件
//LoadedApk getPackageInfoNoCheck(ApplicationInfo ai, CompatibilityInfo compatInfo)
            Object loadedApk=getPackageInfoNoCheckMethod.invoke(currentActivityThread, applicationInfo, defaultCompatibilityInfo);



            String optimizedDirectory = PluginDirHelper.getPluginDalvikCacheDir(hostContext, component.getPackageName());
            String libraryPath = PluginDirHelper.getPluginNativeLibraryDir(hostContext, component.getPackageName());
//Apk文件     class没有加载 apk
            ClassLoader classLoader = new CustomClassLoader(applicationInfo.publicSourceDir,optimizedDirectory,libraryPath,hostContext.getClassLoader());
            Field mClassLoaderField = loadedApk.getClass().getDeclaredField("mClassLoader");
            mClassLoaderField.setAccessible(true);
            mClassLoaderField.set(loadedApk,classLoader);
//            弱引用
            WeakReference weakReference = new WeakReference(loadedApk);
//     最终目的  是要替换ClassLoader  不是替换LoaderApk
            Log.e("TAG", "PluginCoreProcessManager preLoadApk:"+component.getPackageName());
            mPackages.put(component.getPackageName(),weakReference);
            sPluginLoadedApkCache.put(component.getPackageName(), loadedApk);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
