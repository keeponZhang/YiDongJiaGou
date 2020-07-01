package com.dongnao.ls19pluginframework.pluigin.hook.handleImpl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.dongnao.ls19pluginframework.MyApplication;
import com.dongnao.ls19pluginframework.pluigin.core.PluginCoreProcessManager;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2018/4/18.
 */

public class ActivityMH  implements Handler.Callback{


    private  Handler mH;

    public ActivityMH(Context context,Handler mH) {
        this.context = context;
        this.mH = mH;
    }

    private Context context;

    public ActivityMH(Handler mH) {
        this.mH = mH;
    }

    @Override
    public boolean handleMessage(Message msg) {
//LAUNCH_ACTIVITY ==100 即将要加载一个activity了
        if (msg.what == 100) {
//加工 --完  一定丢给系统  secondActivity  -hook->proxyActivity---hook->    secondeActivtiy   2
            Log.e("TAG", "ActivityMH handleMessage:");
            handleLuachActivity(msg);
        }
//做了真正的跳转
        Log.e("TAG", "ActivityMH handleMessage mH:"+mH);
        mH.handleMessage(msg);
        return  true;
    }

    private void handleLuachActivity(Message msg) {
//            还原   ActivityClientRecord obj
        Object obj = msg.obj;
        try {
            Field intentField=obj.getClass().getDeclaredField("intent");
            intentField.setAccessible(true);
            //  ProxyActivity   2
            Intent realyIntent = (Intent) intentField.get(obj);
//                sconedActivity  1
                 Intent oldIntent = realyIntent.getParcelableExtra("oldIntent");
            if (oldIntent != null) {
//即将加载插件Activity  ---Class 加载 进  1       2
                realyIntent.setComponent(oldIntent.getComponent());
                Field activityInfoField= obj.getClass().getDeclaredField("activityInfo");
                activityInfoField.setAccessible(true);
                ActivityInfo activityInfo= (ActivityInfo) activityInfoField.get(obj);
//              插件的class  packageName--->loadeApk   系统   第一次 IPackageManager ----》activitry  -——》包名   ---》
//                    不够 IPackageManage.getPackageInfo()
                activityInfo.applicationInfo.packageName = oldIntent.getPackage() == null ? oldIntent.getComponent().getPackageName()
                        : oldIntent.getPackage();
                Log.w("TAG", "ActivityMH handleLuachActivity:"+oldIntent.getComponent());
                PluginCoreProcessManager.preLoadApk(context, oldIntent.getComponent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
