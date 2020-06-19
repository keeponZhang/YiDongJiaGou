package com.example.baby.binderframwork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by baby on 2018/4/4.
 */

/**
 * 服务提供者
 * 18妹妹
 *
 *
 * 老鸨
 *
 * 发传单
 *
 * 我所在酒店 房间
 *
 */
public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }


    /**
     * 提供给客户端
     * @param intent
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyAppIml();
    }
}
