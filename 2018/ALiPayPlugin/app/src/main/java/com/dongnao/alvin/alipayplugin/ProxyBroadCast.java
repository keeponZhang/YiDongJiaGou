package com.dongnao.alvin.alipayplugin;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dongnao.alvin.pluginstand.PayInterfaceBroadcast;

import java.lang.reflect.Constructor;

/**
 * Created by baby on 2018/3/30.
 */

public class ProxyBroadCast extends BroadcastReceiver {
    private String className;
    //插件里面的广播
    PayInterfaceBroadcast payInterfaceBroadcast;
    public ProxyBroadCast(String className,Context context) {
        //这里是代理receiver的className
        this.className = className;
        try {
            Class loadClass = PluginManager.getInstance().getDexClassLoader().loadClass(className);
            Constructor<?> localConstructor =loadClass.getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});
            payInterfaceBroadcast = (PayInterfaceBroadcast) instance;
            payInterfaceBroadcast.attach(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//class------>  object--->payintenfaceBroadCast
    @Override
    public void onReceive(Context context, Intent intent) {
        payInterfaceBroadcast.onReceive(context, intent);
    }
}
