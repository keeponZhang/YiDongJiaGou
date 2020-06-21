package com.example.baby.binderframwork;

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

public class SameActivity extends AppCompatActivity {

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

        // c.conn.connected(r.name, service);
        // private static final Singleton<IActivityManager> gDefault = new Singleton<IActivityManager>() {
        //     protected IActivityManager create() {
        //         IBinder b = ServiceManager.getService("activity");
        //         IActivityManager am = asInterface(b);
        //         return am;
        //     }
        // };
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                //proxy   装煤炭  Proxy 对象  MyApp接口
                //这里的iBinder参数
                Log.e("TAG", "SameActivity onServiceConnected iBinder:" +iBinder);
                MyApp myApp= MyApp.Stub.asInterface(iBinder);

                try {
                    // myApp.setName("David");
                    Toast.makeText(SameActivity.this, "--->  " + myApp.getName(), Toast.LENGTH_SHORT).show();
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
