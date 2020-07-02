package com.dongnao.ls19pluginframework;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dongnao.ls19pluginframework.pluigin.core.PluginManager;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void load(View view) {
        File file = new File(Environment.getExternalStorageDirectory(), "plugin-debug.apk");
        PluginManager.getInstance().installPackage(file.getAbsolutePath(), 0);

    }

    public void start(View view) {
        PluginManager.getInstance().init(this.getApplicationContext());
        PluginManager.getInstance().connectToService();

    }
    public void jump(View view) {
//        MainActivity   单独开房间
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.dongnao.barry.plugin",
                "com.dongnao.barry.plugin.MainActivity"));
        startActivity(intent);

    }
}
