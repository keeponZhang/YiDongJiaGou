package com.dongnao.ls19pluginframework.pluigin.parser;

import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.text.TextUtils;
import android.util.Log;

import com.dongnao.ls19pluginframework.pluigin.utils.reflect.ComponentNameComparator;
import com.dongnao.ls19pluginframework.pluigin.utils.reflect.PluginDirHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Administrator on 2018/4/13.
 */

public class PluginPackageMap {
    //    ComponentName   组件类名   -------- Activity  前生 内存-----》存档  ActivityInfo
//    Object--->PackageParser.Activity  大标题  javabean
    private Map<ComponentName, Object> mActivityObjCache =
            new TreeMap<ComponentName, Object>(new ComponentNameComparator());
    private Map<ComponentName, Object> mServiceObjCache =
            new TreeMap<ComponentName, Object>(new ComponentNameComparator());
    private Map<ComponentName, Object> mProviderObjCache =
            new TreeMap<ComponentName, Object>(new ComponentNameComparator());
    private Map<ComponentName, Object> mReceiversObjCache =
            new TreeMap<ComponentName, Object>(new ComponentNameComparator());
    //缓存插件  四大组件的存档
    private Map<ComponentName, ActivityInfo> mActivityInfoCache =
            new TreeMap<ComponentName, ActivityInfo>(new ComponentNameComparator());
    private Map<ComponentName, ServiceInfo> mServiceInfoCache =
            new TreeMap<ComponentName, ServiceInfo>(new ComponentNameComparator());
    private Map<ComponentName, ProviderInfo> mProviderInfoCache =
            new TreeMap<ComponentName, ProviderInfo>(new ComponentNameComparator());
    private Map<ComponentName, ActivityInfo> mReceiversInfoCache =
            new TreeMap<ComponentName, ActivityInfo>(new ComponentNameComparator());

    //缓存   acitvity  组件 对应的隐式启动的IntentFilter
    private Map<ComponentName, List<IntentFilter>> mActivityIntentFilterCache =
            new TreeMap<ComponentName, List<IntentFilter>>(new ComponentNameComparator());
    private Map<ComponentName, List<IntentFilter>> mServiceIntentFilterCache =
            new TreeMap<ComponentName, List<IntentFilter>>(new ComponentNameComparator());
    private Map<ComponentName, List<IntentFilter>> mProviderIntentFilterCache =
            new TreeMap<ComponentName, List<IntentFilter>>(new ComponentNameComparator());
    private Map<ComponentName, List<IntentFilter>> mReceiverIntentFilterCache =
            new TreeMap<ComponentName, List<IntentFilter>>(new ComponentNameComparator());

    private File mPluginFile;
    private PackageParser mParser;
    private String mPackageName;
    private Context mHostContext;
    //宿主的包名信息
    private PackageInfo mHostPackageInfo;

    public String getmPackageName() {
        return mPackageName;
    }

    public ActivityInfo getActivityInfo(ComponentName className, int flags) throws Exception {
        ActivityInfo activityInfo = null;
        activityInfo = mActivityInfoCache.get(className);
        fixApplicationInfo(activityInfo.applicationInfo);
        if (TextUtils.isEmpty(activityInfo.processName)) {
            activityInfo.processName = activityInfo.packageName;
        }
        return activityInfo;
    }

    public PluginPackageMap(Context hostContext, File pluginFile) throws Exception {
        mHostContext = hostContext;
        mPluginFile = pluginFile;
        mParser = PackageParserManager.getInstance().getPluginParser(hostContext);
        mParser.parsePackage(pluginFile, 0);
//        插件的包名
        mPackageName = mParser.getPackageName();
        mHostPackageInfo =
                mHostContext.getPackageManager().getPackageInfo(mHostContext.getPackageName(), 0);
//        Activity  缩略信息
        List datas = mParser.getActivities();
        for (Object activity : datas) {
//            插件的包名  ----插件activity的类名
//            键
            ComponentName componentName = new ComponentName(mPackageName
                    , mParser.readNameFromComponent(activity));

//值
            mActivityObjCache.put(componentName, activity);
            //这个是有缺陷的，packageName是插件的
            ActivityInfo value = mParser.generateActivityInfo(activity, 0);
            fixApplicationInfo(value.applicationInfo);
            if (TextUtils.isEmpty(value.processName)) {
                value.processName = value.packageName;
            }
            mActivityInfoCache.put(componentName, value);
            List<IntentFilter> filters = mParser.readIntentFilterFromComponent(activity);
            mActivityIntentFilterCache.remove(componentName);
            mActivityIntentFilterCache.put(componentName, new ArrayList<IntentFilter>(filters));


        }

        datas = mParser.getServices();
        for (Object data : datas) {
            ComponentName componentName =
                    new ComponentName(mPackageName, mParser.readNameFromComponent(data));
            mServiceObjCache.put(componentName, data);
            ServiceInfo value = mParser.generateServiceInfo(data, 0);
            fixApplicationInfo(value.applicationInfo);
            if (TextUtils.isEmpty(value.processName)) {
                value.processName = value.packageName;
            }
            mServiceInfoCache.put(componentName, value);
            List<IntentFilter> filters = mParser.readIntentFilterFromComponent(data);
            mServiceIntentFilterCache.remove(componentName);
            mServiceIntentFilterCache.put(componentName, new ArrayList<IntentFilter>(filters));
        }

        datas = mParser.getProviders();
        for (Object data : datas) {
            ComponentName componentName =
                    new ComponentName(mPackageName, mParser.readNameFromComponent(data));
            mProviderObjCache.put(componentName, data);
            ProviderInfo value = mParser.generateProviderInfo(data, 0);
            fixApplicationInfo(value.applicationInfo);
            if (TextUtils.isEmpty(value.processName)) {
                value.processName = value.packageName;
            }
            mProviderInfoCache.put(componentName, value);

            List<IntentFilter> filters = mParser.readIntentFilterFromComponent(data);
            mProviderIntentFilterCache.remove(componentName);
            mProviderIntentFilterCache.put(componentName, new ArrayList<IntentFilter>(filters));
        }

        datas = mParser.getReceivers();
        for (Object data : datas) {
            ComponentName componentName =
                    new ComponentName(mPackageName, mParser.readNameFromComponent(data));
            mReceiversObjCache.put(componentName, data);

            ActivityInfo value = mParser.generateActivityInfo(data, 0);
            fixApplicationInfo(value.applicationInfo);
            if (TextUtils.isEmpty(value.processName)) {
                value.processName = value.packageName;
            }
            mReceiversInfoCache.put(componentName, value);


            List<IntentFilter> filters = mParser.readIntentFilterFromComponent(data);
            mReceiverIntentFilterCache.remove(componentName);
            mReceiverIntentFilterCache.put(componentName, new ArrayList<IntentFilter>(filters));
        }

    }

    private ApplicationInfo fixApplicationInfo(ApplicationInfo applicationInfo) {

        if (applicationInfo.sourceDir == null) {
            applicationInfo.sourceDir = mPluginFile.getPath();
        }
        if (applicationInfo.publicSourceDir == null) {
            applicationInfo.publicSourceDir = mPluginFile.getPath();
        }
        if (applicationInfo.dataDir == null) {
            applicationInfo.dataDir =
                    PluginDirHelper.getPluginDataDir(mHostContext, applicationInfo.packageName);
        }
        //这个一定要是系统的
        applicationInfo.uid = mHostPackageInfo.applicationInfo.uid;
        if (applicationInfo.nativeLibraryDir == null) {
            applicationInfo.nativeLibraryDir = PluginDirHelper
                    .getPluginNativeLibraryDir(mHostContext, applicationInfo.packageName);
        }
        if (TextUtils.isEmpty(applicationInfo.processName)) {
            applicationInfo.processName = applicationInfo.packageName;
        }
        return applicationInfo;
    }

    public ApplicationInfo getApplicationInfo(int flags) throws Exception {
        ApplicationInfo applicationInfo = mParser.generateApplicationInfo(flags);
        Log.e("TAG", "PluginPackageMap getApplicationInfo applicationInfo:" + applicationInfo);
        fixApplicationInfo(applicationInfo);
        if (TextUtils.isEmpty(applicationInfo.processName)) {
            applicationInfo.processName = applicationInfo.packageName;
        }
        return applicationInfo;
    }


}
