package com.example.otherproesss;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.baby.binderframwork.MyApp;

public class MainActivity extends AppCompatActivity {

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
        bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    //proxy   装煤炭  Proxy 对象  MyApp接口
               MyApp myApp= MyApp.Stub.asInterface(iBinder);

                try {
                    // myApp.setName("David");
                    Toast.makeText(MainActivity.this, "--->  " + myApp.getName(), Toast.LENGTH_SHORT).show();
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
