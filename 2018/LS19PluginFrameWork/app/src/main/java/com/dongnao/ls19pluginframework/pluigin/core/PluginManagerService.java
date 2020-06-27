package com.dongnao.ls19pluginframework.pluigin.core;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dongnao.ls19pluginframework.pluigin.pm.IPluiginManager;
import com.dongnao.ls19pluginframework.pluigin.pm.PackageManagerService;

/**
 * Created by Administrator on 2018/4/13.
 */

public class PluginManagerService extends Service {
    private  static  PackageManagerService packageManagerService;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return getPackageManagerService(this);
    }
    public   PackageManagerService getPackageManagerService(Context context) {
        if (packageManagerService == null) {
            synchronized (PluginManagerService.class) {
                if (packageManagerService == null) {
                    //这里是重写的pms
                    packageManagerService = new PackageManagerService(this);
                    packageManagerService.main();
                }
            }
        }
        return packageManagerService;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
