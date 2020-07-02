package com.dongnao.ls19pluginframework.pluigin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dongnao.ls19pluginframework.R;

/**
 * Created by Administrator on 2018/4/13.
 */

public class ProxyActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxy);
    }
}
