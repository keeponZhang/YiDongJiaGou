package com.dongnao.alvin.taopiaopiao;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.dongnao.alvin.pluginstand.PayInterfaceActivity;

/**
 * Created by Administrator on 2018/3/28.
 */

public class BaseActivity  extends Activity implements PayInterfaceActivity {


    protected  Activity that;
    @Override
    public void attach(Activity proxyActivity) {
        this.that = proxyActivity;
    }


    @Override
    public void setContentView(View view) {
        if (that != null) {
            that.setContentView(view);
        }else {
            super.setContentView(view);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        if (that != null) {
            that.setContentView(layoutResID);
        }else{
            super.setContentView(layoutResID);
        }

    }

    @Override
    public ComponentName startService(Intent service) {
        Intent m = new Intent();
        m.putExtra("serviceName", service.getComponent().getClassName());
        if (that != null) {
            return that.startService(m);
        }else{
            return super.startService(m);
        }

    }

    @Override
    public View findViewById(int id) {
        if (that != null) {
            return that.findViewById(id);
        }else{
            return super.findViewById(id);
        }

    }

    @Override
    public Intent getIntent() {
        if(that!=null){
            return that.getIntent();
        }
        return super.getIntent();
    }
    @Override
    public ClassLoader getClassLoader() {
        if (that != null) {
            return that.getClassLoader();
        }else{
            return super.getClassLoader();
        }

    }


    @Override
    public void startActivity(Intent intent) {
//        ProxyActivity --->className
        Intent m = new Intent();
        m.putExtra("className", intent.getComponent().getClassName());
        if (that != null) {
            that.startActivity(m);
        }else{
            super.startActivity(intent);
        }
    }


    @NonNull
    @Override
    public LayoutInflater getLayoutInflater() {
        if (that != null) {
            return that.getLayoutInflater();
        }else{
            return super.getLayoutInflater();
        }
    }

    @Override
    public ApplicationInfo getApplicationInfo() {
        if (that != null) {
            return that.getApplicationInfo();
        }else{
            return super.getApplicationInfo();
        }

    }


    @Override
    public Window getWindow() {
        if (that != null) {
            return that.getWindow();
        }else{
            return super.getWindow();
        }
    }


    @Override
    public WindowManager getWindowManager() {
        if (that != null) {
            return that.getWindowManager();
        }else{
            return super.getWindowManager();
        }

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
       Log.e("TAG", "BaseActivity onCreate:" );
    }

    @Override
    public void onStart() {
        Log.e("TAG", "BaseActivity onStart:" );
    }

    @Override
    public void onResume() {
        Log.e("TAG", "BaseActivity onResume:" );
    }

    @Override
    public void onPause() {
        Log.e("TAG", "BaseActivity onPause:" );
    }

    @Override
    public void onStop() {
        Log.e("TAG", "BaseActivity onStop:" );
    }

    @Override
    public void onDestroy() {
        Log.e("TAG", "BaseActivity onDestroy:" );
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }
}
