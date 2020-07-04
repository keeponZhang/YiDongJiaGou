package com.dongnao.ls19pluginframework.pluigin.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.dongnao.ls19pluginframework.pluigin.pm.IPluiginManager;

/**
 * Created by Administrator on 2018/4/13.
 */

public class PluginManager implements ServiceConnection {
    //这个是PackageManagerService的代理对象
    private IPluiginManager mPluginManager;

    private Context mHostContext;
    private static final PluginManager ourInstance = new PluginManager();

    public static PluginManager getInstance() {
        return ourInstance;
    }

    private PluginManager() {
    }
    public void init(Context context) {
        mHostContext = context;
    }
    public void connectToService() {
        if (mPluginManager == null) {
            try {
                Intent intent = new Intent(mHostContext, PluginManagerService.class);
                mHostContext.startService(intent);
                mHostContext.bindService(intent, this, Context.BIND_AUTO_CREATE);
            } catch (Exception e) {
            }
        }
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mPluginManager = IPluiginManager.Stub.asInterface(service);
        Toast.makeText(mHostContext,"启动服务成功",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    public int installPackage(String filepath, int flags) {
        try {
            if (mPluginManager != null) {
                int result = mPluginManager.installPackage(filepath, flags);
                Toast.makeText(mHostContext,"安装插件成功",Toast.LENGTH_LONG).show();
                return result;
            }
        }  catch (Exception e) {

        }
        return -1;
    }
    public ActivityInfo resolveActivityInfo(Intent intent, int flags) throws RemoteException {
        if (mPluginManager != null) {
            return mPluginManager.getActivityInfo(intent.getComponent(), flags);
        }
        return null;
    }
    public ApplicationInfo getApplicationInfo(ComponentName componentName, int flag) {
        Log.e("TAG",
                "PluginManager getApplicationInfo (mPluginManager=null):" +(mPluginManager==null));
        if (mPluginManager != null) {
            try {
                return mPluginManager.getApplicationInfo(componentName.getPackageName(), flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }
    public ActivityInfo selectProxyActivity(Intent intent) {
        if (mPluginManager!=null) {
            try {
                return  mPluginManager.selectStubActivityInfoByIntent(intent);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
