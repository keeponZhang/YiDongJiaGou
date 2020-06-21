package com.example.baby.hookframework;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import dalvik.system.DexClassLoader;
import dalvik.system.PathClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClassLoader cl = getClassLoader();
        Log.i("David", "onCreate: "+cl);

    }

    public void jump2(View view) {

//系统   如何找到一个资源
        Intent intent = new Intent();

//        Class.forName    融合的过程
        intent.setComponent(new ComponentName("com.dongnao.barry.plugin",
                "com.dongnao.barry.plugin.SceondActivity"));

        startActivity(intent);
//        classNotFind()
        HookUtil hookUtil = new HookUtil();

    }
    public void jump3(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.dongnao.barry.plugin",
                "com.dongnao.barry.plugin.ThreeActivity"));
        startActivity(intent);
    }

    @Override
    public Resources getResources() {
        return super.getResources();
    }

    public void jump4(View view) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.dongnao.barry.plugin",
                "com.dongnao.barry.plugin.ThirdActivity"));
        startActivity(intent);
    }
    public void logout(View view) {
        SharedPreferences share = this.getSharedPreferences("david", MODE_PRIVATE);//实例化
        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
        editor.putBoolean("login",false);   //设置保存的数据
        Toast.makeText(this, "退出登录成功",Toast.LENGTH_SHORT).show();
        editor.commit();    //提交数据保存
    }
}
