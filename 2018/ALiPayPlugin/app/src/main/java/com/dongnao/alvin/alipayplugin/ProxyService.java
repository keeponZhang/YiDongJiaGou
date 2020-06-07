package com.dongnao.alvin.alipayplugin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dongnao.alvin.pluginstand.PayInterfaceService;

import java.lang.reflect.Constructor;

/**
 * Created by Administrator on 2018/3/28.
 */

public class ProxyService extends  Service{
    String serviceName;
    PayInterfaceService payInterfaceService;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        init(intent);
        return null;
    }

    private void init(Intent intent) {
        serviceName = intent.getStringExtra("serviceName");
//        class
        try {
            Class loadClass= PluginManager.getInstance().getDexClassLoader().loadClass(serviceName);

            Constructor<?> localConstructor =loadClass.getConstructor(new Class[] {});
            Object instance = localConstructor.newInstance(new Object[] {});
//            OneService
            payInterfaceService = (PayInterfaceService) instance;
            payInterfaceService.attach(this);
            Bundle bundle = new Bundle();
            bundle.putInt("form", 1);
            payInterfaceService.onCreate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (payInterfaceService == null) {
            init(intent);
        }

        return payInterfaceService.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        payInterfaceService.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        payInterfaceService.onLowMemory();
        super.onLowMemory();
    }


    @Override
    public boolean onUnbind(Intent intent) {
        payInterfaceService.onUnbind(intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        payInterfaceService.onRebind(intent);
        super.onRebind(intent);
    }

}
