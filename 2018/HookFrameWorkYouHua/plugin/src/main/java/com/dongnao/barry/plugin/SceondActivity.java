package com.dongnao.barry.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

/**
 * Created by 48608 on 2018/1/12.
 */

public class SceondActivity extends  BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SceondActivity.this, "text---->  ", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
