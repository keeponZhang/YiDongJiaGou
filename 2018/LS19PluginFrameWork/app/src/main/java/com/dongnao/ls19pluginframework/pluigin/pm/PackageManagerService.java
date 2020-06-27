package com.dongnao.ls19pluginframework.pluigin.pm;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionGroupInfo;
import android.content.pm.PermissionInfo;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.os.RemoteException;

import com.dongnao.ls19pluginframework.pluigin.parser.PackageParser;
import com.dongnao.ls19pluginframework.pluigin.parser.PackageParserManager;
import com.dongnao.ls19pluginframework.pluigin.parser.PluginPackageMap;
import com.dongnao.ls19pluginframework.pluigin.utils.reflect.FileUtils;
import com.dongnao.ls19pluginframework.pluigin.utils.reflect.PluginDirHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/13.
 */

public class PackageManagerService extends  IPluiginManager.Stub {
    private Context mContext;

    public PackageManagerService(Context mContext) {
        this.mContext = mContext;
    }

    public void main() {
//        PMS 服务  安装了app  将acitvity 所对应的ActivityInfo  ----》、缓存表 ---》
//        app----》app  intent
//        系统开机      宿主也要做 插件  acitvity 对应存档信息  ActivityInfo
        new  Thread(){
            @Override
            public void run() {
                onCreateInnner(mContext);
                super.run();
            }
        }.start();


    }

    private void onCreateInnner(Context mContext) {
//        模拟手机开机的时候加载的app    /data/app/包名/base.1.apk

        ArrayList<File> apkfiles = null;
        try {
            apkfiles = new ArrayList<File>();
            File baseDir = new File(PluginDirHelper.getBaseDir(mContext));
            //插件的apk文件 存放在  /data/data/宿主包名/Plugin/插件包名/apk/base-1.apk
            //   PluginDirHelper.getBaseDir()返回  = /data/data/宿主包名/Plugin/
            File[] dirs = baseDir.listFiles();
            for (File dir : dirs) {
                if (dir.isDirectory()) {
                    File file = new File(dir, "apk/base.apk");
                    if (file.exists()) {
                        apkfiles.add(file);
                    }
                }
            }
        } catch (Exception e) {
        }
        if (apkfiles != null && apkfiles.size() > 0) {
            for (File pluginFile : apkfiles) {
//考虑一个问题  当前手机 版本
                try {
//                    PackageParser packageParser = PackageParserManager.getInstance().getPluginParser(mContext);
                    PluginPackageMap pluginPackageMap = new PluginPackageMap(mContext, pluginFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }






    @Override
    public boolean waitForReady() throws RemoteException {
        return false;
    }

    @Override
    public PackageInfo getPackageInfo(String packageName, int flags) throws RemoteException {
        return null;
    }

    @Override
    public boolean isPluginPackage(String packageName) throws RemoteException {
        return false;
    }

    @Override
    public ActivityInfo getActivityInfo(ComponentName className, int flags) throws RemoteException {
        return null;
    }

    @Override
    public ActivityInfo getReceiverInfo(ComponentName className, int flags) throws RemoteException {
        return null;
    }

    @Override
    public ServiceInfo getServiceInfo(ComponentName className, int flags) throws RemoteException {
        return null;
    }

    @Override
    public ProviderInfo getProviderInfo(ComponentName className, int flags) throws RemoteException {
        return null;
    }

    @Override
    public ResolveInfo resolveIntent(Intent intent, String resolvedType, int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<ResolveInfo> queryIntentActivities(Intent intent, String resolvedType, int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<ResolveInfo> queryIntentReceivers(Intent intent, String resolvedType, int flags) throws RemoteException {
        return null;
    }

    @Override
    public ResolveInfo resolveService(Intent intent, String resolvedType, int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<ResolveInfo> queryIntentServices(Intent intent, String resolvedType, int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<ResolveInfo> queryIntentContentProviders(Intent intent, String resolvedType, int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<PackageInfo> getInstalledPackages(int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<ApplicationInfo> getInstalledApplications(int flags) throws RemoteException {
        return null;
    }

    @Override
    public PermissionInfo getPermissionInfo(String name, int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<PermissionInfo> queryPermissionsByGroup(String group, int flags) throws RemoteException {
        return null;
    }

    @Override
    public PermissionGroupInfo getPermissionGroupInfo(String name, int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<PermissionGroupInfo> getAllPermissionGroups(int flags) throws RemoteException {
        return null;
    }

    @Override
    public ProviderInfo resolveContentProvider(String name, int flags) throws RemoteException {
        return null;
    }

    @Override
    public ApplicationInfo getApplicationInfo(String packageName, int flags) throws RemoteException {
        return null;
    }

    @Override
    public int installPackage(String  pluginFile, int flags) throws RemoteException {
        String apkfile = null;
        PackageManager pm = mContext.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(pluginFile, 0);
        //插件的apk文件 存放在  /data/data/宿主包名/Plugin/插件包名/apk/base-1.apk
        apkfile = PluginDirHelper.getPluginApkFile(mContext, info.packageName);
        if(new File(apkfile).exists()){
            new File(apkfile).delete();
        }
        FileUtils.copyFile(pluginFile, apkfile);
//        -----------------安装------------------
        try {
//            ----------------解析----------
            PluginPackageMap pluginPackageMap = new PluginPackageMap(mContext,new File(apkfile));;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public int deletePackage(String packageName, int flags) throws RemoteException {
        return 0;
    }

    @Override
    public List<ActivityInfo> getReceivers(String packageName, int flags) throws RemoteException {
        return null;
    }

    @Override
    public List<IntentFilter> getReceiverIntentFilter(ActivityInfo info) throws RemoteException {
        return null;
    }

    @Override
    public int checkSignatures(String pkg1, String pkg2) throws RemoteException {
        return 0;
    }

    @Override
    public ActivityInfo selectStubActivityInfo(ActivityInfo targetInfo) throws RemoteException {
        return null;
    }

    @Override
    public ActivityInfo selectStubActivityInfoByIntent(Intent targetIntent) throws RemoteException {
        return null;
    }

    @Override
    public ServiceInfo selectStubServiceInfo(ServiceInfo targetInfo) throws RemoteException {
        return null;
    }

    @Override
    public ServiceInfo selectStubServiceInfoByIntent(Intent targetIntent) throws RemoteException {
        return null;
    }

    @Override
    public ServiceInfo getTargetServiceInfo(ServiceInfo stubInfo) throws RemoteException {
        return null;
    }

    @Override
    public ProviderInfo selectStubProviderInfo(String name) throws RemoteException {
        return null;
    }

    @Override
    public List<String> getPackageNameByPid(int pid) throws RemoteException {
        return null;
    }

    @Override
    public String getProcessNameByPid(int pid) throws RemoteException {
        return null;
    }

    @Override
    public boolean killBackgroundProcesses(String packageName) throws RemoteException {
        return false;
    }

    @Override
    public boolean killApplicationProcess(String pluginPackageName) throws RemoteException {
        return false;
    }

    @Override
    public boolean forceStopPackage(String pluginPackageName) throws RemoteException {
        return false;
    }

    @Override
    public void onActivityCreated(ActivityInfo stubInfo, ActivityInfo targetInfo) throws RemoteException {

    }

    @Override
    public void onActivityDestory(ActivityInfo stubInfo, ActivityInfo targetInfo) throws RemoteException {

    }

    @Override
    public void onServiceCreated(ServiceInfo stubInfo, ServiceInfo targetInfo) throws RemoteException {

    }

    @Override
    public void onServiceDestory(ServiceInfo stubInfo, ServiceInfo targetInfo) throws RemoteException {

    }

    @Override
    public void onProviderCreated(ProviderInfo stubInfo, ProviderInfo targetInfo) throws RemoteException {

    }

    @Override
    public void reportMyProcessName(String stubProcessName, String targetProcessName, String targetPkg) throws RemoteException {

    }

    @Override
    public void onActivtyOnNewIntent(ActivityInfo stubInfo, ActivityInfo targetInfo, Intent intent) throws RemoteException {

    }

    @Override
    public int getMyPid() throws RemoteException {
        return 0;
    }
}
