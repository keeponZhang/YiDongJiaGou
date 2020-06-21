package com.example.otherproesss;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.baby.binderframwork.MyApp;

public class OtherProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void load(View view) {
        Intent intent = new Intent();
        intent.setAction("com.example.baby.binderframwork.MyService");
        intent.setPackage("com.example.baby.binderframwork");
        //进程B 目的   binderServise  ----->  IBinder iBinder

        //ActivityManagerNative
        // c.conn.connected(r.name, service);
        // private static final Singleton<IActivityManager> gDefault = new Singleton<IActivityManager>() {
        //     protected IActivityManager create() {
        //         IBinder b = ServiceManager.getService("activity");
                    //这里已经把存根类写入了系统源码，所以直接调用asInterface方法
        //         IActivityManager am = asInterface(b);
        //         return am;
        //     }
        // };
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    //proxy   装煤炭  Proxy 对象  MyApp接口
                //这里的iBinder参数是系统返回来的，上面则是通过ServiceManager.getService获取的
                //都需要通过存根对下那个的asInterface来确定是否是同进程
                //同进程返回的其实是onBind方法返回的那个对象，不然就是iBinder就是BinderProxy，然后创建一个存根的代理对象
                //通过代理对象通话
                //bindService是一个跨进程通信，返回iBinder
                //下面方法调用也是一个跨进程通信
               MyApp myApp= MyApp.Stub.asInterface(iBinder);
                Log.e("TAG", "OtherProcessActivity onServiceConnected iBinder:" +iBinder);
                try {
                    // myApp.setName("David");
                    Toast.makeText(OtherProcessActivity.this, "--->  " + myApp.getName(), Toast.LENGTH_SHORT).show();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        }, Context.BIND_AUTO_CREATE);
    }
}
